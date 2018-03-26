package com.dgsw.tu.tuapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.dgsw.tu.tuapplication.utils.TimelineAdapter;

public class TimeLineActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, TimelineAdapter.OnRefreshedListener{

    private SwipeRefreshLayout refreshView;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private TimelineAdapter timelineAdapter;
    private EditText editText; // add 김민기 20171223 - 검색기능 추가
    String keyword = ""; // add 김민기 20171223 - 검색기능 추가

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        timelineAdapter = new TimelineAdapter(this);
        recyclerView.setAdapter(timelineAdapter);

        recyclerView.addOnScrollListener(timelineAdapter.onScrollListener);

        refreshView=(SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        refreshView.setOnRefreshListener(this);

        timelineAdapter.reflash(this);

        findViewById(R.id.searchPanel).setVisibility(View.GONE);// add 김민기 20171223 - 검색기능 추가
        findViewById(R.id.stopSearch).setVisibility(View.GONE);// add 김민기 20171223 - 검색기능 추가

        editText = (EditText)findViewById(R.id.et); // add 김민기 20171223 - 검색기능 추가
        editText.addTextChangedListener(new TextWatcher() { // add 김민기 20171223 - 검색기능 추가
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start == 0){
                    findViewById(R.id.startSearch).setVisibility(View.GONE);
                    findViewById(R.id.stopSearch).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.startSearch).setVisibility(View.VISIBLE);
                    findViewById(R.id.stopSearch).setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void onPostWriteClick(View v)
    {
        Intent intent = new Intent(this, UpLoadActivity.class);
        startActivity(intent);
    }

    public void onBtn1(View v) { // add 김민기 20171223 - 검색기능 추가
        findViewById(R.id.writePanel).setVisibility(View.GONE);
        findViewById(R.id.Panel).setVisibility(View.GONE);
        findViewById(R.id.searchPanel).setVisibility(View.VISIBLE);
    }

    public void onClickSearchByKeyword(View v){ // add 김민기 20171223 - 검색기능 추가
        editText = (EditText)findViewById(R.id.et);
        keyword = editText.getText().toString();
        timelineAdapter.clearList();
        timelineAdapter.onSearch(keyword);
    }

    public void onEndSearchMode(View v){ // add 김민기 20171223 - 검색기능 추가
        findViewById(R.id.searchPanel).setVisibility(View.GONE);
        findViewById(R.id.writePanel).setVisibility(View.VISIBLE);
        findViewById(R.id.Panel).setVisibility(View.VISIBLE);
        timelineAdapter.endSearch();
        timelineAdapter.reflash(this);
        //timelineAdapter = new TimelineAdapter(this);
    }

    @Override
    public void onRefresh() {
        timelineAdapter.reflash(this);
    }

    @Override
    public void OnRefeshed() {

        refreshView.setRefreshing(false);
    }
}
