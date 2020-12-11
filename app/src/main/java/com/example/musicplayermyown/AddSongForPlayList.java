package com.example.musicplayermyown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.musicplayermyown.MainActivity.AllAudioList;
import static com.example.musicplayermyown.MainActivity.database_music;
import static com.example.musicplayermyown.PlaylistDetal.playlistDetail;

public class AddSongForPlayList extends AppCompatActivity {

    ListView listView;
    TextView btn_back;
    TextView btn_selectAll;
    MusicAdapterToAdd musicAdapterToAdd;
    static ArrayList<MusicFile> ListSongPlaylist = new ArrayList<>();
    FloatingActionButton btn_done;
    boolean CheckAll = false;

    static Context AddsongForPlaylistContex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song_for_play_list);
        initView();
        AddsongForPlaylistContex = this;
        ListSongPlaylist = AllAudioList;
        long id = getIntent().getLongExtra("PlaylistId", -1);
        musicAdapterToAdd = new MusicAdapterToAdd(this, ListSongPlaylist);
        listView.setAdapter(musicAdapterToAdd);
        // set to all song not checked
        for (int i = 0; i < ListSongPlaylist.size(); i++) {
            ListSongPlaylist.get(i).setChecked(false);
        }
       noitifidataSetChanged();
        // set what song in list that song need checked

        SetCheckSong();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckAll) {
                    CheckAll = false;
                    for (int i = 0; i < ListSongPlaylist.size(); i++) {
                        ListSongPlaylist.get(i).setChecked(false);

                    }
                } else {
                    CheckAll = true;
                    for (int i = 0; i < AllAudioList.size(); i++) {
                        ListSongPlaylist.get(i).setChecked(true);
                    }
                }
                noitifidataSetChanged();
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < ListSongPlaylist.size(); i++) {
                    if (ListSongPlaylist.get(i).isChecked()) {
                        try {
                            database_music.QueryData("INSERT INTO PlayListDetail VALUES( " + id + "," +
                                    ListSongPlaylist.get(i).getId() +
                                    ")");
                        } catch (Exception e) {
                        }
                    }
                    else {
                        try {
                            database_music.QueryData("DELETE FROM  PlayListDetail WHERE PlayListId = " + id + " AND SongId = "  +
                                    ListSongPlaylist.get(i).getId() );
                        } catch (Exception e) {
                        }
                    }

                }
                AddSongForPlayList.super.onBackPressed();
            }
        });
    }


    private void initView() {
        listView = findViewById(R.id.list_AllSongToAdd);
        btn_back = findViewById(R.id.btnCancel);
        btn_selectAll = findViewById(R.id.btnCheckAll);
        btn_done = findViewById(R.id.btn_done);
    }

    // set check for list song
    private void SetCheckSong() {

        for (int i = 0; i < playlistDetail.size(); i++) {
            for (int j = 0; j < ListSongPlaylist.size(); j++) {
                if (ListSongPlaylist.get(j).getId() == playlistDetail.get(i).getId()) {
                    ListSongPlaylist.get(j).setChecked(true);
                }
            }
        }
        noitifidataSetChanged();
    }

    public void noitifidataSetChanged(){
        musicAdapterToAdd.notifyDataSetChanged();
    }
    public  void setListAdd(int position,boolean valueChecked){
        ListSongPlaylist.get(position).setChecked(valueChecked);
        musicAdapterToAdd.notifyDataSetChanged();
    }

}