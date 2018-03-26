package com.dgsw.tu.tuapplication.utils;

import java.sql.Timestamp;

/**
 * Created by WIBER on 2017-12-17.
 */

public class FriendAddListItem {

    private String friendId;
    private String atAdd;
    private String profileImage;

    public FriendAddListItem(String friendId, String atAdd, String profileImage) {
        this.friendId = friendId;
        this.atAdd = atAdd;
        this.profileImage = profileImage;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getAtAdd() {
        return atAdd;
    }

    public void setAtAdd(String atAdd) {
        this.atAdd = atAdd;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
