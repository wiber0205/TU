package com.dgsw.tu.tuapplication;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;

public class EditProfileActivity extends AppCompatActivity {

    NetworkImageView profileImage;
    TextView name;
    TextView password;
    TextView checkPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileImage = (NetworkImageView) findViewById(R.id.editProfileImage);
        name = (TextView) findViewById(R.id.editId);
        password = (TextView) findViewById(R.id.editPassword);
        checkPassword = (TextView) findViewById(R.id.editCheckPassword);

        profileImage.setImageUrl(MyVolley.getinstance(this).SERVER_IP + MyVolley.getinstance(this).getProFileImg(), MyVolley.getinstance(this).getImageLoader());
        name.setText(MyVolley.getinstance(this).getId());

        profileImage.setEnabled(false);
        name.setEnabled(false);
        password.setEnabled(false);
        checkPassword.setEnabled(false);

    }

    public void onClickCP(View v) {
        TextView passwordTextView = (TextView) findViewById(R.id.checkPasswordTextView);
        String passwords = passwordTextView.getText().toString();

        MyVolley.getinstance(this).checkPassword(new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject json = new JSONObject(s);

                    Boolean status = (Boolean) json.get("status");
                    String message = (String) json.get("message");

                    profileImage.setEnabled(true);
                    name.setEnabled(true);
                    password.setEnabled(true);
                    checkPassword.setEnabled(true);

                    if (!status) {
                        Log.d("CHECK PASSWORD ERROR", message);
                    } else {
                        findViewById(R.id.editPanel1).setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, passwords);
    }

    public void onClickBtnEdit(View v) {

        String name = this.name.getText().toString();
        String pw = this.password.getText().toString();

        File image = (File) profileImage.getTag();

        if (pw.equals(checkPassword.getText().toString())) {

            MyVolley.getinstance(this).editProfile(new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject json = new JSONObject(s);

                        Boolean status = (Boolean) json.get("status");
                        String message = (String) json.get("message");

                        if (!status) {
                            Log.d("where where where", message);
                        } else {
                            findViewById(R.id.editPanel1).setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, name, pw, image);
        } else{
            Log.d("asdfasdfasdf", "asdfasdfasdf");
        }

    }
}
