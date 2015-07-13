package com.ekeitho.spotify;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ekeitho.spotify.artist.ArtistSearchFragment;
import com.ekeitho.spotify.artist.ArtistView;
import com.ekeitho.spotify.top10.Top10Fragment;
import com.ekeitho.spotify.top10.TopTrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SpotifyActivity extends FragmentActivity {

    private static final String TAG = "SpotifyActivity";
    public SpotifyService spotify;
    private boolean dualPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // populate empty view
        setContentView(R.layout.activity_spotify);

        if (findViewById(R.id.top10_fragment_layout) == null) {
            Log.e(TAG, "normal layout\n");
        } else {
            dualPane = true;
            Log.e(TAG, "uhhhh...");
        }

        if (savedInstanceState == null) {
            // start up fragment to easily remove and delete it in transactions
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.search_fragment_layout, new ArtistSearchFragment(), "frag");
            transaction.commit();
        }


        // start spotify service
        SpotifyApi api = new SpotifyApi();
        spotify = api.getService();
    }

    // this method is attached from the XML
    public void getTopTracks(View v) {
        // need to have this for the api call
        Map<String, Object> map = new HashMap<>();
        map.put("country", "US");
        // casting looks bleh, but need to get id and name from the view
        final ArtistView view = (ArtistView) v;

        spotify.getArtistTopTrack(view.getArtistId(), map, new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, Response response) {

                // parceficy the tracks response
                ArrayList<TopTrack> topTracks = trackToTopTracks(tracks);

                // initialize top10 fragment with the topTracks
                Top10Fragment top10Fragment = new Top10Fragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(getString(R.string.tracks_fragment_transfer), topTracks);
                top10Fragment.setArguments(bundle);

                // remove artist search in replace with top 10 fragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (!dualPane) {
                    transaction.remove(getSupportFragmentManager().findFragmentById(R.id.search_fragment_layout));
                    transaction.add(R.id.search_fragment_layout, top10Fragment, "toptrackfrag");
                } else {
                    transaction.add(R.id.top10_fragment_layout, top10Fragment, "toptrackfrag");
                }
                transaction.addToBackStack(null).commit();

                // sets the title after transaction
                setTitle(getString(R.string.top_tracks_title) + view.getArtistName());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getBaseContext(), "No internet, try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // helper method to turn into a parceable array
    private ArrayList<TopTrack> trackToTopTracks(Tracks tracks) {
        ArrayList<TopTrack> topTracks = new ArrayList<>();

        for (Track track : tracks.tracks) {
            String url = null;

            if (track.album.images != null && track.album.images.size() > 0) {
                url = track.album.images.get(0).url;
            } else {
                Log.e("SpotifyArtist", "didn't get this song album image: " + track.name);
            }
            topTracks.add(new TopTrack(track.name, track.album.name, url, track.preview_url));
        }
        return topTracks;
    }

}
