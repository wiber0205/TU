package com.dgsw.tu.tuapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.dgsw.tu.tuapplication.model.TimelineItem;
import com.dgsw.tu.tuapplication.utils.FriendTimeLineAdapter;
import com.dgsw.tu.tuapplication.utils.ImageUtils;
import com.dgsw.tu.tuapplication.utils.MyTimelineAdapter;

/**
 * Created by user on 2017-12-17.
 */

public class FriendTimeLineActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, FriendTimeLineAdapter.OnRefreshedListener, Response.Listener<String>, Response.ErrorListener {
    private SwipeRefreshLayout refreshView;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private FriendTimeLineAdapter friendTimeLineAdapter;
    private MyVolley myVolley = null;
    private String userid = "";

    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_timeline);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();

        TimelineItem item = (TimelineItem) intent.getSerializableExtra("item");

        userid = item.getUserid();
        friendTimeLineAdapter = new FriendTimeLineAdapter(this, item.getUserid());
        recyclerView.setAdapter(friendTimeLineAdapter);

        recyclerView.addOnScrollListener(friendTimeLineAdapter.onScrollListener);

        NetworkImageView profile = (NetworkImageView) findViewById(R.id.imageProfile);
        profile.setDefaultImageResId(R.mipmap.dimg);
        profile.setImageUrl(item.getProfileImage(), MyVolley.getinstance(this).getImageLoader());

        if (Build.VERSION.SDK_INT >= 21) {
            ImageUtils.procRound(profile);
        }

        TextView nickName = (TextView) findViewById(R.id.textNickName);
        nickName.setText(item.getUsername() + "(" + item.getUserid() + ")");

        //NetworkImageView nv= (NetworkImageView)findViewById(R.id.imageView4);
        //nv.setImageUrl(/*MyVolley.getinstance(this).getPhotoPath()*/"http://10.80.163.136:8080/images/profile/img_1.jpg", MyVolley.getinstance(this).getImageLoader());


        refreshView = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        refreshView.setOnRefreshListener(this);

        fab = (FloatingActionButton) findViewById(R.id.floating_btn);
        //fab.attachToListView(recyclerView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendTimeLineActivity.this, UpLoadActivity.class);
                startActivity(intent);
            }
        });

        friendTimeLineAdapter.reflash(this);

        myVolley = MyVolley.getinstance(this.getApplicationContext());
    }

    public  void onAddFriendClick(View v)
    {
        ShowProgress();
        myVolley.addFriendEx(userid, this, this);
    }

    @Override
    public void onRefresh() {
        friendTimeLineAdapter.reflash(this);
    }

    @Override
    public void OnRefeshed() {
        refreshView.setRefreshing(false);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        DismissProgress();
        Toast.makeText(this, "네트워크 오류가 발생하였습니다.\n 잠시후 다시 시도해 주세요",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(String s) {
        DismissProgress();
    }

    // 팝업 만들기 전에 임시 변수
    private void ShowProgress() {
        mProgressDialog = ProgressDialog.show(this, "", "친구신청 중입니다.", true);
    }

    private void DismissProgress() {
        mProgressDialog.dismiss();
    }
}
