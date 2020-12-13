package com.example.musicplayermyown;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.musicplayermyown.PlaySongActivity.position;
import static com.example.musicplayermyown.PlaySongActivity.sender;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager ;
    FrameLayout frag_bottom_player_nowplaying;
    public static int  REQUEST_CODE = 1;
    static ArrayList<MusicFile> AllAudioList;
    static ArrayList<MusicFile> AllbumFile = new ArrayList<>();
    ArrayList<String> AllbumName = new ArrayList<>();
    static ArrayList<Playlist> playlist = new ArrayList<>();
    static Database_Music database_music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // create database
        database_music = new Database_Music(getApplicationContext(),"MyAppMusic",null,1);
        database_music.CreateTable();
        permission();
        try {
            frag_bottom_player_nowplaying.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position!=-1){
                        int currentPosition = position;
                        String currentSender = sender;
                        Intent intent = new Intent(getApplicationContext(),PlaySongActivity.class);
                        intent.putExtra("sender",currentSender);
                        intent.putExtra("position",currentPosition);
                        intent.putExtra("sender2","fromNowPlaying");
                        startActivity(intent);
                    }
                }
            });
        }catch (Exception e){

        }
        try{
            Cursor cursor = database_music.GetData("SELECT * FROM PlayList");
            playlist.clear();
            while (cursor.moveToNext()){
                playlist.add(new Playlist(cursor.getLong(0),cursor.getString(1)));
            }
        }catch (Exception e){

        }
        try{
            Cursor cursor = database_music.GetData("SELECT * FROM PlayList");
            playlist.clear();
            while (cursor.moveToNext()){
                playlist.add(new Playlist(cursor.getLong(0),cursor.getString(1)));
            }
        }catch (Exception e){

        }

    }

    public void initView(){
        viewPager =  findViewById(R.id.view_pager);
        viewPager.setAdapter(new adapterFragment(getSupportFragmentManager()));
        frag_bottom_player_nowplaying = findViewById(R.id.LayoutNowplaying);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void getAlbum(){
        for(int i = 0;i<AllAudioList.size();i++){
            if(!AllbumName.contains(AllAudioList.get(i).getAlbum())){

                AllbumName.add(AllAudioList.get(i).getAlbum());
                AllbumFile.add(AllAudioList.get(i));
            }
        }
    };
    private void permission(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
        }
        else{
            // TODO some thing you want here like make a toast
            AllAudioList = getAllAudio(this);
            initView();
            getAlbum();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

                AllAudioList = getAllAudio(this);
                initView();
                getAlbum();
            }else {
                Toast.makeText(MainActivity.this,"you need accses permission on setting",Toast.LENGTH_LONG);
                /*new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // this code will be executed after 2 seconds
                        closeNow();
                    }
                }, 2000);*/
            }
        }
    }

    private void closeNow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }
    public static ArrayList<MusicFile> getAllAudio(Context context) {
        ArrayList<MusicFile> AudioList = new ArrayList<MusicFile>();
        Uri allSongUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,// for path
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID
        };
        Cursor cursor = context.getContentResolver().query(allSongUri,projection,null,null,null);
        if(cursor!= null){
            while (cursor.moveToNext()){
                String Album = cursor.getString(0);
                String Title = cursor.getString(1);
                String Duration = cursor.getString(2);
                String Path = cursor.getString(3);
                String Artist = cursor.getString(4);
                long Id = cursor.getLong(5);
                long AlbumId = cursor.getLong(6);
                MusicFile musicFile = new MusicFile(Id,AlbumId,Path,Title,Artist,Album,Duration);
                AudioList.add(musicFile);
            }
            cursor.close();

        }
        return AudioList;
    }
}