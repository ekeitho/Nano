package com.ekeitho.spot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ekeitho.spot.R;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by m652315 on 7/5/15.
 */
public class ArtistSearchAdapter extends ArrayAdapter<Artist> {

    public ArtistSearchAdapter(Context context, ArrayList<Artist> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Artist artist = getItem(position);

        if (convertView == null) {
            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.artist_view, parent, false);
        }

        //TextView textView = (TextView) convertView.findViewById(R.id.artist_name);
        //textView.setText(artist.name);

        return convertView;
    }
}
