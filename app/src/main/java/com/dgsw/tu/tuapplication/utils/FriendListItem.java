package com.dgsw.tu.tuapplication.utils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dgsw.tu.tuapplication.R;

public class FriendListItem{

    private String friendName;
    private String frendId;
    private String friendProFile;

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String frendName) {
        this.friendName = frendName;
    }

    public String getFrendId() {
        return frendId;
    }

    public void setFrendId(String frendId) {
        this.frendId = frendId;
    }

    public String getFriendProFile() {
        return friendProFile;
    }

    public void setFriendProFile(String frendProFile) {
        this.friendProFile = frendProFile;
    }

    public FriendListItem(String friendName, String friendId, String friendProFile) {
        this.friendName = friendName;
        this.frendId = friendId;
        this.friendProFile = friendProFile;
    }
}
