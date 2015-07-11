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
public class ArtistView extends LinearLayout {

    private TextView artistName;
    private ImageView albumArt;
    private String artistId;

    public ArtistView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.artist_view_children, this, true);
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
    }

    public void setArtist(SpotifyArtist artist) {
        // populate the views
        artistName.setText(artist.getArtistName());

        if (artist.getArtistImageUrl() != null) {
            Picasso.with(getContext()).load(artist.getArtistImageUrl()).centerCrop().fit().into(albumArt);
        }

        artistId = artist.getArtistId();
    }

}
