package com.example.musicplayermyown;

public class MusicFile {
    private long Id;
    private long AlbumId;
    private String path;
    private String title;
    private String artist;
    private String album;
    private String duration;

    private boolean Checked = false;// only using for add song to playlist

    public MusicFile(long Id,long AlbumId,String path,String title,String artist,String album,String duration){
        this.setId(Id);
        this.setAlbumId(AlbumId);
        this.setPath(path);
        this.setTitle(title);
        this.setArtist(artist);
        this.setAlbum(album);
        this.setDuration(duration);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getAlbumId() {
        return AlbumId;
    }

    public void setAlbumId(long albumId) {
        AlbumId = albumId;
    }

    public boolean isChecked() {
        return Checked;
    }

    public void setChecked(boolean checked) {
        Checked = checked;
    }
}
