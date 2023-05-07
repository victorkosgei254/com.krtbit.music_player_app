package com.kritbit.music_player;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button equilizer, rewind, skip_back, play_pause, skip_forward, forward, playlist, get_lyrics;
    TextView song_name, current_time, song_time;
    SeekBar seek_bar;
    MediaPlayer player;
    Handler handler;
    double startTime, finalTime;
    int forwardTime = 1000;
    boolean isPlaying = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Buttons
        equilizer = findViewById(R.id.equilizer);
        rewind = findViewById(R.id.rewind);
        skip_back = findViewById(R.id.skip_back);
        play_pause = findViewById(R.id.play_pause);
        skip_forward = findViewById(R.id.skip_forward);
        forward = findViewById(R.id.forward);
        playlist = findViewById(R.id.playlist);
        get_lyrics = findViewById(R.id.get_lyrics);

        //Seekbar
        seek_bar = findViewById(R.id.seek_bar);

        //Song time
        song_time = findViewById(R.id.end_time);
        current_time = findViewById(R.id.play_time);

        //Song title
        song_name = findViewById(R.id.song_title);

        handler = new Handler();

        startTime = 0.00; finalTime = 0.00;

        player = MediaPlayer.create(this, R.raw.lkb_upbeat_hip_hop);

        seek_bar.setClickable(false);

        //Play or stop music
        play_pause.setOnClickListener(view -> {
            if(isPlaying)
            {
                player.pause();
                isPlaying = false;
                play_pause.setBackground(getResources().getDrawable(R.drawable.ic_play));
            }
            else {

                playMusic();
            }
        });

        //Forward music
        forward.setOnClickListener(view->{
            int temp = (int)startTime;
            if((temp + forwardTime) <= finalTime)
            {
                startTime = startTime + forwardTime;
                player.seekTo((int)startTime);
            }
            else
            {
                Toast.makeText(MainActivity.this,
                        "You have reached the end of the song",
                        Toast.LENGTH_LONG).show();
            }
        });

        //Rewind music
        rewind.setOnClickListener(it->{
            int temp = (int)startTime;

            if((temp - forwardTime) > 0)
            {
                startTime = startTime - forwardTime;
                player.seekTo((int)startTime);
            }
            else
            {
                Toast.makeText(MainActivity.this,
                        "Can't go any further", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void playMusic()
    {
        player.start();
        isPlaying=true;
        play_pause.setBackground(getResources().getDrawable(R.drawable.ic_pause));
        finalTime = player.getDuration();
        startTime = player.getCurrentPosition();

        //----
        seek_bar.setMax((int)finalTime);
        song_time.setText(String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long)finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long)finalTime) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes((long)finalTime))
        ));
        seek_bar.setProgress((int)startTime);
        handler.postDelayed(updateSongTime,100);

    }

    //Creating a runnable
    private Runnable updateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = player.getCurrentPosition();
            current_time.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long)startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long)startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)startTime))
                    ));

            seek_bar.setProgress((int)startTime);
            handler.postDelayed(this,100);
            if (startTime == finalTime)
            {
                play_pause.setBackground(getResources().getDrawable(R.drawable.ic_play));
            }
        }
    };


}