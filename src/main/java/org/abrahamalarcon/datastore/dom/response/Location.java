package org.abrahamalarcon.datastore.dom.response;

import java.io.Serializable;

public class Location implements Serializable {
    String type;
    String country;
    String country_iso3166;
    String country_name;
    String state;
    String city;
    String tz_short;
    String tz_long;
    String lat;
    String lon;
    String zip;
    String magic;
    String wmo;
    String l;
    String requesturl;
    String wuiurl;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_iso3166() {
        return country_iso3166;
    }

    public void setCountry_iso3166(String country_iso3166) {
        this.country_iso3166 = country_iso3166;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTz_short() {
        return tz_short;
    }

    public void setTz_short(String tz_short) {
        this.tz_short = tz_short;
    }

    public String getTz_long() {
        return tz_long;
    }

    public void setTz_long(String tz_long) {
        this.tz_long = tz_long;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getMagic() {
        return magic;
    }

    public void setMagic(String magic) {
        this.magic = magic;
    }

    public String getWmo() {
        return wmo;
    }

    public void setWmo(String wmo) {
        this.wmo = wmo;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getRequesturl() {
        return requesturl;
    }

    public void setRequesturl(String requesturl) {
        this.requesturl = requesturl;
    }

    public String getWuiurl() {
        return wuiurl;
    }

    public void setWuiurl(String wuiurl) {
        this.wuiurl = wuiurl;
    }
}
