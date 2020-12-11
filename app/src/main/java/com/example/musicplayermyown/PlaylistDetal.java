package com.example.musicplayermyown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.musicplayermyown.MainActivity.AllAudioList;
import static com.example.musicplayermyown.MainActivity.database_music;
import static com.example.musicplayermyown.PlaySongActivity.CurrentPlaylistId;
import static com.example.musicplayermyown.PlaySongActivity.listSong;

public class PlaylistDetal extends AppCompatActivity {

    static ArrayList<Long> playlistsongId = new ArrayList<>();
    static ArrayList<MusicFile> playlistDetail = new ArrayList<>();
    static long PlaylistID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_detal);
        PlaylistID = getIntent().getLongExtra("PlaylistId", -1);
        FloatingActionButton btn_addSong = findViewById(R.id.btn_addSong);
        ImageView btn_back = findViewById(R.id.btn_back);
        btn_addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddSongForPlayList.class);
                intent.putExtra("PlaylistId", PlaylistID);
                startActivity(intent);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaylistDetal.super.onBackPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        playlistsongId.clear();
        playlistDetail.clear();

        Cursor cursor = database_music.GetData("SELECT * FROM PlayListDetail WHERE PlayListId = " + PlaylistID);
        while (cursor.moveToNext()) {
            long SongId = cursor.getLong(1);
            playlistsongId.add(SongId);
        }
        if (playlistsongId.size() != 0) {
            for (int i = 0; i < AllAudioList.size(); i++) {
                if (playlistsongId.contains(AllAudioList.get(i).getId())) {
                    playlistDetail.add(AllAudioList.get(i));
                }
            }
        }
        if(PlaylistID == CurrentPlaylistId){
            listSong = playlistDetail;
        }
        MusicPlaylistDetailAdapter musicPlaylistDetailAdapter = new MusicPlaylistDetailAdapter(this, playlistDetail);
        ListView listView = findViewById(R.id.playlist_detail);
        listView.setAdapter(musicPlaylistDetailAdapter);
    }
}