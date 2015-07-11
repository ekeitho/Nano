package com.ekeitho.spotify.artist;

/**
 * Created by m652315 on 7/9/15.
 */

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by m652315 on 7/2/15.
 */
public class SpotifyArtist implements Parcelable {

    private String artistName;
    private String artistImageUrl;
    private String artistId;

    public SpotifyArtist(String artist, String url, String id) {
        this.artistName = artist;
        this.artistImageUrl = url;
        this.artistId = id;
    }

    public String getArtistImageUrl() {
        return artistImageUrl;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistId() { return artistId;}

    @Override
    public int describeContents() {
        return 0;
    }

    public SpotifyArtist(Parcel in){
        String[] data = new String[3];
        in.readStringArray(data);
        this.artistName = data[0];
        this.artistImageUrl = data[1];
        this.artistId = data[2];
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.artistName, this.artistImageUrl, this.artistId});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public SpotifyArtist createFromParcel(Parcel in) {
            return new SpotifyArtist(in);
        }

        public SpotifyArtist[] newArray(int size) {
            return new SpotifyArtist[size];
        }
    };

}
