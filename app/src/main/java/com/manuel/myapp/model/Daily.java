package com.manuel.myapp.model;

import java.util.ArrayList;

/**
 * Created by manuel on 11/17/17.
 */

public class Daily {

    private String summary;
    private String icon;
    private ArrayList<WeatherDay> data;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ArrayList<WeatherDay> getData() {
        return data;
    }

    public void setData(ArrayList<WeatherDay> data) {
        this.data = data;
    }
}
