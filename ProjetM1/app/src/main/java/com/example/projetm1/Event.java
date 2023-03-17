package com.example.projetm1;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {
    private Date mDate;
    private String mTitle;
    private String mType;
    private int mHour;
    private int mMinute;

    public Event(Date date, String title, String type, int hour, int minute) {
        mDate = date;
        mTitle = title;
        mType = type;
        mHour = hour;
        mMinute = minute;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public int getHour() { // Add getter for hour
        return mHour;
    }

    public void setHour(int hour) { // Add setter for hour
        mHour = hour;
    }

    public int getMinute() { // Add getter for minute
        return mMinute;
    }

    public void setMinute(int minute) { // Add setter for minute
        mMinute = minute;
    }
}
