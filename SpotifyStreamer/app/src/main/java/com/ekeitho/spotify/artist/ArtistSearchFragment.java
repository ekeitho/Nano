package com.ekeitho.spotify.artist;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.ekeitho.spotify.R;
import com.ekeitho.spotify.SpotifyActivity;

import java.util.ArrayList;

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
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // populate the layout
        View view = inflater.inflate(R.layout.artist_search_view, container, false);

        ListView listView = (ListView) view.findViewById(R.id.artist_found_list);
        SearchView searchView = (SearchView) view.findViewById(R.id.artist_search);
        searchView.setOnQueryTextListener(this);

        // get activity to use api service
        activity = (SpotifyActivity) getActivity();

        if (artistArray == null) {
            artistArray = new ArrayList<>();
        }

        // acquire the adapter and add it to the list view
        adapter = new ArtistSearchAdapter(getActivity(), artistArray);
        listView.setAdapter(adapter);

        return view;
    }

    private ArrayList<SpotifyArtist> artistArrayToSpotifyArray (ArrayList)


    @Override
    public boolean onQueryTextSubmit(String query) {
        activity.spotify.searchArtists(query, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {
                adapter.clear();
                adapter.addAll(artistsPager.artists.items);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        return false;
    }

}
