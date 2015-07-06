package com.ekeitho.spotify;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;


public class SpotifyActivity extends FragmentActivity {

    public SpotifyService spotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify);

        SpotifyApi api = new SpotifyApi();
        spotify = api.getService();
    }

}
