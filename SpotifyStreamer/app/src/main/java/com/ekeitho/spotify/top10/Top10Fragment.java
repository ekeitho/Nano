package com.ekeitho.spotify.top10;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ekeitho.spotify.R;
import com.ekeitho.spotify.SpotifyActivity;

import java.util.ArrayList;

/**
 * Created by m652315 on 6/30/15.
 */
public class Top10Fragment extends Fragment {

    private static final String TAG = Top10Fragment.class.getSimpleName();
    private ArrayList<TopTrack> tracks;
    private String title = null;
    private Top10Adapter adapter;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(getString(R.string.top_tracks_save), tracks);
        outState.putString(getString(R.string.artist_title_save), title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // on rotation keep array and title persistent
        if (savedInstanceState != null) {
            tracks = savedInstanceState.getParcelableArrayList(getString(R.string.top_tracks_save));
            title = savedInstanceState.getString(
                    getString(R.string.artist_title_save), // grab this, if not choose default
                    getString(R.string.title_activity_spotify));
            getActivity().setTitle(title);
        }
        // on first creation grab, the title & the arguments passed to the fragment
        else {
            title = getActivity().getTitle().toString();
            tracks = getArguments().getParcelableArrayList(getString(R.string.tracks_fragment_transfer));
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // populate the layout
        View view = inflater.inflate(R.layout.top10_view, container, false);
        ListView listView = (ListView) view.findViewById(R.id.top10_song_found_list);

        // acquire the adapter and add it to the list view
        adapter = new Top10Adapter(getActivity(), tracks);
        listView.setAdapter(adapter);

        return view;
    }

}
