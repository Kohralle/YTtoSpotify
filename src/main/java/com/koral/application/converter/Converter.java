package com.koral.application.converter;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.koral.application.model.Song;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class Converter {

    public ReturnInfo RunConverter(String spotify_token, String yt_playlist_id, String spotify_username, String spotify_playlist_name) throws GeneralSecurityException, IOException {

        YouTubeClient yt = new YouTubeClient();
        YouTube youtubeService = yt.getService();

        YouTube.Playlists.List request = youtubeService.playlists().list("id, snippet");
        PlaylistListResponse response = request.setMaxResults(50L).setMine(true).execute();
        System.out.println(response);

        String jsonString = response.toString();
        JSONObject obj = new JSONObject(jsonString);
        JSONArray json_array = obj.getJSONArray("items");

        for (int i = 0; i < json_array.length(); i++) {
            System.out.println(json_array.getJSONObject(i).getJSONObject("snippet").getString("title"));
            System.out.println(json_array.getJSONObject(i).getString("id"));
        }

        YouTube.PlaylistItems.List request_for_playlist = youtubeService.playlistItems().list("snippet, contentDetails");
        PlaylistItemListResponse response_for_playlist = request_for_playlist.setPlaylistId(yt_playlist_id).execute();

        String playlist_string = response_for_playlist.toString();
        JSONObject obj2 = new JSONObject(playlist_string);
        JSONArray json_array2 = obj2.getJSONArray("items");

        String song = "";
        List<Song> list_of_songs = new ArrayList<>();
        System.out.println("TRACKS");
        for (int i = 0; i < json_array2.length(); i++) {
            song = json_array2.getJSONObject(i).getJSONObject("snippet").getString("title");
            String[] song_artist = StringUtils.split(song, '-');
            Song track = new Song(song_artist[0], song_artist[1]);
            System.out.println(track);
            list_of_songs.add(track);
        }

        for (int i = 0; i < list_of_songs.size(); i++) {
            System.out.println(list_of_songs.get(i).toString());
        }

        SpotifyClient spotifyClient = new SpotifyClient();
        String playlist_id = spotifyClient.createPlaylist(spotify_token, spotify_username, spotify_playlist_name);

        String[] song_ids = new String[list_of_songs.size()];

        for (int i = 0; i < song_ids.length; i++) {
            String query = list_of_songs.get(i).query();
            System.out.println(query);
            String spotify_id = spotifyClient.searchSongs(query, spotify_token);
            String prefixed_id = "spotify:track:" + spotify_id;
            System.out.println(prefixed_id);
            song_ids[i] = prefixed_id;
        }

        for (int i = 0; i < song_ids.length; i++) {
            System.out.println(song_ids[i]);
        }

        spotifyClient.addItemsToPlaylist(song_ids, playlist_id, spotify_token);

        System.out.println("PLAYLIST ID:" + playlist_id);

        ReturnInfo returnInfo = new ReturnInfo(list_of_songs, playlist_id);

        return returnInfo;
    }
}
