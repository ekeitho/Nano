package com.ekeitho.spotify.playback;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.ekeitho.spotify.SpotifyActivity;
import com.ekeitho.spotify.top10.TopTrack;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ekeitho on 8/11/15.
 */
public class SpotifyPlaybackService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    MediaPlayer mMediaPlayer;
    private static final String ACTION_PLAY = "com.ekeitho.PLAY";
    private static final String TAG = SpotifyPlaybackService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 3;
    private TopTrack track;
    private int songseek = -1;
    private SpotifyServiceCallback spotifyServiceCallback;

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "on bind success");
        return mBinder;
    }

    public void setSongseek(int seek) {
        this.songseek = seek;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ACTION_PLAY)) {
            this.track = intent.getParcelableExtra("com.ekeitho.track");
            initMediaPlayer(track.getPreviewURL());


            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                    
                    Notification.Builder mBuilder = new Notification.Builder(getApplicationContext())
                            .setLargeIcon(bitmap);
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            Picasso.with(this).load(track.getArtThumbnail()).into(target);
        } else {
            Log.v(TAG, "Wasn't action play..");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void initMediaPlayer(String playbackurl) {
        // this is called when there is a next/prev called when a song is playing
        cleanUp();
        // ...initialize the MediaPlayer here...
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mMediaPlayer.setDataSource(playbackurl);
        } catch(IllegalArgumentException e) {
            Log.e(TAG, "Illegal argument: " + e);
        } catch(IOException e) {
            Log.e(TAG, "IOException: " + e);
        }

        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.prepareAsync();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // assign the song name to songName
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), SpotifyActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        // create the notification
        Notification.Builder mBuilder = new Notification.Builder(this)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle(this.track.getArtistName())
                .setContentText(this.track.getTrackName())
                .setContentIntent(pendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        // start the player
        if (songseek != -1) {
            mMediaPlayer.seekTo(songseek);
            //reset songseek
            songseek = -1;
        }
        mMediaPlayer.start();


        // calls the seek updater from spotify playback to start updating the progress
        if (spotifyServiceCallback != null) {
            spotifyServiceCallback.seekUpdater();
        }

        // start up the notification
        startForeground(NOTIFICATION_ID, mBuilder.build());
    }

    //True if the method handled the error, false if it didn't. Returning false,
    //or not having an OnErrorListener at all, will cause the OnCompletionListener to be called.
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // ... react appropriately ...
        // The MediaPlayer has moved to the Error state, must be reset!
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_IO:
                Log.e(TAG, "File or network related error.");
                return true;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                Log.e(TAG, "Operation took too long.");
                return true;
            default:
                Log.e(TAG, "Something else gave an error with code: " + what + " and extra: " + extra);
                return false;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        cleanUp();
    }

    @Override
    public void onDestroy() {
        cleanUp();
    }

    public void cleanUp() {
        // goodbye media player
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            stopForeground(true);
            songseek = -1;
        }
    }

    public void setSpotifyServiceCallback(SpotifyServiceCallback callback) {
        this.spotifyServiceCallback = callback;
    }


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        SpotifyPlaybackService getService() {
            // Return this instance of LocalService so clients can call public methods
            return SpotifyPlaybackService.this;
        }
    }
}
