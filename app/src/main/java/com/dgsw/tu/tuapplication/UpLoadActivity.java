package com.dgsw.tu.tuapplication;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dgsw.tu.tuapplication.utils.FileUtil;
import com.dgsw.tu.tuapplication.utils.GMapUtil;
import com.dgsw.tu.tuapplication.utils.GPSInfo;
import com.dgsw.tu.tuapplication.utils.ImageUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class UpLoadActivity extends AppCompatActivity implements OnMapReadyCallback, Response.Listener<String>, Response.ErrorListener {
    private final static String TAG = "UpLoadActivity";

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;

    private class DataHolder {
        public Location location = null;
        public String addr = null;
        public File image = null;

        public DataHolder(Location location, String addr, File image) {
            this.location = location;
            this.addr = addr;
            this.image = image;
        }
    }

    private MyVolley myVolley = null;
    ProgressDialog mProgressDialog;
    private DataHolder dataHolder = null;
    private GoogleMap googleMap;
    private GPSInfo gpsInfo;
    private ImageView imageView;
    private TextView addrField;
    private EditText contents;

    private File capturedImage;

    private View map;


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_load);

        myVolley = MyVolley.getinstance(this.getApplicationContext());

        initUI();
    }

    private void initUI() {
        try {
            imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.noimage);
            addrField = (TextView) findViewById(R.id.addrField);
            contents = (EditText) findViewById(R.id.contents);
            map = findViewById(R.id.map);

            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
            map.setVisibility(View.GONE);

            gpsInfo = new GPSInfo(this);

            if(gpsInfo.isNotSetting())
            {
                gpsInfo.showSettingsAlert();
            }
            if (Build.VERSION.SDK_INT < 23) // 빌드버젼이 23 이상일때만 권한 획득 팝업 만듬
                return;

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //------------------------------------------------------------------------------------------카메라에서 사진 찍기
        try {
            /* 카메라에서 되돌아 왔을때 */
            if (requestCode == REQUEST_IMAGE_CAPTURE && requestCode != RESULT_CANCELED) {
                onCameraResult();
                return;
            } else if (requestCode == REQUEST_IMAGE_GALLERY && requestCode != RESULT_CANCELED)// / -----------------------------------------------------------------------------------갤러리에서 사진 가져오기----------------------------
            {
                onGalleryResult(data);
                return;
            }
            else if(requestCode == GPSInfo.REQUEST_GPS_SETTING)
            {
                if(gpsInfo.isNotSetting())
                {
                    finish();
                }
            }
        } catch (Exception e) {
            dataHolder = null;
            e.printStackTrace();
        }
    }

    public void onCameraResult() throws Exception {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap imageBitmap;
        String addr;

        try {
            imageBitmap = BitmapFactory.decodeFile(capturedImage.getAbsolutePath(), options);
            imageBitmap = ImageUtils.rotateImage(imageBitmap, ImageUtils.getOrientation(capturedImage));

            imageView.setImageBitmap(imageBitmap);
            //map.setVisibility(View.VISIBLE);  // 최종 데이터가 완성 될때까지 보여지면 안된다. 중간에 Exception이 발생할수도 있으므로

            Location location = gpsInfo.getLocation();
            addr = GMapUtil.getAddress(this, location.getLatitude(), location.getLongitude());
            GMapUtil.createMarker(googleMap, "사진 위치", location.getLatitude(), location.getLongitude());
            addrField.setText(addr);

            capturedImage = ImageUtils.saveImageFile(this ,imageBitmap);

            dataHolder = new DataHolder(location, addr, capturedImage);
			map.setVisibility(View.VISIBLE);  
            return;
        } catch (Exception e) {
            e.printStackTrace();
			throw new Exception("오류 발생");

        }
    }

    private void onGalleryResult(Intent data) throws Exception {
        Uri imageUri = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap imageBitmap;
        String addr;

        try {
            imageUri = data.getData();
            capturedImage = FileUtil.copyByUri(this, imageUri);

            imageBitmap = BitmapFactory.decodeFile(capturedImage.getAbsolutePath(), options);
            imageBitmap = ImageUtils.rotateImage(imageBitmap, ImageUtils.getOrientation(capturedImage));

            Location location = ImageUtils.getLocation(capturedImage);

            if (location == null) {
                Toast.makeText(UpLoadActivity.this, "선택한 사진의 위치정보 확인이 불가능합니다.", Toast.LENGTH_SHORT).show();
				throw new Exception("위치정보 없음");
            }
            imageView.setImageBitmap(imageBitmap);
            //map.setVisibility(View.VISIBLE);

            addr = GMapUtil.getAddress(this, location.getLatitude(), location.getLongitude());
            GMapUtil.createMarker(googleMap, "사진 위치", location.getLatitude(), location.getLongitude());
            addrField.setText(addr);

            capturedImage = ImageUtils.saveImageFile(this ,imageBitmap);

            dataHolder = new DataHolder(location, addr, capturedImage);
			map.setVisibility(View.VISIBLE);
			return;
        } catch (Exception e) {
            e.printStackTrace();
			throw new Exception("오류 발생");
        }

        
    }

    public void onImageClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("사진을 가져올 방법을 선택하세요.");

        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                OnCameraClicked();
            }
        });

        builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int j) {
                OnGalleryClicked();
            }
        });

        builder.show();
    }

    public void OnCameraClicked() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 카메라 어플을 실행 시키기 위해 intent 생성
        Uri fileUri;

        Log.d(TAG, "OnCameraClicked");

        try {
            if (i.resolveActivity(getPackageManager()) != null) {
                capturedImage = FileUtil.createImageFile(this);

                if (capturedImage != null) {
                    fileUri = FileProvider.getUriForFile(this, "com.dgsw.tu.tuapplication.provider", capturedImage);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OnGalleryClicked() {
        Log.d(TAG, "OnGalleryClicked");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }

    public void onSubmitClick(View view) {

        Location location = null;

        try {
            location = (Location) imageView.getTag();

            myVolley.writeEx(dataHolder.image, contents.getText().toString(), (float) dataHolder.location.getLatitude(), (float) dataHolder.location.getLongitude(), dataHolder.addr, myVolley.getUserName(), myVolley.getId(), this, this);
            ShowProgress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        DismissProgress();
        Toast.makeText(this, "Volley 오류로 인해 실패하였습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(String s) {
        DismissProgress();

        try {
            JSONObject json = new JSONObject(s);
            boolean status = json.getBoolean("status");
            String message = json.getString("message");

            if (status && message.equals("001")) {
                //Intent it = new Intent(UpLoadActivity.this, TimeLineActivity.class);
                //startActivity(it);
                finish();
            } else {
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



