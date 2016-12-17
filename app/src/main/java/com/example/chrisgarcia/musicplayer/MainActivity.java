package com.example.chrisgarcia.musicplayer;

import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public Button pauseButtonVar, playButtonVar, rewindButtonVar, fastForwardButtonVar, stopButtonVar;
    public TextView currentTimeViewVar, endTimeViewVar;
    public MediaPlayer songPlayer;
    public double currentTimeMS ,endTimeMS;

    public Handler myHandler;
    private SeekBar mySongBarVar;
    private int seekTime;

    static String songTitle, songArtist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        playButtonVar = (Button) findViewById(R.id.playButton);
        pauseButtonVar = (Button) findViewById(R.id.pauseButton);
        stopButtonVar = (Button) findViewById(R.id.stopButton);
        rewindButtonVar = (Button) findViewById(R.id.rewindButton);
        fastForwardButtonVar = (Button) findViewById(R.id.fastForwardButton);

        currentTimeViewVar = (TextView) findViewById(R.id.currentTimeView);
        endTimeViewVar = (TextView) findViewById(R.id.endTimeView);

        mySongBarVar= (SeekBar) findViewById(R.id.SeekBar);

        songPlayer = MediaPlayer.create(this, R.raw.smoothocean);

        MediaMetadataRetriever songInfo = new MediaMetadataRetriever();

        Uri mediaPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.smoothocean);
        songInfo.setDataSource(this, mediaPath);

        songTitle = songInfo.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        songArtist = songInfo.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);



        endTimeMS = songPlayer.getDuration();
        int endTimeMin = (int) endTimeMS/1000/60;
        int endTimeS = (int) (endTimeMS/1000) % 60;
        endTimeViewVar.setText(endTimeMin + " min," + endTimeS + " sec");

        mySongBarVar.setProgress((int) currentTimeMS);
        mySongBarVar.setMax((int) endTimeMS);

        pauseButtonVar.setEnabled(false);
        stopButtonVar.setEnabled(false);


        mySongBarVar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekTime = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                songPlayer.seekTo(seekTime);
                currentTimeMS = seekTime;
            }
        });

        myHandler.postDelayed(UpdateSongTime, 100);

    }

    public void playSong(View view){
        Toast.makeText(getApplicationContext(), "Playing song", Toast.LENGTH_SHORT).show();
        pauseButtonVar.setEnabled(true);
        playButtonVar.setEnabled(false);
        stopButtonVar.setEnabled(true);
        songPlayer.start();
    }
    public void pauseSong(View view){
        Toast.makeText(getApplicationContext(), "Pausing song", Toast.LENGTH_SHORT).show();
        pauseButtonVar.setEnabled(false);
        playButtonVar.setEnabled(true);
        songPlayer.pause();
    }

    public void stopSong(View view){
        Toast.makeText(getApplicationContext(), "Stopping song", Toast.LENGTH_SHORT).show();
        stopButtonVar.setEnabled(false);
        pauseButtonVar.setEnabled(false);
        playButtonVar.setEnabled(true);
        songPlayer.seekTo(0);
        songPlayer.pause();
    }

    public void fastForwardSong(View view){
        Toast.makeText(getApplicationContext(), "Foward 5 second", Toast.LENGTH_SHORT).show();
        if(currentTimeMS < endTimeMS-5000){
            songPlayer.seekTo( (int)currentTimeMS +5000);}
    }

    public void rewindSong(View view){
        Toast.makeText(getApplicationContext(), "Back 5 second", Toast.LENGTH_SHORT).show();
        if(currentTimeMS >5000){
            songPlayer.seekTo( (int)currentTimeMS -5000);}
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            currentTimeMS = songPlayer.getCurrentPosition();
            int currentTimeMin = (int) currentTimeMS/1000/60;
            int currentTimeS = (int) (currentTimeMS/1000) % 60;
            currentTimeViewVar.setText(currentTimeMin + " min, " + currentTimeS + " sec");

            myHandler.postDelayed(this, 100);
        }
    };

}
