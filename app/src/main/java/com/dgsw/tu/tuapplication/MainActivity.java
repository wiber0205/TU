package com.dgsw.tu.tuapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void OnMyLine(View v)
    {
        Intent i = new Intent(this, MyTimeLineActivity.class);
        startActivity(i);
    }

    public void OnView(View v)
    {
        Intent i = new Intent(this, ViewActivity.class);

        i.putExtra("date","2017-07-16");
        i.putExtra("content", "아-기 상어 뚜루루뚜루 기여운 뚜루루뚜루 바닷속 뚜루룻뚜루 아기상어~! 뚜뚜 보라도리 뚜비 나비야 나비야~");
        i.putExtra("lat", "36.0");
        i.putExtra("long", "127.0");
        //String url = MyVolley.getPost(,);
        //i.putExtra("url", url);
        i.putExtra("addr", "대구광역시 달성군 구지면 창리로 11길 93");

        startActivity(i);
    }

    public void OnUpload(View v)
    {
        Intent i = new Intent(this, UpLoadActivity.class);
        startActivity(i);
    }

    public void OnJRegister(View v)
    {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    public void OnLogin(View v)
    {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void OnEdit(View v)
    {
        Intent i = new Intent(this, EditProfileActivity.class);
        startActivity(i);
    }
}
