package com.example.musicplayermyown;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static com.example.musicplayermyown.AlbumDetails.allbumFiles;
import static com.example.musicplayermyown.ApplicationClass.ACTION_NEXT;
import static com.example.musicplayermyown.ApplicationClass.ACTION_PLAY;
import static com.example.musicplayermyown.ApplicationClass.ACTION_PREVIOUS;
import static com.example.musicplayermyown.ApplicationClass.CHANNEL_ID_1;
import static com.example.musicplayermyown.ApplicationClass.CHANNEL_ID_2;
import static com.example.musicplayermyown.MainActivity.AllAudioList;
import static com.example.musicplayermyown.MusicAdapter.getImage;
import static com.example.musicplayermyown.PlaylistDetal.PlaylistID;
import static com.example.musicplayermyown.PlaylistDetal.playlistDetail;
import static com.example.musicplayermyown.AlbumDetails.albumName;

public class PlaySongActivity extends AppCompatActivity implements ActionPlaying , ServiceConnection {

    TextView duration_played, duaration_toal;
    static TextView song_name, artist_name;
    ImageView cover_art, nextBtn, prevBtn, backBtn, shuffleBtn, repeatBtn,playListDetailBtn;
    static FloatingActionButton playPauseBtn;
    SeekBar seekBar;
    static int position = -1;
    boolean shuffe = false,repeat = false;
    static ArrayList<MusicFile> listSong = new ArrayList<>();
    static Uri uri;
    //static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playThread, preThread, nextThread;
    static long CurrentPlaylistId = -1;
    MusicService musicService;
    MediaSessionCompat mediaSession;
    static String sender;
    static String sender2 = null;
    static Context playSongContext;
    static boolean isPlaying = false;

    // for now playing bottom
    NowPlaying nowPlayingBottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initViews();
        mediaSession = new MediaSessionCompat(getBaseContext(),"my Audio");
        getintentMethod();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (musicService != null && fromUser) {
                    musicService.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlaySongActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (musicService != null) {
                    setCurrentPosition();
                }
                handler.postDelayed(this, 1000);
            }
        });
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffe){
                    shuffe= false;
                    shuffleBtn.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.white));
                }else{
                    shuffe= true;
                    shuffleBtn.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.blue));
                }
            }
        });
        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeat){
                    repeat = false;
                    repeatBtn.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.white));
                }else{
                    repeat = true;
                    repeatBtn.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.blue));
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        playListDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PlaylistID!=-1){
                    long clone_playlistId = PlaylistID;
                    Intent intent = new Intent(getApplicationContext(),PlaylistDetal.class);
                    intent.putExtra("PlaylistId",clone_playlistId);
                    startActivity(intent);
                }
                else if(albumName!=null) {
                    String clone_albumName = albumName;
                    Intent intent = new Intent(getApplicationContext(), AlbumDetails.class);
                    intent.putExtra("albumName", clone_albumName);
                    startActivity(intent);
                }
                else if(albumName==null && PlaylistID==-1){
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        playSongContext = this;
    }

    public void setCurrentPosition(){
        int currentPosition = musicService.getCurrentPosition() / 1000;
        seekBar.setProgress(currentPosition);
        duration_played.setText(forMatTime(currentPosition));
    }
    private String forMatTime(int currentPosition) {
        String totalout = "";
        String totalNew = "";
        String seconds = String.valueOf(currentPosition % 60);
        String minutes = String.valueOf(currentPosition / 60);
        totalout = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalout;
        }
    }


    private void getintentMethod() {

        try {
            sender2 = getIntent().getStringExtra("sender2");

        }catch (Exception e){

        }
        sender = getIntent().getStringExtra("sender");
        position = getIntent().getIntExtra("position", -1);
        if(sender.equals("albumDetails")){
            listSong = allbumFiles;
            PlaylistID = - 1;
        }
        else if(sender.equals("PlaylistDetail")){
            listSong = playlistDetail;
            CurrentPlaylistId = PlaylistID;
            albumName = null;
        }
        else {
            listSong = AllAudioList;
            PlaylistID = - 1;
            albumName = null;
        }
        if (listSong != null) {
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, listSong.get(position).getId());

        }
        if(sender2==null){
            Intent intent = new Intent(this,MusicService.class);
            intent.putExtra("SevicePosition",position);
            startService(intent);

        }
    }

    private void initViews() {
        song_name = findViewById(R.id.song_name);
        artist_name = findViewById(R.id.song_artist);
        duration_played = findViewById(R.id.duarationPlayed);
        duaration_toal = findViewById(R.id.duarationTotal);
        cover_art = findViewById(R.id.cover_art);
        nextBtn = findViewById(R.id.next);
        prevBtn = findViewById(R.id.previos);
        backBtn = findViewById(R.id.back_btn);
        shuffleBtn = findViewById(R.id.shuffle);
        repeatBtn = findViewById(R.id.repeate);
        playPauseBtn = findViewById(R.id.play_pause);
        seekBar = findViewById(R.id.seek_barr);
        playListDetailBtn = findViewById(R.id.menu_Detail_btn);

    }

    private void metaData(Uri uri) {
        int durationTotal = Integer.parseInt(listSong.get(position).getDuration()) / 1000;
        duaration_toal.setText(forMatTime(durationTotal));

    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this,MusicService.class);
        bindService(intent,this,BIND_AUTO_CREATE);
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    private void prevThreadBtn() {
        preThread = new Thread() {
            @Override
            public void run() {
                super.run();
                prevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prevBtnClicked();
                    }
                });
            }
        };
        preThread.start();

    }

    public void prevBtnClicked() {

        if(musicService.isPlaying()){
            musicService.stop();
            musicService.release();
            if(shuffe && (!repeat)){
                // get random
                position = randomPosition();
            }else if(shuffe && repeat){
                // do no thing, position not change
            }
            else if(!shuffe && !repeat){
                // get prev song
                position = ((position - 1) < 0 ? (listSong.size()-1) : (position-1));
            }

            song_name.setText(listSong.get(position).getTitle());
            artist_name.setText(listSong.get(position).getArtist());
            uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, listSong.get(position).getId());
            ImageAnimation(this,cover_art,listSong.get(position).getAlbumId());
            metaData(uri);
            try {
                musicService.setDataSource(getApplicationContext(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                musicService.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            musicService.start();
            seekBar.setMax(musicService.getDuration() / 1000);
            PlaySongActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.OnCompleted();
            musicService.ShowNotification(R.drawable.ic_pause);
            playPauseBtn.setImageResource(R.drawable.ic_pause);
        }
        else {
            musicService.release();
            if(shuffe && (!repeat)){
                // get random
                position = randomPosition();
            }else if(shuffe && repeat){
                // do no thing, position not change
            }
            else if(!shuffe && !repeat){
                // get prev song
                position = ((position - 1) < 0 ? (listSong.size()-1) : (position-1));
            }

            song_name.setText(listSong.get(position).getTitle());
            artist_name.setText(listSong.get(position).getArtist());
            ImageAnimation(this,cover_art,listSong.get(position).getId());
            uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, listSong.get(position).getId());
            metaData(uri);
            try {
                musicService.setDataSource(getApplicationContext(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                musicService.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            musicService.start();
            seekBar.setMax(musicService.getDuration() / 1000);
            PlaySongActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.OnCompleted();
            musicService.ShowNotification(R.drawable.ic_play);
            playPauseBtn.setImageResource(R.drawable.ic_play);
        }
        try {
            nowPlayingBottom.setText();
        }catch (Exception e){

        }

    }

    private void nextThreadBtn() {
        nextThread = new Thread() {
            @Override
            public void run() {
                super.run();
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread.start();
    }

    public void nextBtnClicked() {
        if(musicService.isPlaying()){
            musicService.stop();
            musicService.release();
            if(shuffe && (!repeat)){
                // get random
                position = randomPosition();
            }
            else if(!shuffe && !repeat){
                // get next song
                position = ((position+1) % listSong.size());
            }

            song_name.setText(listSong.get(position).getTitle());
            artist_name.setText(listSong.get(position).getArtist());
            uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, listSong.get(position).getId());
            ImageAnimation(this,cover_art,listSong.get(position).getAlbumId());
            metaData(uri);
            try {
                musicService.setDataSource(getApplicationContext(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                musicService.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            musicService.start();
            seekBar.setMax(musicService.getDuration() / 1000);
            PlaySongActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.OnCompleted();
            musicService.ShowNotification(R.drawable.ic_pause);
            playPauseBtn.setImageResource(R.drawable.ic_pause);
        }
        else {
            musicService.release();
            if(shuffe && (!repeat)){
                // get random
                position = randomPosition();
            }else if(shuffe && repeat){
                // do no thing, position not change
            }
            else if(!shuffe && !repeat){
                // get next song
                position = ((position+1) % listSong.size());
            }

            song_name.setText(listSong.get(position).getTitle());
            artist_name.setText(listSong.get(position).getArtist());
            uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, listSong.get(position).getId());
            ImageAnimation(this,cover_art,listSong.get(position).getAlbumId());

            metaData(uri);
            try {
                musicService.setDataSource(getApplicationContext(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                musicService.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            seekBar.setMax(musicService.getDuration() / 1000);
            musicService.OnCompleted();
            musicService.ShowNotification(R.drawable.ic_play);
            playPauseBtn.setImageResource(R.drawable.ic_play);
        }
        try {
            nowPlayingBottom.setText();
        }catch (Exception e){

        }
    }

    private void playThreadBtn() {
        playThread = new Thread() {
            @Override
            public void run() {
                super.run();
                playPauseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    public void playPauseBtnClicked() {
        if (musicService.isPlaying()) {
            isPlaying = false;
            playPauseBtn.setImageResource(R.drawable.ic_play);
            musicService.ShowNotification(R.drawable.ic_play);
            musicService.pause();
            seekBar.setMax(musicService.getDuration() / 1000);
            PlaySongActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
        else {
            isPlaying = true;
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            musicService.ShowNotification(R.drawable.ic_pause);
            musicService.start();
            seekBar.setMax(musicService.getDuration() / 1000);
            PlaySongActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }

    // set image animation App
    public void ImageAnimation(Context context, ImageView imageView, Long AlbumId){
        Animation animOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        Animation animIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Picasso.get().load(getImage(AlbumId))
                        .error(R.drawable.default_song_img)
                        .fit()
                        .into(imageView);
                animIn.setAnimationListener(new Animation.AnimationListener(){
                    @Override
                    public void onAnimationStart(Animation animation){
                    }
                    @Override
                    public void onAnimationEnd(Animation animation){
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation){
                    }
                });
                imageView.startAnimation(animIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imageView.startAnimation(animOut);

    }


    public int randomPosition(){
        Random random = new Random();
        int currentPosition = position;
        int newPosition;
        do {
            newPosition = random.nextInt(listSong.size() - 1);
        }while (currentPosition == newPosition);
        return newPosition;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
        musicService = myBinder.getService();
        musicService.setCallBack(this);

        seekBar.setMax(musicService.getDuration() / 1000);
        setCurrentPosition();
        metaData(uri);
        song_name.setText(listSong.get(position).getTitle());
        artist_name.setText(listSong.get(position).getArtist());
        musicService.OnCompleted();
        if(musicService.isPlaying()){
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            musicService.ShowNotification(R.drawable.ic_pause);
        }else {
            playPauseBtn.setImageResource(R.drawable.ic_play);
            musicService.ShowNotification(R.drawable.ic_play);
        }

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;
    }

    void setNowPlaying(NowPlaying nowPlaying){
        this.nowPlayingBottom = nowPlaying;
    }

}