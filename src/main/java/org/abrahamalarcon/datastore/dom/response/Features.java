package org.abrahamalarcon.datastore.dom.response;

import java.io.Serializable;

public class Features implements Serializable {
    int geolookup;
    int conditions;
    int forecast;

    public int getGeolookup() {
        return geolookup;
    }

    public void setGeolookup(int geolookup) {
        this.geolookup = geolookup;
    }

    public int getConditions() {
        return conditions;
    }

    public void setConditions(int conditions) {
        this.conditions = conditions;
    }

    public int getForecast() {
        return forecast;
    }

    public void setForecast(int forecast) {
        this.forecast = forecast;
    }
}
