package com.ekeitho.spotify.artist;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by m652315 on 6/21/15.
 */
public class ArtistSearchAdapter extends ArrayAdapter<SpotifyArtist> {

    public ArtistSearchAdapter(Context context, ArrayList<SpotifyArtist> users) {
        super(context, 0, users);
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

}
