package com.dgsw.tu.tuapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.Image;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.dgsw.tu.tuapplication.FriendTimeLineActivity;
import com.dgsw.tu.tuapplication.MyVolley;
import com.dgsw.tu.tuapplication.R;
import com.dgsw.tu.tuapplication.TimeLineActivity;
import com.dgsw.tu.tuapplication.ViewActivity;
import com.dgsw.tu.tuapplication.model.TimelineItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by johyunji on 2017-06-10.
 */

public class TimelineAdapter extends RecyclerView.Adapter implements Response.Listener<String>, Response.ErrorListener{

    protected Context context;
    protected MyVolley myVolley;
    protected ArrayList<TimelineItem> listItemList = new ArrayList<TimelineItem>(); // 데이타
    protected OnRefreshedListener onRefreshedListener ;
	// add 20171223 김민기 - 검색기능 추가 [[
    private boolean isSearch = false;
    private String keyword = null;

    public ArrayList<TimelineItem> getListItemList() {
        return listItemList;
    }

    public void setListItemList(ArrayList<TimelineItem> listItemList) {
        this.listItemList = listItemList;
    }

    public void endSearch(){ // add 20171223 김민기 - 검색기능 추가
        isSearch = false;
    }

    public void clearList() {
        this.listItemList.clear();
    }

    public void onSearch(String keyword){
        myVolley = TimelineAdapter.this.myVolley;
        isSearch = true;
        this.keyword = keyword;
        myVolley.search(keyword, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject json = new JSONObject(s);
                    JSONArray jsonArray = json.getJSONArray("searchlist");
                    listItemList.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String imagePath = "";
                        String date = "";
                        String lat = "0";
                        String longi = "0";
                        String addr = "";
                        String content = "";
                        String username = "";
                        int idx = 0;
                        String profilePath = "";
                        String userid = "";
                        try {
                            idx = (int) obj.get("idx");
                        } catch (Exception e) {
                        }
                        try {
                            imagePath = (String) obj.get("imagepath");
                        } catch (Exception e) {
                        }
                        try {
                            date = (String) obj.get("date");
                        } catch (Exception e) {
                        }
                        try {
                            lat = "" + (String) obj.get("lat");
                        } catch (Exception e) {
                        }
                        try {
                            longi = "" + (String) obj.get("long");
                        } catch (Exception e) {
                        }
                        try {
                            addr = (String) obj.get("addr");
                        } catch (Exception e) {
                        }
                        try {
                            content = (String) obj.get("content");
                        } catch (Exception e) {
                        }
                        try {
                            username = (String) obj.get("username");
                        } catch (Exception e) {
                        }
                        try {
                            profilePath = (String) obj.get("profile");
                        } catch (Exception e) {
                        }
                        try {
                            userid = (String) obj.get("id");
                        } catch (Exception e) {
                        }
                        TimelineItem timelineItem = new TimelineItem(idx, myVolley.SERVER_IP + imagePath, myVolley.SERVER_IP + profilePath, date, addr, content, lat, longi, userid, username);
                        listItemList.add(timelineItem);


                    }
                    notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
	// add 20171223 김민기 - 검색기능 추가 ]]
	
    public RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        MyVolley myVolley = null;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if(TimelineAdapter.this.myVolley == null)
                return;

            myVolley = TimelineAdapter.this.myVolley;
            int lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;

            try{// add 20171226 김혜린 - 예외처리
                if (lastVisibleItemPosition == itemTotalCount) {
                    String reqIdx = "" + listItemList.get(listItemList.size()-1).getIdx();
                    if(isSearch){ // add 20171223 김민기 - 검색기능 추가
                        myVolley.oldSearch(keyword, reqIdx, TimelineAdapter.this, TimelineAdapter.this);
                    } else {
                        myVolley.getOldTimelineEx(myVolley.getId(), reqIdx, TimelineAdapter.this, TimelineAdapter.this);
                    }
                }
            }catch (Exception e){}

        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView date;
        private TextView addr;
        private NetworkImageView iv;
        private NetworkImageView proImage;
        private TextView name;
        private ImageView share;
        private LinearLayout container;
        private LinearLayout container2;

        public ViewHolder(View v){
            super(v);
            this.date = (TextView) v.findViewById(R.id.textDate2);
            this.name = (TextView) v.findViewById(R.id.textNickName2);
            this.iv = (NetworkImageView) v.findViewById(R.id.imagePhoto2);
            this.proImage = (NetworkImageView) v.findViewById(R.id.proImage);
            this.container = (LinearLayout) v.findViewById(R.id.itemContainer2);
            this.container2 = (LinearLayout) v.findViewById(R.id.itemContainer3);
            this.addr = (TextView) v.findViewById(R.id.textAddr2);
            this.share = (ImageView) v.findViewById(R.id.share);
        }

        public void setAddr(String title){
            addr.setText(title);
        }
        public void setDate(String write_date){
            this.date.setText(write_date);
        }
        // public void setContent(String title){content.setText(title);}

        public void setimg(String url){
            iv.setImageUrl(url, MyVolley.getinstance(context).getImageLoader());
            iv.setBackgroundResource(R.drawable.rounded_white);
            iv.setClipToOutline(true);
        }

        public void setProfile_image(String url){
            proImage.setImageUrl(url, MyVolley.getinstance(context).getImageLoader());
            proImage.setDefaultImageResId(R.mipmap.usericon);
            proImage.setErrorImageResId(R.mipmap.usericon);

            proImage.setBackground(new ShapeDrawable(new OvalShape()));
            proImage.setClipToOutline(true);
        }

        public void setOnClickListener(int position, View.OnClickListener l, View.OnClickListener l2, View.OnClickListener l3) {
            container.setTag(position);
            container.setOnClickListener(l);
            proImage.setTag(position);
            proImage.setOnClickListener(l2);
            share.setTag(position);
            share.setOnClickListener(l3);
        }

        public void setUserName(String userName) {
            this.name.setText(userName);
        }
    }

    public interface OnRefreshedListener {
        public void OnRefeshed();
    }


    public TimelineAdapter(Context context) {
        this.context = context;
        myVolley = MyVolley.getinstance(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.timeline_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder)holder;

        if (listItemList.size() <= position) {
            return;
        }

        vh.setimg(listItemList.get(position).getImage());
        vh.setProfile_image(listItemList.get(position).getProfileImage());
        vh.setAddr(listItemList.get(position).getAddr());
        vh.setDate(listItemList.get(position).getWriteDate());
        vh.setUserName(listItemList.get(position).getUsername());

        vh.setOnClickListener(position, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int)v.getTag();

                Intent i = new Intent(context, ViewActivity.class);

                if(listItemList.size() <= position)
                    return;

                i.putExtra("TimelineItem", listItemList.get(position));

                context.startActivity(i);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();

                Intent i = new Intent(context, FriendTimeLineActivity.class);

                if (listItemList.size() <= position)
                    return;

                Log.v("태그", listItemList.get(position).getUserid());

                i.putExtra("item", listItemList.get(position));

                context.startActivity(i);
            }
        }, new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });
    }

    public void reflash(OnRefreshedListener onRefreshedListener) {
        this.onRefreshedListener = onRefreshedListener;

        try {
            myVolley.getTimelineEx(myVolley.getId(), this, this);
        } catch (Exception e) {
            this.onRefreshedListener.OnRefeshed();
        }
    }

    @Override
    public int getItemCount() {
        return listItemList.size();
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        this.onRefreshedListener.OnRefeshed();
        Toast.makeText(context,"네트워크 에러가 발생하였습니다.", Toast.LENGTH_LONG);
    }

    @Override
    public void onResponse(String s) {

        JSONObject json = null;
        JSONArray jsonArray = null;

        if(this.onRefreshedListener != null) {
            this.onRefreshedListener.OnRefeshed();
        }

        try {
            if(isSearch){// add 20171223 김민기 - 검색기능 추가 
                json = new JSONObject(s);
                jsonArray = json.getJSONArray("oldsearchlist");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String imagePath = "";
                    String date = "";
                    String lat = "0";
                    String longi = "0";
                    String addr = "";
                    String content = "";
                    String username = "";
                    int idx = 0;
                    String profilePath = "";
                    String userid = "";
                    try {
                        idx = (int) obj.get("idx");
                    } catch (Exception e) {
                    }
                    try {
                        imagePath = (String) obj.get("imagepath");
                    } catch (Exception e) {
                    }
                    try {
                        date = (String) obj.get("date");
                    } catch (Exception e) {
                    }
                    try {
                        lat = "" + (String) obj.get("lat");
                    } catch (Exception e) {
                    }
                    try {
                        longi = "" + (String) obj.get("long");
                    } catch (Exception e) {
                    }
                    try {
                        addr = (String) obj.get("addr");
                    } catch (Exception e) {
                    }
                    try {
                        content = (String) obj.get("content");
                    } catch (Exception e) {
                    }
                    try {
                        username = (String) obj.get("username");
                    } catch (Exception e) {
                    }
                    try {
                        profilePath = (String) obj.get("profile");
                    } catch (Exception e) {
                    }
                    try {
                        userid = (String) obj.get("id");
                    } catch (Exception e) {
                    }

                	TimelineItem timelineItem = new TimelineItem(idx, myVolley.SERVER_IP + imagePath, myVolley.SERVER_IP + profilePath, date, addr, content, lat, longi, userid, username);
                    listItemList.add(timelineItem);
                }
                notifyDataSetChanged();
            } else {
            json = new JSONObject(s);

            if (json.isNull("postlist") == false) {
                jsonArray = json.getJSONArray("postlist");
                listItemList.clear();
            } else {
                jsonArray = json.getJSONArray("oldpostlist");
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String imagePath = "";
                String date = "";
                String lat = "0";
                String longi = "0";
                String addr = "";
                String content = "";
                String userid = "";
                String username = "";
                int idx = 0;
                String profilePath = "";

                try {
                    idx = (int) obj.get("idx");
                } catch (Exception e) {
                }
                try {
                    imagePath = (String) obj.get("imagepath");
                } catch (Exception e) {
                }
                try {
                    date = (String) obj.get("date");
                } catch (Exception e) {
                }
                try {
                    lat = "" + (String) obj.get("lat");
                } catch (Exception e) {
                }
                try {
                    longi = "" + (String) obj.get("long");
                } catch (Exception e) {
                }
                try {
                    addr = (String) obj.get("addr");
                } catch (Exception e) {
                }
                try {
                    content = (String) obj.get("content");
                } catch (Exception e) {
                }
                try {
                    userid = (String) obj.get("id");
                } catch (Exception e) {
                }
                try {
                    username = (String) obj.get("username");
                } catch (Exception e) {
                }
                try {
                    profilePath = (String) obj.get("profile");
                } catch (Exception e) {
                }

                TimelineItem timelineItem = new TimelineItem(idx, myVolley.SERVER_IP + imagePath, myVolley.SERVER_IP + profilePath, date, addr, content, lat, longi, userid, username);
                listItemList.add(timelineItem);
            }
            notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
            notifyDataSetChanged();
        }
    }


}
