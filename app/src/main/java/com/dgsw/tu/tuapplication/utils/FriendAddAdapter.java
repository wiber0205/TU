package com.dgsw.tu.tuapplication.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.dgsw.tu.tuapplication.MyVolley;
import com.dgsw.tu.tuapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by WIBER on 2017-12-17.
 */

public class FriendAddAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<FriendAddListItem> data = new ArrayList<FriendAddListItem>();
    private int layout;
    private Context context;

    public FriendAddAdapter(Context context, int layout) {
        this.layout = layout;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;

        MyVolley.getinstance(context).friendRequestList(new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                JSONObject json = null;
                try {
                    json = new JSONObject(s);
                    JSONArray jsonArray = json.getJSONArray("friendRequestList");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        String friendId = obj.getString("senderId");
                        String addTime = obj.getString("date");
                        String profile = obj.getString("profile");

                        data.add(new FriendAddListItem(friendId, addTime, profile));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                FriendAddAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i).getFriendId();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) { //final 2017 12 26 BJH

        if (view == null) {
            view = inflater.inflate(layout, viewGroup, false);
        }

        FriendAddListItem item = data.get(i);

        NetworkImageView FriendProFile = (NetworkImageView) view.findViewById(R.id.addFriendProfileImage);
        TextView FriendId = (TextView) view.findViewById(R.id.addFriendName);
        TextView addTime = (TextView) view.findViewById(R.id.addFriendAddTime);

        Timestamp time = Timestamp.valueOf(item.getAtAdd());
        final String friendId = item.getFriendId();


        FriendId.setText(friendId);
        FriendProFile.setImageUrl(MyVolley.getinstance(context).SERVER_IP + item.getProfileImage(), MyVolley.getinstance(context).getImageLoader());
        addTime.setText(time.getMonth() + "월 " + time.getDate() + "일 \n" + time.getHours() + "시 " + time.getMinutes() + "분");

        view.findViewById(R.id.btn_YesFriend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyVolley.getinstance(context).addFriend(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject json = new JSONObject(s);

                            boolean status = json.getBoolean("status");
                            String message = json.getString("message");

                            data.remove(i);//Remove item 2017 12 26 BJH

                            notifyDataSetChanged();//Refresh 2017 12 26 BJH
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, friendId);

            }
        });

        view.findViewById(R.id.btn_DefuseFriend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyVolley.getinstance(context).refuseFriend(friendId, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject json = new JSONObject(s);

                            boolean status = json.getBoolean("status");
                            String message = json.getString("message");

                            data.remove(i);//Remove item 2017 12 26 BJH

                            notifyDataSetChanged();//Refresh 2017 12 26 BJH
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });


        return view;
    }


}
