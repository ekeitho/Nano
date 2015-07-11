package com.ekeitho.spotify;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.ekeitho.spotify.artist.ArtistSearchFragment;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;


public class SpotifyActivity extends FragmentActivity {

    private static final String TAG = "SpotifyActivity";
    public SpotifyService spotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // populate empty view
        setContentView(R.layout.activity_spotify);

        if (findViewById(R.id.search_fragment_layout) != null) {
            Log.e(TAG, "normal layout\n");
        } else if (findViewById(R.id.search_fragment_layout_large) != null) {
            Log.e(TAG, "large layout\n");
        } else {
            Log.e(TAG, "uhhhh...");
        }

        if (savedInstanceState == null ) {
            // start up fragment to easily remove and delete it in transactions
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.search_fragment_layout, new ArtistSearchFragment(), "frag")
                    .commit();
        }


        // start spotify service
        SpotifyApi api = new SpotifyApi();
        spotify = api.getService();
    }

}
