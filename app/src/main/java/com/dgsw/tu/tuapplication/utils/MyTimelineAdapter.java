/**
 * Created by johyunji on 2017-06-10.
 */

package com.dgsw.tu.tuapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.Image;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.dgsw.tu.tuapplication.MyVolley;
import com.dgsw.tu.tuapplication.R;
import com.dgsw.tu.tuapplication.ViewActivity;
import com.dgsw.tu.tuapplication.model.TimelineItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by johyunji on 2017-06-10.
 */

public class MyTimelineAdapter extends RecyclerView.Adapter implements Response.Listener<String>, Response.ErrorListener{

    protected Context context;
    protected MyVolley myVolley;
    protected ArrayList<TimelineItem> listItemList = new ArrayList<TimelineItem>(); // 데이타
    protected OnRefreshedListener onRefreshedListener ;

    public RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        MyVolley myVolley = null;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if(MyTimelineAdapter.this.myVolley == null)
                return;

            myVolley = MyTimelineAdapter.this.myVolley;
            int lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;

            if (lastVisibleItemPosition == itemTotalCount) {
                String reqIdx = "" + listItemList.get(listItemList.size() - 1).getIdx();
                myVolley.getMyOldTimelineEx(myVolley.getId(), reqIdx, MyTimelineAdapter.this, MyTimelineAdapter.this);
            }

        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView date;
        private TextView addr;
        private TextView content;
        private NetworkImageView iv;
        private FrameLayout container; // 2에서는 필요 없음
        // private NetworkImageView proImage 2에서는 필요 있음
        // private TextView name 2에서는 필요 있음
        // private Image share 2에서는 필요 있음

        public ViewHolder(View v){
            super(v);
            this.date = (TextView) v.findViewById(R.id.textDate);
            this.addr = (TextView) v.findViewById(R.id.textAddr);
            this.content = (TextView) v.findViewById(R.id.textContent);
            this.iv = (NetworkImageView) v.findViewById(R.id.imagePhoto);
            this.container = (FrameLayout)v.findViewById(R.id.itemContainer);
        }

        public void setAddr(String title){
            addr.setText(title);
        }
        public void setDate(String title){
            date.setText(title);
        }
        public void setContent(String title){
            content.setText(title);
        }

        public void  setimg(String url){
            iv.setImageUrl(url, MyVolley.getinstance(context).getImageLoader());
            iv.setBackgroundResource(R.drawable.rounded_white);
            iv.setClipToOutline(true);
        }

        public void setOnClickListener(int position, View.OnClickListener l)
        {
            container.setTag(position);
            container.setOnClickListener(l);
        }
    }

    public interface OnRefreshedListener
    {
        public void OnRefeshed();
    }


    public MyTimelineAdapter(Context context) {
        this.context = context;
        myVolley = MyVolley.getinstance(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.mytimeline_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder)holder;

        if(listItemList.size() <= position)
        {
            return;
        }

        vh.setimg(listItemList.get(position).getImage());
        vh.setAddr(listItemList.get(position).getAddr());
        vh.setDate(listItemList.get(position).getWriteDate());
        vh.setContent(listItemList.get(position).getContent());

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
        });
    }

    public void reflash(OnRefreshedListener onRefreshedListener)
    {
        this.onRefreshedListener = onRefreshedListener;

        try {
            myVolley.getMyTimelineEx(myVolley.getId(), this, this);
        }
        catch(Exception e)
        {
            this.onRefreshedListener.OnRefeshed();
        }
    }

    @Override
    public int getItemCount() {
        return listItemList.size();
    }

    @Override
    public void onErrorResponse(VolleyError volleyError)
    {
        this.onRefreshedListener.OnRefeshed();
        Toast.makeText(context,"네트워크 에러가 발생하였습니다.", Toast.LENGTH_LONG);
    }

    @Override
    public void onResponse(String s) {

        JSONObject json = null;
        JSONArray jsonArray = null;

        if (this.onRefreshedListener != null) {
            this.onRefreshedListener.OnRefeshed();
        }

        try {
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
                    userid = (String) obj.get("userid");
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
        } catch (Exception e) {
            e.printStackTrace();
            notifyDataSetChanged();
        }
    }
}

