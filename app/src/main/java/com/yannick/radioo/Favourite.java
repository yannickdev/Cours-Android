package com.yannick.radioo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Favourite implements Parcelable {

    private String name;
    private String stationuuid;
    private String country;
    private String countrycode;
    private String state;
    private String url;
    private String favicon;
    private int votes;
    private String codec;
    //et date de creation du favori


//    @SerializedName("tags")
//    private List<String> tags;


    public Favourite() {
    }

    public Favourite(Station station) {
        this.setCodec(station.getCodec());
        this.setName(station.getName());
        this.setCountry(station.getCountry());
        this.setCountrycode(station.getCountrycode());
        this.setFavicon(station.getFavicon());
        this.setState(station.getState());
        this.setStationuuid(station.getStationuuid());
        this.setUrl(station.getUrl());
        this.setVotes(station.getVotes());
    }
    protected Favourite(Parcel in) {
        name = in.readString();
        stationuuid = in.readString();
        country = in.readString();
        countrycode = in.readString();
        state = in.readString();
        url = in.readString();
        favicon = in.readString();
        votes=in.readInt();
        codec = in.readString();
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };



    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param in  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel in, int flags) {
        in.writeString(name);
        in.writeString(stationuuid);
        in.writeString(country);
        in.writeString(countrycode);
        in.writeString(state);
        in.writeString(url);
        in.writeString(favicon);
        in.writeInt(votes);
        in.writeString(codec);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStationuuid() {
        return stationuuid;
    }

    public void setStationuuid(String stationuuid) {
        this.stationuuid = stationuuid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

   /* public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }*/
}
