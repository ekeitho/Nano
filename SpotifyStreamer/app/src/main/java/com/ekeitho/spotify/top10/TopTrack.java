package com.ekeitho.spotify.top10;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by m652315 on 7/2/15.
 */
public class TopTrack implements Parcelable {

    private String trackName;
    private String albumName;
    private String artThumbnail;
    private String previewURL;
    private String artistName;
    private String trackDuration;

    public TopTrack(String track, String album, String art, String preview, String artist, String duration) {
        this.trackName = track;
        this.albumName = album;
        this.artThumbnail = art;
        this.previewURL = preview;
        this.artistName = artist;
        this.trackDuration = duration;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getArtThumbnail() {
        return artThumbnail;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public String getTrackName() {
        return trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getTrackDuration() {
        return trackDuration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public TopTrack(Parcel in){
        String[] data = new String[6];
        in.readStringArray(data);
        this.trackName = data[0];
        this.albumName = data[1];
        this.artThumbnail = data[2];
        this.previewURL = data[3];
        this.artistName = data[4];
        this.trackDuration = data[5];
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.trackName,
                this.albumName,
                this.artThumbnail,
                this.previewURL,
                this.artistName,
                this.trackDuration});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public TopTrack createFromParcel(Parcel in) {
            return new TopTrack(in);
        }

        public TopTrack[] newArray(int size) {
            return new TopTrack[size];
        }
    };

}
