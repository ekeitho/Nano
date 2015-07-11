package com.ekeitho.spotify.artist;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ekeitho.spotify.R;
import com.ekeitho.spotify.SpotifyActivity;
import com.ekeitho.spotify.top10.TopTrack;
import com.ekeitho.spotify.top10.Top10Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by m652315 on 6/21/15.
 */
public class ArtistSearchAdapter extends ArrayAdapter<SpotifyArtist> implements View.OnClickListener {

    private SpotifyActivity activity;
    private ArtistSearchAdapter adapter = this;
    private FragmentManager manager;
    private ArtistView artistView;

    public ArtistSearchAdapter(Context context, ArrayList<SpotifyArtist> users) {
        super(context, 0, users);
        // attach to activity
        activity = (SpotifyActivity)getContext();
        manager = activity.getSupportFragmentManager();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the artist
        artistView = (ArtistView) convertView;

        if (artistView == null) {
            artistView = ArtistView.inflate(parent);
        }

        artistView.setOnClickListener(this);

        artistView.setArtist(getItem(position));

        return artistView;
    }

    public TopTrack trackToTopTrack(Track track) {
        String url = null;

        if (track.album.images != null && track.album.images.size() > 0) {
            url = track.album.images.get(0).url;
        }
        else {
            Log.e("SpotifyArtist", "didn't get this song album image: " + track.name);
        }
        return new TopTrack(track.name, track.album.name, url, track.preview_url);
    }

    public void onClick(View v) {
        Map<String, Object> map = new HashMap<>();
        map.put("country", "US");
        final ArtistView view = (ArtistView) v;

        activity.spotify.getArtistTopTrack(view.getArtistId(), map, new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, Response response) {
                ArrayList<TopTrack> topTracks = new ArrayList<TopTrack>();


                for (Track track : tracks.tracks) {
                    // transform to my toptrack object and add to array list
                    topTracks.add(adapter.trackToTopTrack(track));
                }

                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                Fragment fragment = new Top10Fragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("com.ekeitho.top10tracks", topTracks);
                fragment.setArguments(bundle);
                fragmentTransaction.hide(manager.findFragmentById(R.id.artist_search_fragment));
                fragmentTransaction.add(android.R.id.content, fragment, "MyStringIdentifierTag");
                fragmentTransaction.addToBackStack(null).commit();
                // sets the title after transaction
                activity.setTitle("Top 10 Tracks - " + view.getArtistName());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("adapter", "fail");
            }
        });
    }
}
