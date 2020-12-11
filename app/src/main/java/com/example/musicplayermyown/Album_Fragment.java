package com.example.musicplayermyown;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.example.musicplayermyown.MainActivity.AllAudioList;
import static com.example.musicplayermyown.MainActivity.AllbumFile;

public class Album_Fragment extends Fragment {
    private View RootView ;
    private AlbumAdapter albumAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RootView = inflater.inflate(R.layout.album_fragment, container, false);

        GridView listView = (GridView) RootView.findViewById(R.id.grid_list_song);
        if (!(AllbumFile.size() < 1)) {
            albumAdapter = new AlbumAdapter(getContext(), AllbumFile);
        }
        listView.setAdapter(albumAdapter);
        return RootView;
    }
}
