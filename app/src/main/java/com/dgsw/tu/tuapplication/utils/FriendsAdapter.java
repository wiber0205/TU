package com.dgsw.tu.tuapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.NetworkImageView;
import com.dgsw.tu.tuapplication.MyVolley;
import com.dgsw.tu.tuapplication.R;
import com.dgsw.tu.tuapplication.SidebarFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Wiber on 2017-12-16.
 */

public class FriendsAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private ArrayList<FriendListItem> data = new ArrayList<FriendListItem>();
    private int layout;
    private Context context;

    public FriendsAdapter(Context context, int layout) {
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;

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
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i).getFriendName();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(layout, viewGroup, false);
        }

        FriendListItem item = data.get(i);

        final String friendId = item.getFrendId();

        NetworkImageView FriendProFile = (NetworkImageView) view.findViewById(R.id.friendProfileImage);
        TextView FriendName = (TextView) view.findViewById(R.id.friendName);

        FriendProFile.setImageUrl(MyVolley.getinstance(context).SERVER_IP + item.getFriendProFile(), MyVolley.getinstance(context).getImageLoader());
        FriendName.setText(item.getFriendName());

        ImageView btnDel = (ImageView) view.findViewById(R.id.btn_del);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyVolley.getinstance(context).deleteFriend(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject json = null;
                        try {
                            json = new JSONObject(s);

                            boolean status = json.getBoolean("status");
                            String message = json.getString("message");

                            if (status == true) {
                                data.remove(i);
                                notifyDataSetChanged();
                            }else{
                                Log.d("Del Friend", message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, friendId);
            }
        });

        return view;
    }
}
