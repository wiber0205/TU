package com.dgsw.tu.tuapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.dgsw.tu.tuapplication.model.TimelineItem;
import com.dgsw.tu.tuapplication.utils.GMapUtil;
import com.dgsw.tu.tuapplication.utils.GPSInfo;
import com.dgsw.tu.tuapplication.utils.ImageUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;

/**
 * Created by Administrator on 2017-12-18.
 */

public class EditActivity extends AppCompatActivity implements OnMapReadyCallback, Response.Listener<String> {
    TimelineItem timelineItem;

    private GoogleMap map;
    private MyVolley myVolley;
    private GPSInfo gpsInfo;

    private NetworkImageView networkImageView;
    private EditText content;
    private TextView address;

    float lat;
    float lon;
    //private String name;
    //private String id;

    private int postIdx;

    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        myVolley = MyVolley.getinstance(this.getApplicationContext());
        gpsInfo = new GPSInfo(this);

        initUI();
    }

    private void initUI() {

        networkImageView = (NetworkImageView)findViewById(R.id.imagePhoto);
        content = (EditText)findViewById(R.id.contents);
        address = (TextView)findViewById(R.id.addrField);

        myVolley = MyVolley.getinstance(this);


        Intent intent = getIntent();
        postIdx = intent.getIntExtra("idx", 0);
        networkImageView.setImageUrl(intent.getStringExtra("imageAddr"), myVolley.getImageLoader());
        content.setText(intent.getStringExtra("content"));
        lat = intent.getFloatExtra("lat", 0.0f);
        lon = intent.getFloatExtra("lon", 0.0f);
        address.setText(intent.getStringExtra("address"));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if( Build.VERSION.SDK_INT >= 21) {
            ImageUtils.procRound2(networkImageView);
        }

    }

    public void onMapReady(GoogleMap googleMap) {

        LatLng loc = new LatLng(lat, lon);

        GMapUtil.createMarker(googleMap, "사진위치", lat, lon);
    }

    private final static String TAG = "EditActivity";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onSubmitClick(View view) {

        try {
            myVolley.reWrite(content.getText().toString(), Integer.toString(postIdx), this);
            ShowProgress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onErrorResponse(VolleyError volleyError) {
        DismissProgress();
        Toast.makeText(this, "Volley 오류로 인해 실패하였습니다.", Toast.LENGTH_SHORT).show();
    }

    public void onResponse(String s) {
        DismissProgress();

        try {
            JSONObject json = new JSONObject(s);
            boolean status = json.getBoolean("status");
            String message = json.getString("message");

            if (status && message.equals("001")) {
                finish();
            } else{
                Toast.makeText(this, "네트워크 오류로 인해 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 팝업 만들기 전에 임시 변수
    private void ShowProgress() {
        mProgressDialog = ProgressDialog.show(this, "", "업로드 중입니다.", true);
    }

    private void DismissProgress() {
        mProgressDialog.dismiss();
    }
}
