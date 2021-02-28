package com.koral.application.converter;

import com.koral.application.model.Song;

import java.util.List;

public class ReturnInfo {
    private static List<Song> songs;
    private static String playlist_url;

    public ReturnInfo(List<Song> songs, String playlist_url) {
        this.songs = songs;
        this.playlist_url = playlist_url;
    }

    public ReturnInfo() {
    }

    public static List<Song> getSongs() {
        return songs;
    }

    public static void setSongs(List<Song> songs) {
        ReturnInfo.songs = songs;
    }

    public static String getPlaylist_url() {
        return playlist_url;
    }

    public static void setPlaylist_url(String playlist_url) {
        ReturnInfo.playlist_url = playlist_url;
    }
}
