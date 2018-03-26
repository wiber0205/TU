package com.dgsw.tu.tuapplication.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by qowns on 2017-07-15.
 */

public class ImageUtils {
    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void procRound(ImageView iv)
    {
        iv.setBackground(new ShapeDrawable(new OvalShape()));
        iv.setClipToOutline(true);
    }

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void procRound2(ImageView iv)
    {
        //iv.setBackground(new ShapeDrawable(new OvalShape()));
        iv.setClipToOutline(true);
    }


    public static Bitmap rotateImage(Bitmap src, float degree) {

        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    public static int getOrientation(File file)
    {
        int degree = 0;
        ExifInterface exif = null;

        try{
            exif =  new ExifInterface(file.getAbsolutePath());
            degree = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

            switch(degree)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;

                default:
                    degree = 0;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return degree;
    }


    public static Location getLocation(File imageFile){
        Location location = new Location("gps");
        try {
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());

            double lat = GMapUtil.convertToLocationByEXIFDMS(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
            double lon = GMapUtil.convertToLocationByEXIFDMS(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));

            location.setLatitude(Double.parseDouble(Double.toString(lat)));
            location.setLongitude(Double.parseDouble(Double.toString(lon)));

            return location;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File saveImageFile(Context context, Bitmap bitmap /*File fromFile*/) throws Exception {
        File toFile = null;

        FileOutputStream fos;
        FileInputStream fis;

        File path = context.getExternalCacheDir(); // 임시파일 저장 경로를 획득한다.

        toFile = new File(path, "" + System.currentTimeMillis() + ".jpeg"); // 임시 파일을 생성한다.

        try {
            // 이 부분이 파일을 복사하는 부분이다.
            // 이 부분이 없으면 CROP할 때 SK W폰에서 다른 이미지를 불러온다.
            // 이유는 모르지만 원본이 소실되거나 DB에서 소실되는 지도 모르겠다.

            //Bitmap bitmap = BitmapFactory.decodeFile(fromFile.getAbsolutePath());
            //bitmap = rotateImage(bitmap, getOrientation(fromFile));
            fos = new FileOutputStream(toFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            //cameraTempFile = Uri.fromFile(toFile);
            //cameraTempFile = FileProvider.getUriForFile(this.getApplicationContext()/*selectPhotoDialogActivity.this,"com.example.test.provider"*/,toFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return toFile;
    }
}
