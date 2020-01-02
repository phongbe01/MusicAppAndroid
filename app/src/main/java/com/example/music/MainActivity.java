package com.example.music;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    ImageButton play,back,next;
    TextView txStart, txEnd, txTitle;
    SeekBar skTime;
    MediaPlayer mediaPlayer = null;
    ListView listView;

    int pauseCurrentPosition;
    ArrayList<Song> arraySong;
    ArrayList<String> arrayTitleSong;
    int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (ImageButton)findViewById(R.id.play);
        back = (ImageButton)findViewById(R.id.back);
        next = (ImageButton)findViewById(R.id.next);
        txStart = (TextView)findViewById(R.id.txStart);
        txEnd = (TextView)findViewById(R.id.txEnd);
        txTitle = (TextView)findViewById(R.id.txTitle);
        skTime = (SeekBar)findViewById(R.id.seekBar);
        listView = (ListView)findViewById(R.id.list_item);
        addSong();
        setPlay();
        setNext();
        setBack();

        ArrayList<String> arr = new ArrayList<String>();
        arr.add("A");
        arr.add("B");


        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,arrayTitleSong);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, arraySong.get(position).getTitle(),Toast.LENGTH_SHORT).show();
                if (mediaPlayer == null)
                {
                    createSong(position);
                }
                else if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    createSong(position);
                } else {
                    createSong(position);
                }

            }
        });
    }

    private void createSong(int position)
    {
        mediaPlayer = MediaPlayer.create(getApplicationContext(),arraySong.get(position).getFile());
        txTitle.setText(arraySong.get(position).getTitle());
        mediaPlayer.start();
        setTimeBegin();
        setTimeEnd();
        setSkTime();
        play.setImageResource(R.drawable.pause_icon);
    }

    private void addSong() {
        arraySong = new ArrayList<>();
        arrayTitleSong = new ArrayList<>();
        arraySong.add(new Song("Bai 1", R.raw.music));
        arraySong.add(new Song("Bai 2", R.raw.music2));
        arraySong.add(new Song("Có một nơi như thế", R.raw.music3));
        arrayTitleSong.add("Bai 1");
        arrayTitleSong.add("Bai 2");
        arrayTitleSong.add("Có một nơi như thế");
    }

    private void setPlay()
    {
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer == null)
                {
                    createSong(position);
                }
                else if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    pauseCurrentPosition = mediaPlayer.getCurrentPosition();

                    mediaPlayer.seekTo(pauseCurrentPosition);
                    play.setImageResource(R.drawable.play_icon);
                } else {
                    play.setImageResource(R.drawable.pause_icon);
                    mediaPlayer.start();
                }
            }
        });
    }

    private void setNext()
    {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;

                if (position > arraySong.size() -1)
                {
                    position = 0;
                }
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                }
                createSong(position);
            }
        });
    }

    private void setBack()
    {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;

                if (position < 0 )
                {
                    position = arraySong.size() -1;
                }
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                }
                createSong(position);
            }
        });
    }

    private void setTimeEnd()
    {
        SimpleDateFormat total = new SimpleDateFormat("mm:ss");
        txEnd.setText(total.format(mediaPlayer.getDuration()));
        // gan max seekbar
        skTime.setMax(mediaPlayer.getDuration());
    }

    private void setSkTime()
    {
        skTime = (SeekBar)findViewById(R.id.seekBar);
        if (mediaPlayer != null)
        {
            skTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    mediaPlayer.seekTo(skTime.getProgress());
                }
            });
        }
    }

    private  void setTimeBegin()
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat time = new SimpleDateFormat("mm:ss");
                txStart.setText(time.format(mediaPlayer.getCurrentPosition()));
                skTime.setProgress(mediaPlayer.getCurrentPosition());
                // kiem tra bai hat hoan tat
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position++;

                        if (position > arraySong.size() -1)
                        {
                            position = 0;
                        }
                        if (mediaPlayer.isPlaying())
                        {
                            mediaPlayer.stop();
                        }
                        createSong(position);
                    }
                });
                handler.postDelayed(this,500);
            }
        },100);
    }

}
