package com.ekeitho.spotify.playback;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ekeitho.spotify.R;
import com.ekeitho.spotify.top10.TopTrack;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ekeitho on 8/17/15.
 */
public class SpotifyPlayback extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, SpotifyServiceCallback {


    private static final String ACTION_PLAY = "com.ekeitho.PLAY";
    private static final String ACTION_NEXT = "com.ekeitho.NEXT";
    private static final String ACTION_PREV = "com.ekeitho.PREV";
    private ArrayList<TopTrack> tracks;
    private SpotifyPlaybackService spotifyPlaybackService;
    private ImageView prev, next, playPause, albumArt;
    private TextView artist, album, song;
    private SeekBar seekBar;
    private boolean playing = false;
    boolean mBound = false;
    private int songPosition = 0;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            tracks = getArguments().getParcelableArrayList("com.ekeitho.toptracks");
            songPosition = getArguments().getInt("com.ekeitho.trackpos");
            Log.e("AHH", "" + songPosition);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // populate the layout
        View view = inflater.inflate(R.layout.spotify_playback, container, false);

        artist = (TextView) view.findViewById(R.id.playback_artist);
        album = (TextView) view.findViewById(R.id.playback_album);
        song = (TextView) view.findViewById(R.id.playback_song);
        albumArt = (ImageView) view.findViewById(R.id.playback_album_art);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        seekBar.setMax(30000);

        prev = (ImageView) view.findViewById(R.id.prevButton);
        next = (ImageView) view.findViewById(R.id.nextButton);
        playPause = (ImageView) view.findViewById(R.id.playButton);
        playPause.setOnClickListener(this);
        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);


        if (tracks.get(songPosition) != null) {
            fillViews(songPosition);
        }
        return view;
    }

    private void fillViews(int pos) {
        TopTrack track = tracks.get(pos);
        artist.setText(track.getArtistName());
        album.setText(track.getAlbumName());
        song.setText(track.getTrackName());
        Picasso.with(getActivity()).load(track.getArtThumbnail()).fit().centerCrop().into(albumArt);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), SpotifyPlaybackService.class);
        boolean startFromPause = false;

        switch (v.getId()) {
            case R.id.prevButton:
                if (--songPosition < 0) {
                    songPosition = 9;
                }
                fillViews(songPosition);
                break;
            case R.id.nextButton:
                if (++songPosition > 9) {
                    songPosition = 0;
                }
                fillViews(songPosition);
                break;
            case R.id.playButton:
                if (playing) {
                    playing = false;
                    playPause.setImageResource(android.R.drawable.ic_media_play);
                    spotifyPlaybackService.mMediaPlayer.pause();
                } else {
                    playing = true;
                    // change the image
                    playPause.setImageResource(android.R.drawable.ic_media_pause);

                    if (spotifyPlaybackService != null && spotifyPlaybackService.mMediaPlayer != null) {
                        spotifyPlaybackService.mMediaPlayer.start();
                        startFromPause = true;
                    }
                }
                break;
        }

        intent.setAction(ACTION_PLAY);
        intent.putExtra("com.ekeitho.track", tracks.get(songPosition));
        // if it's not bound and user wants to play it, this is their first time trying to use the mediaplayer
        if (!mBound && playing) {
            //create an intent to play, pass in information for the service,
            // and have the activity start spotify playback service
            getActivity().bindService(intent, mConnection, getActivity().BIND_AUTO_CREATE);
        }
        // service is already bound and not coming back from a pause to start state
        else if (playing && !startFromPause) {
            spotifyPlaybackService.startService(intent);
        }

    }

    @Override
    public void seekUpdater() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                //need this catch, because if music stops playing, there is no more media player
                if (spotifyPlaybackService.mMediaPlayer != null) {
                    seekBar.setProgress(spotifyPlaybackService.mMediaPlayer.getCurrentPosition());
                    if (spotifyPlaybackService.mMediaPlayer.getCurrentPosition() < 30000) {
                        seekBar.postDelayed(this, 1000);
                    }
                } else {
                    // if no more media player since song ended, reset seek bar and playbutton
                    seekBar.setProgress(0);
                    playing = false;
                    playPause.setImageResource(android.R.drawable.ic_media_play);
                }
            }
        };

        run.run();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            spotifyPlaybackService.mMediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            SpotifyPlaybackService.LocalBinder binder = (SpotifyPlaybackService.LocalBinder) service;
            spotifyPlaybackService = binder.getService();
            mBound = true;
            spotifyPlaybackService.setSpotifyServiceCallback(SpotifyPlayback.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


}
