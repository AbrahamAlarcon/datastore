package org.abrahamalarcon.datastore.dom.response;

import java.io.Serializable;
import java.util.List;

public class TxtForecast implements Serializable {
    String date;
    List<ForecastDay> forecastday;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ForecastDay> getForecastday() {
        return forecastday;
    }

    public void setForecastday(List<ForecastDay> forecastday) {
        this.forecastday = forecastday;
    }
}
