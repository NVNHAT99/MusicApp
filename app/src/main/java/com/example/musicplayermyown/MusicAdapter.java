package com.example.musicplayermyown;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MusicAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MusicFile> list_song;

    MusicAdapter(Context mcontex, ArrayList<MusicFile> mFiles) {
        this.context = mcontex;
        this.list_song = mFiles;
    }

    @Override
    public int getCount() {
        return list_song.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.song_items, null);
        // init view
        ImageView songImg = (ImageView) view.findViewById(R.id.song_img);
        TextView songName = (TextView) view.findViewById(R.id.song_name);

        // load image Album Art for imageview by Picaso with Uri using album_id
        Picasso.get().load( getImage(list_song.get(position).getAlbumId()))
                .error(R.drawable.default_song_img)
                .fit()
                .into(songImg);


        songName.setText(list_song.get(position).getTitle());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PlaySongActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("sender","AllSong");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });

        return view;
    }
    // this fuction get uri by albumId to load image by picaso in line 57
    public static Uri getImage(Long albumId){
        Uri uri = Uri.parse("content://media/external/audio/albumart");
        return ContentUris.withAppendedId(uri,albumId);
    }
}
