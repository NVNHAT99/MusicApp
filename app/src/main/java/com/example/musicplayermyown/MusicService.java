package com.example.musicplayermyown;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.example.musicplayermyown.ApplicationClass.ACTION_NEXT;
import static com.example.musicplayermyown.ApplicationClass.ACTION_PLAY;
import static com.example.musicplayermyown.ApplicationClass.ACTION_PREVIOUS;
import static com.example.musicplayermyown.ApplicationClass.CHANNEL_ID_2;
import static com.example.musicplayermyown.PlaySongActivity.isPlaying;
import static com.example.musicplayermyown.PlaySongActivity.listSong;
import static com.example.musicplayermyown.PlaySongActivity.playPauseBtn;
import static com.example.musicplayermyown.PlaySongActivity.position;
import static com.example.musicplayermyown.PlaySongActivity.sender;
import static com.example.musicplayermyown.PlaySongActivity.uri;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    IBinder mBinder = new MyBinder();
    MediaPlayer mediaPlayer;
    ArrayList<MusicFile> musicFiles;
    int positionService;
    ActionPlaying actionPlaying;
    PowerManager.WakeLock wakeLock;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getCanonicalName());
        wakeLock.acquire();
        musicFiles = listSong;
        //ShowNotification;
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void pause() {
        mediaPlayer.pause();
    }



    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int myPosition = intent.getIntExtra("SevicePosition",-1);
        String actionName = intent.getStringExtra("ActionName");
        if(myPosition!=-1){
            playMedia(myPosition);
        }
        if(actionName !=null ){
            switch (actionName){
                case "playPause":
                    if(actionPlaying!=null){
                        actionPlaying.playPauseBtnClicked();
                    }
                    break;
                case "next":
                    if(actionPlaying!=null){
                        actionPlaying.nextBtnClicked();
                    }
                    break;
                case "previous":
                    if(actionPlaying!=null){
                        actionPlaying.prevBtnClicked();
                    }
                    break;
            }
        }
        return START_STICKY;
    }

    private void playMedia(int startPositon) {
        musicFiles = listSong;
        positionService = startPositon;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            if(musicFiles!=null){
                Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, listSong.get(positionService).getId());
                try {
                    mediaPlayer.setDataSource(getApplicationContext(),uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        }else {
            mediaPlayer = new MediaPlayer();
            if(musicFiles!=null){
                Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, listSong.get(positionService).getId());
                try {
                    mediaPlayer.setDataSource(getApplicationContext(),uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }

        }
        isPlaying = true;
        OnCompleted();
    }

    void start() {
        mediaPlayer.start();

    }

    boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    void stop() {
        mediaPlayer.stop();

    }

    void release() {
        mediaPlayer.release();
    }

    int getDuration() {
        return mediaPlayer.getDuration();
    }

    void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    void setDataSource(Context context, Uri uri) throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(context, uri);
    }

    void prepare() throws IOException {
        mediaPlayer.prepare();
    }

    void OnCompleted(){
        mediaPlayer.setOnCompletionListener(this);
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        if(actionPlaying!=null){
            actionPlaying.nextBtnClicked();
        }
        try {
            stop();
            mediaPlayer.reset();
            release();
            setDataSource(getApplicationContext(),uri);
            prepare();
            start();
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            ShowNotification(R.drawable.ic_pause);
            OnCompleted();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // to set actionPlaying;
    void setCallBack(ActionPlaying actionPlaying){
        this.actionPlaying = actionPlaying;
    }

    void ShowNotification(int playPauseBtn){

        Intent intent = new Intent(this,PlaySongActivity.class);
        intent.putExtra("sender",sender);
        intent.putExtra("position",position);
        intent.putExtra("sender2","fromNotification");
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        Intent prevIntent = new Intent(this,NotificationReceiver.class).setAction(ACTION_PREVIOUS);
        PendingIntent prevPending = PendingIntent.getBroadcast(this,0,prevIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playPauseIntent = new Intent(this,NotificationReceiver.class).setAction(ACTION_PLAY);
        PendingIntent playPausePending = PendingIntent.getBroadcast(this,0,playPauseIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent(this,NotificationReceiver.class).setAction(ACTION_NEXT);
        PendingIntent nextPending = PendingIntent.getBroadcast(this,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap picture = BitmapFactory.decodeResource(getResources(),R.drawable.default_song_img);
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID_2)
                .setSmallIcon(R.drawable.app_icon_2)
                .setLargeIcon(picture)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setContentTitle(musicFiles.get(position).getTitle())
                .setContentText(musicFiles.get(position).getArtist())
                .addAction(R.drawable.ic_skip_previous,"Previous",prevPending)
                .addAction(playPauseBtn,"Pause",playPausePending)
                .addAction(R.drawable.ic_skip_next,"Next",nextPending)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0,1,2))
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setNotificationSilent()
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setShowWhen(false)
                .build();
        startForeground(2,notification);
    }
}