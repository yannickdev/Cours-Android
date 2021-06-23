package com.yannick.radioo;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;

import java.io.Serializable;


public class ScheduledStation extends Station  {


    private String startDate;
    private String endDate;
    private SchedulerStatus status;

    //ISO8601 format : yyyy-MM-dd'T'HH:mm:ssZ,

    public ScheduledStation(String startDate, String endDate, SchedulerStatus status) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public ScheduledStation() {
        super();
    }

    public ScheduledStation(Station station) {
        super(station);
    }

    public ScheduledStation(Parcel in) {
        super(in);
        startDate = in.readString();
                //(org.joda.time.DateTime)in.readSerializable();
        endDate = in.readString();
                //= (org.joda.time.DateTime)in.readSerializable();
        status = (SchedulerStatus)in.readSerializable();

    }

    public static final Creator<ScheduledStation> CREATOR = new Creator<ScheduledStation>() {
        @Override
        public ScheduledStation createFromParcel(Parcel in) {
            return new ScheduledStation(in);
        }

        @Override
        public ScheduledStation[] newArray(int size) {
            return new ScheduledStation[size];
        }
    };

    @Override
    public void writeToParcel(Parcel in, int flags) {
        super.writeToParcel(in,flags);
        in.writeSerializable(startDate);
        in.writeSerializable(endDate);
        in.writeSerializable(status);
    }

    public int getDuration(){
        DateTime start = DateTime.parse(startDate, DateTimeFormat.forPattern("dd-MM-yyyy"));
        DateTime end = DateTime.parse(endDate, DateTimeFormat.forPattern("dd-MM-yyyy"));
        return new Period(start, end).getMillis();
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public SchedulerStatus getStatus() {
        return status;
    }

    public void setStatus(SchedulerStatus status) {
        this.status = status;
    }
}
