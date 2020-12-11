package com.example.musicplayermyown;

public class Playlist {
    private long playlistId;
    private String playlistName;

    public long getPlaylistId() {
        return playlistId;
    }

    Playlist(long playlistId,String playlistName){
        this.setPlaylistId(playlistId);
        this.playlistName = playlistName;
    }

    public void setPlaylistId(long playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }
}
