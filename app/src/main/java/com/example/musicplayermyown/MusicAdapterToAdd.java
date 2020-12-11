package com.example.musicplayermyown;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.musicplayermyown.AddSongForPlayList.AddsongForPlaylistContex;
import static com.example.musicplayermyown.AddSongForPlayList.ListSongPlaylist;

public class MusicAdapterToAdd extends BaseAdapter {
    private Context context;
    private ArrayList<MusicFile> list_song;
    private boolean valueChangedFromUser = true;

    MusicAdapterToAdd(Context mcontex, ArrayList<MusicFile> mFiles) {
        this.context = mcontex;
        this.setList_song(mFiles);
    }

    @Override
    public int getCount() {
        return getList_song().size();
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

        ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.song_item_for_add_playlist, null);

            viewHolder = new ViewHolder();
            // init view
            viewHolder.songImg = (ImageView) convertView.findViewById(R.id.song_img);
            viewHolder.songName = (TextView) convertView.findViewById(R.id.song_name);
            viewHolder.checkBox = convertView.findViewById(R.id.chekc_song);
            convertView.setTag(viewHolder);
        }else {
            CheckBox checkBox = convertView.findViewById(R.id.chekc_song);

            // this line to remove event onCheckedChange because i think getTag call it again, make checkbox allways false , so it make we with view not
            // change before we click
            // and then will change ListSongPlaylist.get(position) too
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }
            });
            viewHolder = (ViewHolder) convertView.getTag();

        }
        // load image Album Art for imageview by Picaso with Uri using album_id
        Picasso.get().load(getImage(getList_song().get(position).getAlbumId()))
                .error(R.drawable.default_song_img)
                .fit()
                .into(viewHolder.songImg);


        viewHolder.songName.setText(getList_song().get(position).getTitle());
        if(getList_song().get(position).isChecked()){
            viewHolder.checkBox.setChecked(true);
        }else{
            viewHolder.checkBox.setChecked(false);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.checkBox.isChecked()) {
                    ListSongPlaylist.get(position).setChecked(false);
                    viewHolder.checkBox.setChecked(false);
                } else {
                    ListSongPlaylist.get(position).setChecked(true);
                    viewHolder.checkBox.setChecked(true);
                }
            }
        });
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ListSongPlaylist.get(position).setChecked(isChecked);
                viewHolder.checkBox.setChecked(isChecked);
            }
        });

        return convertView;
    }

    public static Uri getImage(Long albumId) {
        Uri uri = Uri.parse("content://media/external/audio/albumart");
        return ContentUris.withAppendedId(uri, albumId);
    }

    public ArrayList<MusicFile> getList_song() {
        return list_song;
    }

    private void setList_song(ArrayList<MusicFile> list_song) {
        this.list_song = list_song;
    }

    class ViewHolder {
        public ImageView songImg ;
        public TextView songName ;
        public CheckBox checkBox ;
    }
}
