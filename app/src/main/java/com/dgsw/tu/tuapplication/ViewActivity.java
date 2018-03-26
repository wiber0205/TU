package com.dgsw.tu.tuapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.dgsw.tu.tuapplication.utils.list_item;
import com.dgsw.tu.tuapplication.MyVolley;

import org.json.JSONObject;

import java.net.ResponseCache;


public class ViewActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private MyVolley myVolley;
    private TimelineItem timelineItem;

    private TextView textContent;
    private NetworkImageView imagePhoto;
    private TextView textAddress;
    private NetworkImageView imageProfile;
    private TextView textName;
    private TextView textData;
    private GPSInfo gpsInfo;
	private ImageView editImage;
	private ImageView closeImage;
    

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        myVolley = MyVolley.getinstance(this.getApplicationContext());
        gpsInfo = new GPSInfo(this);

        Intent intent = getIntent();
        timelineItem = (TimelineItem)intent.getSerializableExtra("TimelineItem");

        if ( Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    0 );
        }
        initUI();
    }

    private void initUI()
    {
        textContent = (TextView)findViewById(R.id.textContent);
        imagePhoto = (NetworkImageView)findViewById(R.id.imagePhoto);
        textAddress = (TextView)findViewById(R.id.textAddress);
        imageProfile = (NetworkImageView)findViewById(R.id.imageProfile);
        textName = (TextView)findViewById(R.id.textName);
        textData = (TextView)findViewById(R.id.textDate);
 		editImage = (ImageView)findViewById(R.id.imageSetting);
 		closeImage = (ImageView)findViewById(R.id.imageClose);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if( Build.VERSION.SDK_INT >= 21) {
            ImageUtils.procRound(imageProfile);
            ImageUtils.procRound2(imagePhoto);
        }

        imageProfile.setImageUrl(timelineItem.getProfileImage(), myVolley.getImageLoader());
        textContent.setText(timelineItem.getContent());
        imagePhoto.setImageUrl(timelineItem.getImage(), myVolley.getImageLoader());
        textAddress.setText("위치 : "+timelineItem.getAddr());
        textName.setText(timelineItem.getUsername());
        textData.setText(timelineItem.getWriteDate());

        if(MyVolley.getinstance(this).getId().equals(timelineItem.getUserid())){
            editImage.setVisibility(View.VISIBLE);
            closeImage.setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(ViewActivity.this, timelineItem.getUserid(), Toast.LENGTH_LONG);
            editImage.setVisibility(View.INVISIBLE);
            closeImage.setVisibility(View.INVISIBLE);
        }
    }

  //  @Override
    public void onMapReady(GoogleMap googleMap) {

        float lat = 0;
        float longi = 0;
        try
        {
            lat = Float.parseFloat(timelineItem.getLat());
            longi = Float.parseFloat(timelineItem.getLongi());
        }
        catch(Exception e)
        {
            lat = 36;
            longi = 128;
        }

        LatLng loc = new LatLng(lat, longi);

        GMapUtil.createMarker(googleMap, "사진위치", lat, longi);
    }

    public void onSettingClick(View v)
    {
        Intent i = new Intent(
            getApplicationContext(),
            EditActivity.class
        );
        i.putExtra("idx", timelineItem.getIdx());
        i.putExtra("imageAddr",timelineItem.getImage());
        i.putExtra("content", textContent.getText().toString());
        i.putExtra("lat", Float.parseFloat(timelineItem.getLat()));
        i.putExtra("lon", Float.parseFloat(timelineItem.getLongi()));
        i.putExtra("userName", textName.toString());
        //i.putExtra("userId", )
        i.putExtra("address", this.textAddress.getText().toString());
        startActivity(i);
    }

    private Response.Listener<String> onRetweetReponse = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            dismissProgress();
            try {
                JSONObject Json = new JSONObject(s);
                Boolean status = Json.getBoolean("status");

                if(status == true)
                {
                    Toast.makeText(ViewActivity.this, "게시되었습니다.", Toast.LENGTH_LONG);
                }
                else
                {
                    Toast.makeText(ViewActivity.this, "게시가 실패하였습니다.", Toast.LENGTH_LONG);
                }
            } catch (Exception e) {
            }
        }
    };

    private Response.ErrorListener onErrorResponse = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dismissProgress();
            Toast.makeText(ViewActivity.this, "네트워크 오류가 발생하였습니다.", Toast.LENGTH_LONG);
        }
    };

    public void onretweetClick(View v)
    {
        try {
            myVolley.shareEx("" + timelineItem.getIdx(),onRetweetReponse,  onErrorResponse);

            showProgress();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void onendClick(View v)
    {
        try {
            myVolley.removePost("" + timelineItem.getIdx(),onRetweetReponse);
            finish();
            showProgress();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void onnavClick(View v)
    {
        try{// add 김혜린 20171226 - 예외처리
            StringBuffer result = new StringBuffer("daummaps://route?sp=");

            Location loc = gpsInfo.getLocation();

            result.append((float)loc.getLatitude()); result.append(","); result.append((float)loc.getLongitude()); /*시작점*/
            result.append("&ep=");
            result.append(timelineItem.getLat()); result.append(","); result.append(timelineItem.getLongi()); /*도착점*/
            result.append("&by=CAR");
            Uri uri = Uri.parse(result.toString());
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(ViewActivity.this, "카카오맵을 깔아주세요.", Toast.LENGTH_LONG);
        }

    }

    private void showProgress()
    {
        mProgressDialog = ProgressDialog.show(this,"","계시중 중입니다.",true);
    }

    private void dismissProgress()
    {
        mProgressDialog.dismiss();
    }

}
