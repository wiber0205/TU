package com.dgsw.tu.tuapplication.model;

import java.io.Serializable;

public class TimelineItem implements Serializable{
    private static final long serialVersionUID = 1L;

    private int idx;
    private String image;
    private String profileImage;
    private String writeDate;
    private String addr;
    private String content;
    private String lat;
    private String longi;
    private String userid;
    private String username;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public TimelineItem(int idx, String image, String profileImage, String writeDate, String addr, String content, String lat, String longi, String userid, String username) {
        this.idx = idx;
        this.image = image;
        this.profileImage = profileImage;
        this.writeDate = writeDate;
        this.addr = addr;
        this.content = content;
        this.lat = lat;
        this.longi = longi;
        this.userid = userid;
        this.username = username;
    }
}
