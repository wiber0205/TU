package com.dgsw.tu.tuapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.dgsw.tu.tuapplication.utils.ImageUtils;
import com.dgsw.tu.tuapplication.utils.MyTimelineAdapter;

public class MyTimeLineActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, MyTimelineAdapter.OnRefreshedListener{

    private SwipeRefreshLayout refreshView;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private MyTimelineAdapter myTimelineAdapter;
    private Intent intent;
    private ImageButton backKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_timeline);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        myTimelineAdapter = new MyTimelineAdapter(this);
        recyclerView.setAdapter(myTimelineAdapter);

        recyclerView.addOnScrollListener(myTimelineAdapter.onScrollListener);

        NetworkImageView profile = (NetworkImageView) findViewById(R.id.imageProfile);
        profile.setDefaultImageResId(R.mipmap.dimg);
        profile.setImageUrl(MyVolley.getinstance(this).SERVER_IP+MyVolley.getinstance(this).getProFileImg(), MyVolley.getinstance(this).getImageLoader());

        if( Build.VERSION.SDK_INT >= 21) {
            ImageUtils.procRound(profile);
        }

        TextView nickName = (TextView)findViewById(R.id.textNickName);
        nickName.setText(MyVolley.getinstance(this).getUserName() + "(" + MyVolley.getinstance(this).getId() + ")");

        //NetworkImageView nv= (NetworkImageView)findViewById(R.id.imageView4);
        //nv.setImageUrl(/*MyVolley.getinstance(this).getPhotoPath()*/"http://10.80.163.136:8080/images/profile/img_1.jpg", MyVolley.getinstance(this).getImageLoader());


        refreshView=(SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        refreshView.setOnRefreshListener(this);

        fab = (FloatingActionButton)findViewById(R.id.floating_btn);
        //fab.attachToListView(recyclerView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                intent= new Intent(MyTimeLineActivity.this, UpLoadActivity.class);
                startActivity(intent);
            }
        });

       backKey = (ImageButton)findViewById(R.id.backButton);
       backKey.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v){
               intent= new Intent(MyTimeLineActivity.this, TimeLineActivity.class);
               startActivity(intent);
            }
        });

        myTimelineAdapter.reflash(this);
    }

    @Override
    public void onRefresh() {
        myTimelineAdapter.reflash(this);
    }

    @Override
    public void OnRefeshed() {
        refreshView.setRefreshing(false);
    }
}
