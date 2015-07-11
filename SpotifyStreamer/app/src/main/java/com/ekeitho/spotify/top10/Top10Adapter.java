package com.ekeitho.spotify.top10;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by m652315 on 6/30/15.
 */
public class Top10Adapter extends ArrayAdapter<TopTrack> {

    public Top10Adapter(Context context, ArrayList<TopTrack> tracks) {
        super(context, 0, tracks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Top10View top10View = (Top10View) convertView;

        if(top10View == null) {
            top10View = Top10View.inflate(parent);
        }

        top10View.setTrack(getItem(position));

        return top10View;
    }

}