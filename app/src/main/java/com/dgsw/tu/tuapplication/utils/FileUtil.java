package com.dgsw.tu.tuapplication.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2017-12-16.
 */

public class FileUtil {
    public static File copyByUri(Context context, Uri uri)throws Exception
    {
        try {

            if(uri == null)
                return null;

            File toFile = null;
            File path = context.getExternalCacheDir(); // 임시파일 저장 경로를 획득한다.

            toFile = new File(path, "" + System.currentTimeMillis() + ".jpeg");


            FileInputStream fis = (FileInputStream) context.getContentResolver().openInputStream(uri);
            FileOutputStream fos = new FileOutputStream(toFile);

            FileChannel fcin = fis.getChannel();
            FileChannel fcout = fos.getChannel();

            long size = fcin.size();

            fcin.transferTo(0, size, fcout);

            fcout.close();
            fcin.close();
            fos.close();
            fis.close();

            return toFile;


        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    public static File createImageFile(Context context) throws IOException {
        // uri가 null일경우 null반환
        String imageFileName = ""+System.currentTimeMillis();
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }
}
