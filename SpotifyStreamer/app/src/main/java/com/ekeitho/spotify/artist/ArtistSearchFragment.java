package com.ekeitho.spotify.artist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.ekeitho.spotify.R;
import com.ekeitho.spotify.SpotifyActivity;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ArtistSearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private ArtistSearchAdapter adapter;
    private SpotifyActivity activity;
    private ArrayList<SpotifyArtist> artistArray;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("com.ekeitho.artist_array", artistArray);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artist_search_view, container, false);;
        // get activity to use api service
        activity = (SpotifyActivity) getActivity();

        // if on first created
        if (savedInstanceState == null && artistArray == null) {
            // populate the layout
            artistArray = new ArrayList<>();
        }
        // if on rotated
        else if (savedInstanceState != null && artistArray == null) {
            artistArray = savedInstanceState.getParcelableArrayList("com.ekeitho.artist_array");
        }

        ListView listView = (ListView) view.findViewById(R.id.artist_found_list);
        SearchView searchView = (SearchView) view.findViewById(R.id.artist_search);
        searchView.setOnQueryTextListener(this);

        // acquire the adapter and add it to the list view
        adapter = new ArtistSearchAdapter(getActivity(), artistArray);
        listView.setAdapter(adapter);
        activity.setTitle("SpotifyActivity");

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        activity.spotify.searchArtists(query, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {
                adapter.clear();
                adapter.addAll(artistArrayToSpotifyArray(artistsPager.artists.items));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String newText) { return false; }

    /* additional helper methods below */

    private ArrayList<SpotifyArtist> artistArrayToSpotifyArray(List<Artist> artists) {
        ArrayList<SpotifyArtist> spotifyArtists = new ArrayList<>();

        for (Artist artist : artists) {
            spotifyArtists.add(transform(artist));
        }
        return spotifyArtists;
    }

    private SpotifyArtist transform(Artist artist) {
        String url = null;

        if (artist.images != null & artist.images.size() > 0) {
            url = artist.images.get(0).url;
        }
        return new SpotifyArtist(artist.name, url, artist.id);
    }

}
