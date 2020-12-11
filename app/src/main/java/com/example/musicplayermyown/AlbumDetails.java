package com.example.musicplayermyown;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.musicplayermyown.MainActivity.AllAudioList;

public class AlbumDetails extends AppCompatActivity {

    ImageView imageViewAlbum;
    static String albumName;
    ListView listView ;
    static ArrayList<MusicFile> allbumFiles = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        allbumFiles.clear();
        getIntentData();
    }

    public void getIntentData(){

        listView = findViewById(R.id.list_songalbum);

        albumName = "";
        try{
            albumName = getIntent().getStringExtra("albumName");
        }finally {

        }
        for(int i = 0;i<AllAudioList.size();i++){
            if(albumName.equals(AllAudioList.get(i).getAlbum())){
                allbumFiles.add(AllAudioList.get(i));
            }
        }

        AlbumdetailAdapter albumdetailAdapter = new AlbumdetailAdapter(this,allbumFiles);

        listView.setAdapter(albumdetailAdapter);
    }
}