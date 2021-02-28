package com.koral.application.converter;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.koral.application.model.Song;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class YouTubeClient {
    private static final String CLIENT_SECRETS = "src/main/resources/client_secret.json";
    private static final Collection<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/youtube.force-ssl");

    private static final String APPLICATION_NAME = "API code samples";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public static Credential authorize(final NetHttpTransport httpTransport) throws IOException {
        InputStream in = new FileInputStream(CLIENT_SECRETS);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        return credential;
    }

    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(httpTransport);
        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static String getClientSecrets() {
        return CLIENT_SECRETS;
    }

    public static void main(String[] args) throws GeneralSecurityException, IOException, GoogleJsonResponseException {

        YouTube youtubeService = getService();
        // Define and execute the API request
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
        PlaylistItemListResponse response_for_playlist = request_for_playlist.setPlaylistId("PLj8mnSW-fi2Q1zYnxxvuJLg7ygNAS1M3M").execute();

        String playlist_string = response_for_playlist.toString();
        JSONObject obj2 = new JSONObject(playlist_string);
        JSONArray json_array2 = obj2.getJSONArray("items");

        System.out.println("YAYAYAY");
        System.out.println(json_array2.length());

        String song = "";
        List<Song> list_of_songs = new ArrayList<>();
        for (int i = 0; i < json_array2.length(); i++) {
            song = json_array2.getJSONObject(i).getJSONObject("snippet").getString("title");
            String[] song_artist = StringUtils.split(song, '-');
            Song track = new Song(song_artist[0], song_artist[1]);
            list_of_songs.add(track);
        }

        for (int i = 0; i < list_of_songs.size(); i++) {
            System.out.println(list_of_songs.get(i).toString());
        }

    }
}
