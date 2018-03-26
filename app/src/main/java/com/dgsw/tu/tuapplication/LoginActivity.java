package com.dgsw.tu.tuapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.navercorp.volleyextensions.volleyer.builder.PostBuilder;
import com.google.firebase.iid.FirebaseInstanceId;
import com.navercorp.volleyextensions.volleyer.factory.DefaultRequestQueueFactory;

import org.json.JSONException;
import org.json.JSONObject;

import static com.navercorp.volleyextensions.volleyer.Volleyer.volleyer;

public class LoginActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener{

    MyVolley myVolley = null;  // 매번 생성하지 않도록 하나의 멤버를 만든다.
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_login);
            myVolley = MyVolley.getinstance(this.getApplicationContext());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void OnLoginClick(View v)
    {

        //RequestQueue requestQueue = DefaultRequestQueueFactory.create(LoginActivity.this); 불필요한 코드
        //requestQueue.start(); 불필요한 코드

        try
        {
            EditText LoginId = (EditText) findViewById(R.id.LoginId);
            EditText LoginPw = (EditText) findViewById(R.id.LoginPw);

            String id = LoginId.getText().toString();   // 변수 이름은 시작은 소문자로 통일
            String pwd = LoginPw.getText().toString();  // 변수 이름은 시작은 소문자로 통일
            String token = FirebaseInstanceId.getInstance().getToken();

            if (myVolley != null) {
                myVolley.loginEx(id, pwd, token, this, this);  // Error과 성공 모두 받는다.

                ShowProgress();
            } else {
                Toast.makeText(this, "네트워크 자원이 유효하지 않습니다.", Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void OnSignUpClick(View v) {  // 이벤트 콜백 메쏘드 임을 나타내는 이름으로 변경


        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void onResponse(String message)
    {
        boolean status = false;
        JSONObject json = null;

        DismissProgress();

        try {
            json = new JSONObject(message);
            status = (Boolean) json.get("status");
            myVolley.setUserName((String) json.get("userName"));
            myVolley.setId((String) json.get("id"));

            try {
                myVolley.setProfileImg((String) json.get("profile"));
            }
            catch(Exception e)
            {
                myVolley.setProfileImg(null);
            }

            Log.d(this.getLocalClassName(),  myVolley.getUserName() + ", " + myVolley.getId() + ", " + myVolley.getProFileImg());

            if (status) {
                //Toast toast003 = Toast.makeText(LoginActivity.this, "로그인성공", Toast.LENGTH_SHORT); 성공은 따로 알릴 필요가 없음
                //toast003.show();
                Intent it = new Intent(LoginActivity.this, TimeLineActivity.class);
                startActivity(it);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "로그인실패", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, "로그인실패", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        DismissProgress();
        Toast.makeText(LoginActivity.this, "로그인실패", Toast.LENGTH_SHORT).show();
    }

    // 팝업 만들기 전에 임시 변수
    private boolean isLogging = false; // 로그인 중일때 사용자 입력을 받지 않기 위해 선언

    private void ShowProgress()
    {
        mProgressDialog = ProgressDialog.show(this,"","로그인 중입니다.",true);
    }

    private void DismissProgress()
    {
        mProgressDialog.dismiss();
    }



}
