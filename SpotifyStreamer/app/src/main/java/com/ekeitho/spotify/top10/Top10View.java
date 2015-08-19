package com.ekeitho.spotify.top10;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekeitho.spotify.R;
import com.squareup.picasso.Picasso;

/**
 * Created by m652315 on 7/7/15.
 */
public class Top10View extends LinearLayout {

    private ImageView albumImage;
    private LinearLayout albumTextLayout;
    private TextView albumName;
    private TextView albumSong;
    private TopTrack track;
    //start count track at 0
    private int trackCount = 0;


    public TopTrack getTrack() {
        return this.track;
    }
    public int getTrackCount() { return this.trackCount;};
    public void setTrackCount(int pos) { this.trackCount = pos;}

    public Top10View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.top10_song_view_children, this, true);
        setupChildren();
    }

    public Top10View(Context context) {
        this(context, null);
    }

    public Top10View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public static Top10View inflate(ViewGroup parent) {
        Top10View top10View = (Top10View)
                LayoutInflater.from(parent.getContext()).inflate(R.layout.top10_song_view, parent, false);
        return top10View;
    }

    private void setupChildren() {
        albumImage = (ImageView) findViewById(R.id.top10_album_view);
        albumTextLayout = (LinearLayout) findViewById(R.id.top10_text_layout);
        albumName = (TextView) findViewById(R.id.top10_album_name);
        albumSong = (TextView) findViewById(R.id.top10_song_name);
    }

    public void setTrack(TopTrack track) {
        albumName.setText(track.getAlbumName());
        albumSong.setText(track.getTrackName());
        this.track = track;

        Picasso.with(getContext()).load(track.getArtThumbnail()).fit().centerCrop().into(albumImage);
    }
}
