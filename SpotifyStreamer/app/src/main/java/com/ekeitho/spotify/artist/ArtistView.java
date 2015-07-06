package com.ekeitho.spotify.artist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekeitho.spotify.R;
import com.squareup.picasso.Picasso;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by m652315 on 7/5/15.
 */
public class ArtistView extends LinearLayout {

    private TextView artistName;
    private ImageView albumArt;

    public ArtistView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.artist_view_children, this, true);
        setupChildren();
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

    public void setArtist(Artist artist) {
        // populate the views
        artistName.setText(artist.name);

        if (artist.images != null && artist.images.size() > 0) {
            Picasso.with(getContext()).load(artist.images.get(0).url).centerCrop().fit().into(albumArt);
        }
    }
}
