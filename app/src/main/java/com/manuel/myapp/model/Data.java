package com.manuel.myapp.model;

import java.util.ArrayList;

/**
 * Created by manuel on 11/16/17.
 */

public class Data {

    private String timezone;
    private WeatherDay currently;

    public Daily getDaily() {
        return daily;
    }

    public void setDaily(Daily daily) {
        this.daily = daily;
    }

    private Daily daily;


    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public WeatherDay getCurrently() {
        return currently;
    }

    public void setCurrently(WeatherDay currently) {
        this.currently = currently;
    }
}
