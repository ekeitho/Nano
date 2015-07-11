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

    private ArrayList<TopTrack> tracks;
    private String title = null;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("com.ekeitho.top10tracks", tracks);
        outState.putString("com.ekeitho.title", title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // on rotation keep array and title persistent
        if (savedInstanceState != null) {
            tracks = savedInstanceState.getParcelableArrayList("com.ekeitho.top10tracks");
            title = savedInstanceState.getString("com.ekeitho.title", "SpotifyActivity");
            getActivity().setTitle(title);
        }
        // on first creation grab, the title
        else {
            title = getActivity().getTitle().toString();
        }
        super.onCreate(savedInstanceState);
    }

    /* the data is passed through from artistView, when one of artist views are clicked */
    @Override
    public void setArguments(Bundle args) {
        tracks = args.getParcelableArrayList("com.ekeitho.top10tracks");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // populate the layout
        View view = inflater.inflate(R.layout.top10_view, container, false);
        ListView listView = (ListView) view.findViewById(R.id.top10_song_found_list);

        // acquire the adapter and add it to the list view
        Top10Adapter adapter = new Top10Adapter(getActivity(), tracks);
        listView.setAdapter(adapter);

        return view;
    }
}
