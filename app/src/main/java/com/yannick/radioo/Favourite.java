package com.yannick.radioo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Favourite extends Station implements Parcelable {

    private Date creationDate;
    private Integer order;


    public Favourite() {
        super();
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
        super(in);
        this.setCreationDate(((java.util.Date) in.readSerializable()));
        this.setOrder(in.readInt());
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Favourite createFromParcel(Parcel in) {
            return new Favourite(in);
        }

        @Override
        public Favourite[] newArray(int size) {
            return new Favourite[size];
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
        super.writeToParcel(in,flags);
        in.writeSerializable(this.getCreationDate());
        in.writeInt(this.getOrder());

    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Integer getOrder() {
        return order;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    /* public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }*/
}
