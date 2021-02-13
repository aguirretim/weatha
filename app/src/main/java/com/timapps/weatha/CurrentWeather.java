package com.timapps.weatha;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CurrentWeather implements Parcelable {

    /**************************************
     * initialized Variables for Object.  *
     **************************************/

    private String locationLabel;
    private String icon;
    private double temperature;
    private double humidity;
    private double precipChance;
    private String summary;
    private long time;
    private String timeZone;

    /**************************************
     * Constructor for Object.  *
     **************************************/

    public CurrentWeather(String locationLabel,
                          String icon,
                          double temperature,
                          double humidity,
                          double precipChance,
                          String summary,
                          long time,
                          String timeZone) {
        this.locationLabel = locationLabel;
        this.icon = icon;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipChance = precipChance;
        this.summary = summary;
        this.time = time;
        this.timeZone = timeZone;
    }




    /****************************************
     * Methods and Actions that do things  *
     ****************************************/

    public int getIconId(String icon) {

        int iconId = R.drawable.clear_skyd;

        switch (icon) {
            case "01d":
                iconId = R.drawable.clear_skyd;
                break;
            case "01n":
                iconId = R.drawable.clear_skyn;
                break;
            case "02d":
                iconId = R.drawable.few_cloudsd;
                break;
            case "02n":
                iconId = R.drawable.few_cloudsn;
                break;
            case "03d":
                iconId = R.drawable.scattered_cloudsd;
                break;
            case "03n":
                iconId = R.drawable.scattered_cloudsn;
                break;
            case "04d":
                iconId = R.drawable.broken_cloudsd;
                break;
            case "04n":
                iconId = R.drawable.broken_cloudsn;
                break;
            case "09d":
                iconId = R.drawable.shower_raind;
                break;
            case "09n":
                iconId = R.drawable.shower_rainn;
                break;
            case "10d":
                iconId = R.drawable.raind;
                break;
            case "10n":
                iconId = R.drawable.rainn;
                break;
            case "11d":
                iconId = R.drawable.thunderstormd;
                break;
            case "11n":
                iconId = R.drawable.thunderstormnn;
                break;
            case "13d":
                iconId = R.drawable.snowd;
                break;
            case "13n":
                iconId = R.drawable.snown;
                break;
            case "50d":
                iconId = R.drawable.mistd;
                break;
            case "50n":
                iconId = R.drawable.mistn;
                break;
        }
        return iconId;
    }

    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));

        Date dateTime = new Date(time * 1000);
        return formatter.format(dateTime);
    }

    /************************
     * Getters and setters  *
     ************************/

    public String getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        this.locationLabel = locationLabel;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance(double precipChance) {
        this.precipChance = precipChance;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /************************
     * String Method for print Debuging  *
     ************************/

    @Override
    public String toString() {
        return "CurrentWeather{" +
                "locationLabel='" + locationLabel + '\'' +
                ", icon='" + icon + '\'' +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", precipChance=" + precipChance +
                ", summary='" + summary + '\'' +
                ", time=" + time +
                ", timeZone='" + timeZone + '\'' +
                '}';
    }



    protected CurrentWeather(Parcel in) {
        locationLabel = in.readString();
        icon = in.readString();
        temperature = in.readDouble();
        humidity = in.readDouble();
        precipChance = in.readDouble();
        summary = in.readString();
        time = in.readLong();
        timeZone = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(locationLabel);
        dest.writeString(icon);
        dest.writeDouble(temperature);
        dest.writeDouble(humidity);
        dest.writeDouble(precipChance);
        dest.writeString(summary);
        dest.writeLong(time);
        dest.writeString(timeZone);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CurrentWeather> CREATOR = new Parcelable.Creator<CurrentWeather>() {
        @Override
        public CurrentWeather createFromParcel(Parcel in) {
            return new CurrentWeather(in);
        }

        @Override
        public CurrentWeather[] newArray(int size) {
            return new CurrentWeather[size];
        }
    };
}