package com.ekeitho.spotify.artist;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekeitho.spotify.R;
import com.ekeitho.spotify.SpotifyActivity;
import com.ekeitho.spotify.top10.Top10Fragment;
import com.ekeitho.spotify.top10.TopTrack;
import com.squareup.picasso.Picasso;

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
 * Created by m652315 on 7/5/15.
 */
public class ArtistView extends LinearLayout implements View.OnClickListener {

    private TextView artistName;
    private ImageView albumArt;
    private String artistId;
    private SpotifyActivity activity;
    private FragmentManager manager;

    public ArtistView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.artist_view_children, this, true);

        // grab an instance of activity to call api and get fragments
        activity = (SpotifyActivity) context;
        manager = activity.getSupportFragmentManager();
        setupChildren();
    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName.getText().toString();
    }

    public ArtistView(Context context) {
        this(context, null);
    }

    public ArtistView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public static ArtistView inflate(ViewGroup parent) {
        ArtistView artistView = (ArtistView)
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_view, parent, false);
        return artistView;
    }

    private void setupChildren() {
        artistName = (TextView) findViewById(R.id.artist_name);
        albumArt = (ImageView) findViewById(R.id.artist_image);
        setOnClickListener(this);
    }

    public void setArtist(SpotifyArtist artist) {
        // populate the views
        artistName.setText(artist.getArtistName());


        if (artist.getArtistImageUrl() != null) {
            Picasso.with(getContext()).load(artist.getArtistImageUrl()).centerCrop().fit().into(albumArt);
        }

        artistId = artist.getArtistId();
    }

    @Override
    public void onClick(View v) {
        Map<String, Object> map = new HashMap<>();
        map.put("country", "US");
        final ArtistView view = (ArtistView) v;

        activity.spotify.getArtistTopTrack(view.getArtistId(), map, new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, Response response) {
                startUpTop10Fragment(tracks, view);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("adapter", "fail");
            }
        });
    }

    private void startUpTop10Fragment(Tracks tracks, ArtistView view) {
        ArrayList<TopTrack> topTracks = new ArrayList<TopTrack>();

        // iterate through the tracks
        for (Track track : tracks.tracks) {
            // transform to my toptrack object and add to array list
            topTracks.add(this.trackToTopTrack(track));
        }

        Top10Fragment top10Fragment = new Top10Fragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("com.ekeitho.top10tracks", topTracks);
        top10Fragment.setArguments(bundle);

        activity.getSupportFragmentManager().beginTransaction()
                .remove(manager.findFragmentById(R.id.search_fragment_layout))
                .add(R.id.search_fragment_layout, top10Fragment, "toptrackfrag")
                .addToBackStack(null)
                .commit();

        // sets the title after transaction
        activity.setTitle("Top 10 Tracks - " + view.getArtistName());
    }

    private TopTrack trackToTopTrack(Track track) {
        String url = null;

        if (track.album.images != null && track.album.images.size() > 0) {
            url = track.album.images.get(0).url;
        }
        else {
            Log.e("SpotifyArtist", "didn't get this song album image: " + track.name);
        }
        return new TopTrack(track.name, track.album.name, url, track.preview_url);
    }
}
