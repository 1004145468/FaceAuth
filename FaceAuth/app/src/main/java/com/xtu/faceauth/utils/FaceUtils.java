package com.xtu.faceauth.utils;

import android.graphics.Bitmap;

import com.xtu.faceauth.config.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/5/16.
 */
public class FaceUtils {

    /**
     * 保存Bitmap至本地
     * @param bmp
     */
    public static void saveBitmapToFile(Bitmap bmp){
        File file = new File(Constants.cachePath);
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            bmp.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
