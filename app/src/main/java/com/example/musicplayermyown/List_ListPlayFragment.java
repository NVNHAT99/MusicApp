package com.example.musicplayermyown;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.example.musicplayermyown.MainActivity.AllbumFile;
import static com.example.musicplayermyown.MainActivity.database_music;
import static com.example.musicplayermyown.MainActivity.playlist;

public class List_ListPlayFragment extends Fragment {

    private View RootView ;
    PlayListAdapter playListAdapter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RootView = inflater.inflate(R.layout.list_playlistfragment,container,false);
        ListView listView = (ListView) RootView.findViewById(R.id.list_playlist);
        RelativeLayout btnAddNew = RootView.findViewById(R.id.btn_addnewPlaylist);
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddNewPlaylist();
            }
        });

        playListAdapter = new PlayListAdapter(getContext(), playlist);
        listView.setAdapter(playListAdapter);

        return RootView;
    }

    private void DialogAddNewPlaylist(){
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.add_new_playlist_dialog);

        EditText edtPlaylistName = dialog.findViewById(R.id.etext_playlistName);

        Button btnDone = dialog.findViewById(R.id.btnDone);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playlistName = edtPlaylistName.getText().toString().trim();
                database_music.QueryData("INSERT INTO PlayList VALUES(null,'" + playlistName  + "')");
                Cursor cursor = database_music.GetData("SELECT * FROM PlayList");
                playlist.clear();
                while (cursor.moveToNext()){
                    playlist.add(new Playlist(cursor.getLong(0),cursor.getString(1)));
                }
                dialog.dismiss();
                try {
                    playListAdapter.notifyDataSetChanged();
                }catch (Exception e){

                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}
