package com.example.projet2;

import java.io.Serializable;

public class Event implements Serializable {
    private String title;
    private String date;
    private String time;
    private int coefficient;
    private String type;

    public Event(String title, String date, String time, int coefficient, String type) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.coefficient = coefficient;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}