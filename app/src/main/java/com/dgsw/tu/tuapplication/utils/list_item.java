package com.dgsw.tu.tuapplication.utils;

import java.util.Date;

public class list_item {
    private String profile_image;
    private String proflepath;
    private String write_date;
    private String addr;
    private String content;
    private String lat;
    private int idx;
    private String longi;
    private String username;

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getWrite_date() {
        return write_date;
    }

    public void setWrite_date(String write_date) {
        this.write_date = write_date;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getName(){ return username; }

    public void setName(String name){ this.username = username; }

    public String getProflepath(){ return proflepath; }

    public void setProflepath(String name){ this.proflepath = proflepath; }

    public list_item(String profile_image, String proflepath, String write_date, String addr, String content, String lat, String longi, String username, int idx) {
        this.profile_image = profile_image;
        this.proflepath = proflepath;
        this.write_date = write_date;
        this.addr = addr;
        this.content = content;
        this.lat = lat;
        this.longi = longi;
        this.username = username;
        this.idx = idx;
    }
}
