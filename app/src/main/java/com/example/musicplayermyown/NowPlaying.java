package com.example.musicplayermyown;

import android.content.Context;
import android.os.Binder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.musicplayermyown.PlaySongActivity.artist_name;
import static com.example.musicplayermyown.PlaySongActivity.isPlaying;
import static com.example.musicplayermyown.PlaySongActivity.playPauseBtn;
import static com.example.musicplayermyown.PlaySongActivity.playSongContext;
import static com.example.musicplayermyown.PlaySongActivity.song_name;

public class NowPlaying extends Fragment implements AcctionNowPlayingBottom{

    ImageView nextBtn,previousBtn,albumArt;
    TextView songName,songArtist;
    FloatingActionButton playPauseBtnBottom;
    View view;
    ActionPlaying actionPlaying;
    PlaySongActivity playSongActivity;
    public NowPlaying() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_now_playing, container, false);
        initViews();
        actionPlaying = (ActionPlaying) playSongContext;
        playSongActivity = (PlaySongActivity) playSongContext;
        if(playSongActivity!=null){
            playSongActivity.setNowPlaying(this);
        }
        if(isPlaying){
            playPauseBtnBottom.setImageResource(R.drawable.ic_pause);
        }
        setText();
        playPauseBtnBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actionPlaying!=null){
                    if(isPlaying){
                        playPauseBtnBottom.setImageResource(R.drawable.ic_play);
                    }
                    else {
                        playPauseBtnBottom.setImageResource(R.drawable.ic_pause);
                    }
                    actionPlaying.playPauseBtnClicked();
                    setText();
                }
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actionPlaying!=null){
                    if(isPlaying){
                        playPauseBtnBottom.setImageResource(R.drawable.ic_pause);
                    }
                    else {
                        playPauseBtnBottom.setImageResource(R.drawable.ic_play);
                    }
                    actionPlaying.nextBtnClicked();
                    setText();
                }
            }
        });
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actionPlaying!=null){
                    if(isPlaying){
                        playPauseBtnBottom.setImageResource(R.drawable.ic_pause);
                    }
                    else {
                        playPauseBtnBottom.setImageResource(R.drawable.ic_play);
                    }
                    actionPlaying.prevBtnClicked();
                    setText();
                }
            }
        });
        return view;
    }

    void initViews(){
        nextBtn = view.findViewById(R.id.btn_skip_next_bottom);
        previousBtn = view.findViewById(R.id.btn_skip_previous_bottom);
        playPauseBtnBottom = view.findViewById(R.id.btn_playPause_bottom);
        albumArt = view.findViewById(R.id.bottom_Album_Art);
        songName = view.findViewById(R.id.song_name_nowPlaying);
        songArtist = view.findViewById(R.id.song_artist_nowPlaying);
    }
    public void setText(){
        try {
            songName.setText(song_name.getText());
            if(artist_name.getText()==null){
                artist_name.setText("Unknow");
            }
            else {
                songArtist.setText(artist_name.getText());
            }
        }catch (Exception e){

        }
    }
}