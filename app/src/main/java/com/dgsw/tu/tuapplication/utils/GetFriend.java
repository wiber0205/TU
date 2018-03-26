package com.dgsw.tu.tuapplication.utils;

import android.content.Context;
import android.widget.ListView;

import com.android.volley.Response;
import com.dgsw.tu.tuapplication.MyVolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Wiber on 2017-12-16.
 */

public class GetFriend {

    private ArrayList<FriendListItem> data = new ArrayList<FriendListItem>();

    public ArrayList<FriendListItem> getFriend(Context context){
        MyVolley.getinstance(context).showFriends(new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {

                try {

                    JSONObject json = new JSONObject(s);
                    JSONArray jsonArray = json.getJSONArray("friendlist");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        String friendName = obj.getString("username");
                        String friendProfile = obj.getString("profile");
                        String friendId = obj.getString("userid");

                        data.add(new FriendListItem(friendName, friendId, friendProfile));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        return data;
    }
}
