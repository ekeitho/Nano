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

    public TopTrack(String track, String album, String art, String preview) {
        this.trackName = track;
        this.albumName = album;
        this.artThumbnail = art;
        this.previewURL = preview;
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

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public void setArtThumbnail(String artThumbnail) {
        this.artThumbnail = artThumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public TopTrack(Parcel in){
        String[] data = new String[3];
        in.readStringArray(data);
        this.trackName = data[0];
        this.albumName = data[1];
        this.artThumbnail = data[2];
        this.previewURL = data[3];
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.trackName,
                this.albumName,
                this.artThumbnail,
                this.previewURL});
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
