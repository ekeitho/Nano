package com.ekeitho.spotify.model;

import android.os.Parcel;
import android.os.Parcelable;

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
