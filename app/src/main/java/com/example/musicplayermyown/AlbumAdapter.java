package com.example.musicplayermyown;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MusicFile> allbumFile;

    AlbumAdapter(Context mcontex, ArrayList<MusicFile> mFiles) {
        this.context = mcontex;
        this.allbumFile = mFiles;
    }

    @Override
    public int getCount() {
        return allbumFile.size();
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
        View view = inflater.inflate(R.layout.album_item, null);
        // init view
        ImageView albumImage = (ImageView) view.findViewById(R.id.album_image);
        TextView albumName = (TextView) view.findViewById(R.id.album_name);

        // load image Album Art for imageview by Picaso with Uri using album_id
        Picasso.get().load( getImage(allbumFile.get(position).getAlbumId()))
                .error(R.drawable.default_song_img)
                .fit()
                .into(albumImage);


        albumName.setText(allbumFile.get(position).getAlbum());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AlbumDetails.class);

                intent.putExtra("albumName",allbumFile.get(position).getAlbum());
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
