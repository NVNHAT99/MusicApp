package com.example.musicplayermyown;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.example.musicplayermyown.MainActivity.AllAudioList;

public class All_songFragment extends Fragment {
    private View RootView ;
    private MusicAdapter musicAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RootView = inflater.inflate(R.layout.all_songfragment,container,false);

        ListView listView = (ListView) RootView.findViewById(R.id.list_AllSongToAdd);
        if(!(AllAudioList.size()<1)){
            musicAdapter = new MusicAdapter(getContext(),AllAudioList);
        }
        listView.setAdapter(musicAdapter);
        return RootView;
    }
}
