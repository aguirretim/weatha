package com.timapps.weatha;

public class CurrentWeather {

    /**************************************
     * initialized Variables for Object.  *
     **************************************/

    private String locationLabel;
    private String icon;
    private double temperature;
    private double humidity;
    private double precipChance;
    private String summary;


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


}