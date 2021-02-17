package com.timapps.weatha;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CurrentWeather implements Parcelable {

    /**************************************
     * initialized Variables for Object.  *
     **************************************/

    private String locationLabel;
    private String icon;
    private double temperature;
    private double feelsLikeTemp;
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
                          double feelsLikeTemp,
                          double humidity,
                          double precipChance,
                          String summary,
                          long time,
                          String timeZone) {
        this.locationLabel = locationLabel;
        this.icon = icon;
        this.temperature = temperature;
        this.feelsLikeTemp = feelsLikeTemp;
        this.humidity = humidity;
        this.precipChance = precipChance;
        this.summary = summary;
        this.time = time;
        this.timeZone = timeZone;
        ePochTimeConverter(time);

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

    public double getFeelsLikeTemp() {
        return feelsLikeTemp;
    }

    public void setFeelsLikeTemp(double feelsLikeTemp) {
        this.feelsLikeTemp = feelsLikeTemp;
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

    public Calendar ePochTimeConverter(long pTime) {
        //You could start with a string representation of epoch
        long epoch = pTime;
        //System.out.println("Convert Epoch " + epoch + " to date: ");
        Date d = new Date(epoch * 1000); //convert epoch seconds to microseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //Here you say to java the initial timezone. This is the secret
        // sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        cal.get(cal.HOUR_OF_DAY);
        //cal.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()); // Thu


        System.out.println(cal.get(cal.AM_PM) + "");


        return cal;
    }

    public String amPmFinder(int amPmFieldNumber) {

        String amPm;

        switch (amPmFieldNumber) {
            case 0:
                amPm = "AM";
                break;
            case 1:
                amPm = "PM";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + amPmFieldNumber);
        }
        return amPm;

    }

    public int midNoonConverter(int pTime) {
        int Time = pTime;

        switch (pTime) {
            case 0:
                Time = 12;
                break;
            case 13:
                Time = 1;
                break;
            case 14:
                Time = 2;
                break;
            case 15:
                Time = 3;
                break;
            case 16:
                Time = 4;
                break;
            case 17:
                Time = 5;
                break;
            case 18:
                Time = 6;
                break;
            case 19:
                Time = 7;
                break;
            case 20:
                Time = 8;
                break;
            case 21:
                Time = 9;
                break;
            case 22:
                Time = 10;
                break;
            case 23:
                Time = 11;
                break;

        }
        return Time;
    }

    /************************
     * Makes object Parcelable methods so it can be passed in bundle*
     ************************/

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