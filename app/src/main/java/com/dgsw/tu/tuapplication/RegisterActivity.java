package com.dgsw.tu.tuapplication;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dgsw.tu.tuapplication.utils.FileUtil;
import com.dgsw.tu.tuapplication.utils.ImageUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.navercorp.volleyextensions.volleyer.factory.DefaultRequestQueueFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import static com.navercorp.volleyextensions.volleyer.Volleyer.volleyer;

public class RegisterActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<String>{

    private static final int SELECT_PICTURE = 1;

    private MyVolley myVolley = null;
    ProgressDialog mProgressDialog;

    private ImageButton proFileImg; // private 선언
    private EditText registerName;
    private EditText registerId;
    private EditText registerPw;
    //private Uri         selectedImageUri; 불필요한 변수 삭제
    //private String      selectedImagePath; 불필요한 변수 삭제

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_register);

        myVolley = MyVolley.getinstance(this.getApplicationContext());

        initUI(); // UI 변수 초기화는 한곳으로 몰아 넣는다.
    }

    private void initUI()
    {
        proFileImg = (ImageButton) findViewById(R.id.RegisterProfileImg);
        registerName = (EditText) findViewById(R.id.RegisterName);
        registerId = (EditText) findViewById(R.id.RegisterId);
        registerPw = (EditText) findViewById(R.id.RegisterPw);

        if( Build.VERSION.SDK_INT >= 21) { // 빌드 버젼이 롤리팝 이상일때만 실행 되도록 이미지 원형 처리
            ImageUtils.procRound(proFileImg);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        File imageFile = null;
        Bitmap imageBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 64;

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri imageUri = data.getData();

                proFileImg.setImageURI(imageUri);
                try {

                    imageUri = data.getData();
                    imageFile = FileUtil.copyByUri(this, imageUri);

                    imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
                    imageBitmap = ImageUtils.rotateImage(imageBitmap, ImageUtils.getOrientation(imageFile));

                    proFileImg.setImageBitmap(imageBitmap);
                    imageFile = ImageUtils.saveImageFile(this ,imageBitmap);
                    proFileImg.setTag(imageFile);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onProfileImageClick(View v)
    {
        Log.d("RegisterProfile","onClickSuccess");

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, SELECT_PICTURE);
    }

    public void OnRegisterClick(View v)
    {
        String name = null;
        String id = null;
        String pwd = null;
        File file = null;
        String token = null;

        try
        {
            name = registerName.getText().toString();
            id = registerId.getText().toString();
            pwd = registerPw.getText().toString();
            file = (File)proFileImg.getTag();
            token = FirebaseInstanceId.getInstance().getToken();

            myVolley.registerEx(name,id,pwd, file, token, this, this);  // 에러도 받을 수 있도록 한다.
            ShowProgress();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String recvMsg)
    {
        try {
            JSONObject json = new JSONObject(recvMsg);
            boolean status = json.getBoolean("status");
            String message = json.getString("message");

            DismissProgress();

            if (status && message.equals("001")) {
                System.out.println("회원가입 성공!!");
                Toast.makeText(RegisterActivity.this, "회원가입 성공!!!", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(it);
                finish();
            } else {
                switch (message) {

                    case "002":
                        System.out.println("아이디 없음 회원가입 실패!");
                        Toast.makeText(RegisterActivity.this, "아이디 적어!!!", Toast.LENGTH_SHORT).show();
                        break;
                    case "003":
                        System.out.println("비밀번호 없음 회원가입 실패!");
                        Toast.makeText(RegisterActivity.this, "비밀번호 적어!!!", Toast.LENGTH_SHORT).show();
                        break;
                    case "004":
                        System.out.println("아이디가 중복 회원가입 실패!");
                        Toast.makeText(RegisterActivity.this, "이미 이 아이디는 있어!!!", Toast.LENGTH_SHORT).show();
                        break;
                    case "005":
                        System.out.println("비밀번호가 비밀번호 확인과 다름 회원가입 실패!");
                        Toast.makeText(RegisterActivity.this, "비밀번호 확인좀 다시해봐!!!", Toast.LENGTH_SHORT).show();
                        break;
                    case "006":
                        System.out.println("유저 이름이 없음 회원가입 실패!");
                        Toast.makeText(RegisterActivity.this, "이름이 없어!!!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(RegisterActivity.this, "네트워크 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
        DismissProgress();
    }

    // 팝업 만들기 전에 임시 변수
    private boolean isLogging = false; // 로그인 중일때 사용자 입력을 받지 않기 위해 선언

    private void ShowProgress()
    {

        mProgressDialog = ProgressDialog.show(this,"","회원가입 중입니다.",true);
    }

    private void DismissProgress()
    {
        mProgressDialog.dismiss();
    }
}
