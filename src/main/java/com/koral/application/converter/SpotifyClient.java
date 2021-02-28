package com.koral.application.converter;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.special.SnapshotResult;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import com.wrapper.spotify.requests.data.playlists.CreatePlaylistRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class SpotifyClient {

    //Variables for create method
   // private static final String lol = "BQD02-q99XCLZ-onIBiISs41emvJSmYYdumvmEAFXEzV4VSzOTi-ypYY6Dlzsrpp-aO756JoK-8pwRi5oipr7PYBq7sXajmOgUk2XBqYWzF2d2voK53Bam9dwVxhvObOtM_pn3mLaB3GI59vc92NAlAknf1ZdsLaI0AuyIcHHpf-rSBkTwRGYO2zlzkhZd3YGA";
   // private static final String accessToken = lol;
    //private static final String userId = "korlz";
    //private static final String name = "Baba";



    public static String createPlaylist(String accessToken, String userId, String name) {
        String playlist_id = null;
        try {
            final SpotifyApi spotifyApi = new SpotifyApi.Builder().setAccessToken(accessToken).build();
            final CreatePlaylistRequest createPlaylistRequest = spotifyApi.createPlaylist(userId, name).build();
            final Playlist playlist = createPlaylistRequest.execute();
            playlist_id = playlist.getId();
            System.out.println(playlist_id);

            System.out.println("Name: " + playlist.getName());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return playlist_id;
    }

    public static String searchSongs(String query, String accessToken) {
        Paging<Track> trackPaging = null;
        try {
            SpotifyApi spotifyApi = new SpotifyApi.Builder().setAccessToken(accessToken).build();
            SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(query).build();
            CompletableFuture<Paging<Track>> pagingFuture = searchTracksRequest.executeAsync();

            trackPaging = pagingFuture.join();


        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
        return trackPaging.getItems()[0].getId();
    }

    public static void addItemsToPlaylist(String[] uris, String playlistId, String accessToken) {
        try {
            final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                    .setAccessToken(accessToken)
                    .build();
            final AddItemsToPlaylistRequest addItemsToPlaylistRequest = spotifyApi
                    .addItemsToPlaylist(playlistId, uris)
                    .build();
            final SnapshotResult snapshotResult = addItemsToPlaylistRequest.execute();

            System.out.println("Snapshot ID: " + snapshotResult.getSnapshotId());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}