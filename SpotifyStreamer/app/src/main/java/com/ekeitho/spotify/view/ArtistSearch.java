package com.ekeitho.spotify.view;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.ekeitho.spotify.R;
import com.ekeitho.spotify.model.ArtistSearchAdapter;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class ArtistSearch extends Activity implements SearchView.OnQueryTextListener {

    private ArtistSearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // populate the layout
        setContentView(R.layout.artist_search_view);
        // make sure we have a valid array list

        SearchView searchView = (SearchView) findViewById(R.id.artist_search);
        searchView.setOnQueryTextListener(this);

        // acquire the adapter and add it to the list view
        adapter = new ArtistSearchAdapter(this, new ArrayList<Artist>());
        ListView listView = (ListView) findViewById(R.id.artist_found_list);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.clear();
        new fetchSpotifyArtists().execute(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private class fetchSpotifyArtists extends AsyncTask<String, Void, List<Artist>> {

        @Override
        protected List<Artist> doInBackground(String... params) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager results = spotify.searchArtists(params[0]);
            return results.artists.items;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            ArrayList<Artist> artistArrayList = new ArrayList<>(artists);
            adapter.addAll(artistArrayList);
            super.onPostExecute(artists);
        }
    }

}
