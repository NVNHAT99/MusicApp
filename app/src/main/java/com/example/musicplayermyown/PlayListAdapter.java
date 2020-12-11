package com.example.musicplayermyown;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.musicplayermyown.MainActivity.database_music;

public class PlayListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Playlist> playlists;

    PlayListAdapter(Context mcontex, ArrayList<Playlist> playlists) {
        this.context = mcontex;
        this.playlists = playlists;
    }
    @Override
    public int getCount() {
        return playlists.size();
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
        View view = inflater.inflate(R.layout.playlist_item, null);
        // init view
        TextView songName = (TextView) view.findViewById(R.id.play_list_name);
        ImageView moreBtn = view.findViewById(R.id.btn_more);
        songName.setText(playlists.get(position).getPlaylistName());

        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener((item) ->{
                    switch (item.getItemId()){
                        case R.id.btn_delete:
                            deletePlaylist(position,v);
                    }
                    return true;
                } );
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PlaylistDetal.class);
                intent.putExtra("PlaylistId",playlists.get(position).getPlaylistId());
                context.startActivity(intent);
            }
        });
        return view;
    }

    private void deletePlaylist(int position, View v) {
        database_music.QueryData("DELETE FROM PlayListDetail WHERE PlayListId = " + playlists.get(position).getPlaylistId());
        database_music.QueryData("DELETE FROM PlayList WHERE Id = " +  playlists.get(position).getPlaylistId());
        playlists.remove(position);
        notifyDataSetChanged();
        Snackbar.make(v,"Playlist Delete",Snackbar.LENGTH_LONG).show();
    }

    private void  DeletePlaylist(){
        AlertDialog.Builder alerDialog = new AlertDialog.Builder(context);
        alerDialog.setTitle("message");
        alerDialog.setMessage("Do you want to delete this playlist ?");

    }
}
