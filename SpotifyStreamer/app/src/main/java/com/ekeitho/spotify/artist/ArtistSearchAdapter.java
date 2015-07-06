package com.ekeitho.spotify.artist;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ekeitho.spotify.SpotifyActivity;
import com.ekeitho.spotify.top10.TopTrack;
import com.ekeitho.spotify.top10.Top10;

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
public class ArtistSearchAdapter extends ArrayAdapter<Artist> implements View.OnClickListener {

    private SpotifyActivity activity;

    public ArtistSearchAdapter(Context context, ArrayList<Artist> users) {
        super(context, 0, users);
        // attach to activity
        activity = (SpotifyActivity)getContext();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the artist
        ArtistView artistView = (ArtistView) convertView;

        if (artistView == null) {
            artistView = ArtistView.inflate(parent);
        }

        artistView.setArtist(getItem(position));

        return artistView;
    }

    public void onClick(View v) {
        Map<String, Object> map = new HashMap<>();
        map.put("country", "US");

        activity.spotify.getArtistTopTrack((String) v.getTag(), map, new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, Response response) {
                ArrayList<TopTrack> topTracks = new ArrayList<TopTrack>();

                for (Track t : tracks.tracks) {
                    String url = "";

                    if (t.album.images != null && t.album.images.size() > 0) {
                        url = t.album.images.get(0).url;
                    }

                    TopTrack track = new TopTrack(t.name, t.album.name, url, t.preview_url);
                    topTracks.add(track);
                }


                Intent intent = new Intent(getContext(), Top10.class);
                intent.putParcelableArrayListExtra("top_track_10", topTracks);

                Log.e("adapter", "success with " + tracks.tracks.size() + " tracks");
                Log.e("adapter", "first track is " + tracks.tracks.get(0).name);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("adapter", "fail");
            }
        });
    }
}
