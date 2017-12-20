package org.abrahamalarcon.datastore.dom.request;

public class DatastoreRequest extends BaseRequest
{
    boolean conditions;
    boolean forecast;
    boolean geolookup;
    String country;
    String city;

    public boolean isConditions() {
        return conditions;
    }

    public void setConditions(boolean conditions) {
        this.conditions = conditions;
    }

    public boolean isForecast() {
        return forecast;
    }

    public void setForecast(boolean forecast) {
        this.forecast = forecast;
    }

    public boolean isGeolookup() {
        return geolookup;
    }

    public void setGeolookup(boolean geolookup) {
        this.geolookup = geolookup;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
