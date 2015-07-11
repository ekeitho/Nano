package com.ekeitho.spotify.top10;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.ekeitho.spotify.R;
import com.ekeitho.spotify.SpotifyActivity;
import com.ekeitho.spotify.artist.ArtistSearchAdapter;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by m652315 on 6/30/15.
 */
public class Top10Fragment extends Fragment {


    private SpotifyActivity activity;
    private Top10Adapter adapter;
    private ArrayList<TopTrack> tracks;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("com.ekeitho.top10tracks", tracks);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            tracks = savedInstanceState.getParcelableArrayList("com.ekeitho.top10tracks");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setArguments(Bundle args) {
        tracks = args.getParcelableArrayList("com.ekeitho.top10tracks");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // populate the layout
        View view = inflater.inflate(R.layout.top10_view, container, false);

        ListView listView = (ListView) view.findViewById(R.id.top10_song_found);

        // get activity to use api service
        activity = (SpotifyActivity) getActivity();

        // acquire the adapter and add it to the list view
        adapter = new Top10Adapter(getActivity(), tracks);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroy() {
        activity.setTitle("Spotify Activity");
        super.onDestroy();
    }
}
