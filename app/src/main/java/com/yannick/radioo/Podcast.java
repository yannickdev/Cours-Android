package com.yannick.radioo;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Podcast implements Parcelable {
    private int id;
    private String title;
    private Station station;
    private String date;
    private String fileUrl;
    private String faviconUrl;
    private long duration;


    public Podcast() {
    }

    public Podcast(int id, String title, Station station, String date, String fileUrl, String faviconUrl, long duration) {
        this.id = id;
        this.title = title;
        this.station = station;
        this.date = date;
        this.fileUrl = fileUrl;
        this.faviconUrl = faviconUrl;
        this.duration = duration;
    }

    protected Podcast(Parcel in) {
        id = in.readInt();
        title = in.readString();
        station = in.readParcelable(Station.class.getClassLoader());
        date = in.readString();
        fileUrl = in.readString();
        faviconUrl = in.readString();
        duration = in.readInt();
    }

    public static final Creator<Podcast> CREATOR = new Creator<Podcast>() {
        @Override
        public Podcast createFromParcel(Parcel in) {
            return new Podcast(in);
        }

        @Override
        public Podcast[] newArray(int size) {
            return new Podcast[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFaviconUrl() {
        return faviconUrl;
    }

    public void setFaviconUrl(String faviconUrl) {
        this.faviconUrl = faviconUrl;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

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
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeParcelable(station, flags);
        dest.writeString(date);
        dest.writeString(fileUrl);
        dest.writeString(faviconUrl);
        dest.writeLong(duration);
    }

    @Override
    public String toString() {
        return "Podcast{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", station=" + station +
                ", date='" + date + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", faviconUrl='" + faviconUrl + '\'' +
                ", duration=" + duration +
                '}';
    }
}
