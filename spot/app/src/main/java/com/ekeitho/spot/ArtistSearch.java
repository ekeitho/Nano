package com.ekeitho.spot;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by m652315 on 7/5/15.
 */
public class ArtistSearch extends Activity implements SearchView.OnQueryTextListener {

    ArtistSearchAdapter adapter;
    SpotifyService spotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.artist_search_view);
        ListView listView = (ListView) findViewById(R.id.artist_found_list);
        SearchView searchView = (SearchView) findViewById(R.id.artist_search);
        searchView.setOnQueryTextListener(this);

        adapter = new ArtistSearchAdapter(this, new ArrayList<Artist>());
        listView.setAdapter(adapter);

        SpotifyApi api = new SpotifyApi();
        spotify = api.getService();
        Log.d("poop", "" + Thread.currentThread().getId());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        spotify.searchArtists(query, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {
                Log.d("poop2", ""+Thread.currentThread().getId());
                adapter.clear();
                //adapter.addAll(artistsPager.artists.items);

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
