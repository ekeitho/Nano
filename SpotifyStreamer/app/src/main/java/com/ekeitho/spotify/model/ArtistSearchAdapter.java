package com.ekeitho.spotify.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekeitho.spotify.R;
import com.ekeitho.spotify.view.Top10;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
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


    public ArtistSearchAdapter(Context context, ArrayList<Artist> users) {
        super(context, 0, users);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the artist
        Artist artist = getItem(position);
        // make sure the view is inflated
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.artist_view, parent, false);
            convertView.setOnClickListener(this);
            convertView.setTag(artist.id);
        }
        // get instances of the views
        ImageView imageView = (ImageView) convertView.findViewById(R.id.artist_image);
        TextView textView = (TextView) convertView.findViewById(R.id.artist_name);
        // populate the views
        textView.setText(artist.name);

        if (artist.images != null && artist.images.size() > 0) {
           Picasso.with(getContext()).load(artist.images.get(0).url).into(imageView);
        }

        return convertView;
    }

    public void onClick(View v) {
        Map<String, Object> map = new HashMap<>();
        map.put("country", "US");

        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();

        spotify.getArtistTopTrack((String)v.getTag(), map, new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, Response response) {
                ArrayList<TopTrack> topTracks = new ArrayList<TopTrack>();

                for(Track t : tracks.tracks) {
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
