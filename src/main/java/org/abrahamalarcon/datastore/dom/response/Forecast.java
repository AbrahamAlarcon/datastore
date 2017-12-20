package org.abrahamalarcon.datastore.dom.response;

import java.io.Serializable;

public class Forecast implements Serializable{
    TxtForecast txt_forecast;

    public TxtForecast getTxt_forecast() {
        return txt_forecast;
    }

    public void setTxt_forecast(TxtForecast txt_forecast) {
        this.txt_forecast = txt_forecast;
    }
}
