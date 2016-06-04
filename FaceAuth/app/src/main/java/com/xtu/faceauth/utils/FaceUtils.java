package com.xtu.faceauth.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;

import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.xtu.faceauth.adapter.SkinAdapter;
import com.xtu.faceauth.config.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/5/16.
 */
public class FaceUtils {

    public interface CallBack {
        void onSuccess(Bitmap bitmap);

        void onFail(Exception e);
    }

    /**
     * 保存Bitmap至本地
     *
     * @param bmp
     */
    public static void saveBitmapToFile(Bitmap bmp) {
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


    private static int faceNum;
    private static float rightY;
    private static float rightX;
    private static float leftY;
    private static float leftX;
    private static Bitmap tmpBitmap;
    private static CallBack mCallback;
    private static Context mcontext;


    public static void detectFace(Context context, final Uri uri,CallBack callback) {
        mCallback = callback;
        mcontext = context;
        new Thread() {
            @Override
            public void run() {
                try {
                    tmpBitmap = BitmapFactory.decodeStream(mcontext.getContentResolver().openInputStream(uri));
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    tmpBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                    byte[] mImageDatas = bos.toByteArray();
                    HttpRequests requests = new HttpRequests(Constants.KEY, Constants.SCRETE, true, true);
                    PostParameters parameters = new PostParameters();
                    parameters.setImg(mImageDatas);
                    JSONObject jsonObject = requests.detectionDetect(parameters);

                    JSONArray face = jsonObject.getJSONArray("face");
                    faceNum = face.length();

                    if (faceNum <= 0) {
                        ((Activity) mcontext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ProgressbarUtils.hideDialog();
                                ToastUtils.show("未检测到人脸！");
                            }
                        });
                        return;
                    }

                    JSONObject jsonObject1 = face.getJSONObject(0);
                    JSONObject position = jsonObject1.getJSONObject("position");
                    JSONObject eye_left = position.getJSONObject("eye_left");
                    JSONObject eye_right = position.getJSONObject("eye_right");
                    leftX = (float) eye_left.getDouble("x");
                    leftY = (float) eye_left.getDouble("y");
                    rightX = (float) eye_right.getDouble("x");
                    rightY = (float) eye_right.getDouble("y");

                    drawImage(0);

                } catch (final Exception e) {
                    e.printStackTrace();
                    ((Activity) mcontext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ProgressbarUtils.hideDialog();
                            if (mCallback != null) {
                                mCallback.onFail(e);
                            }
                        }
                    });
                }
            }
        }.start();
    }

    public static void drawImage(int index) {
        if(faceNum<=0){
            return;
        }


        Bitmap eyeL = BitmapFactory.decodeResource(mcontext.getResources(), SkinAdapter.SKINS[index]);
        Bitmap eyeR = BitmapFactory.decodeResource(mcontext.getResources(), SkinAdapter.SKINSR[index]);
        int width = tmpBitmap.getWidth();
        int height = tmpBitmap.getHeight();
        final Bitmap mBitmap = Bitmap.createBitmap(width, height, tmpBitmap.getConfig());
        Canvas canvas = new Canvas(mBitmap);
        canvas.drawBitmap(tmpBitmap, 0, 0, null);
       // canvas.drawbitmap
        canvas.drawBitmap(eyeL, width * leftX / 100 - eyeL.getWidth() / 2, height * leftY / 100 - eyeL.getHeight() / 2, null);
        canvas.drawBitmap(eyeR, width * rightX / 100 - eyeR.getWidth() / 2, height * rightY / 100 - eyeR.getHeight() / 2, null);
        eyeL.recycle();
        eyeR.recycle();
        //更新原图
        ((Activity) mcontext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgressbarUtils.hideDialog();
                if (mCallback != null && mBitmap != null) {
                    mCallback.onSuccess(mBitmap);
                }
            }
        });
    }
}
