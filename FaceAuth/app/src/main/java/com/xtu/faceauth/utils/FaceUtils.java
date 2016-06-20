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
import java.util.ArrayList;
import java.util.List;

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


    //上传人脸
    public static void detectFace(Context context, final Uri uri, CallBack callback) {
        mCallback = callback;
        mcontext = context;
        new Thread() {
            @Override
            public void run() {
                try {

                    if (tmpBitmap != null) {
                        tmpBitmap.recycle();
                        tmpBitmap = null;
                    }
                    tmpBitmap = BitmapFactory.decodeStream(mcontext.getContentResolver().openInputStream(uri));
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    tmpBitmap.compress(Bitmap.CompressFormat.JPEG, 65, bos);
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

    //绘制趣图图片
    public static void drawImage(int index) {
        if (faceNum <= 0) {
            return;
        }

        //获取原始图片的 宽 和 高
        int width = tmpBitmap.getWidth();
        int height = tmpBitmap.getHeight();

        //创建原始图片的副本，可写
        final Bitmap mBitmap = Bitmap.createBitmap(width, height, tmpBitmap.getConfig());
        Canvas canvas = new Canvas(mBitmap);
        canvas.drawBitmap(tmpBitmap, 0, 0, null);

        //获取两眼间的距离 也就是单眼图片的宽度
        int disX = (int) ((rightX - leftX) / 100 * width);

        //加载原始眼睛素材
        Bitmap initeyeL = BitmapFactory.decodeResource(mcontext.getResources(), SkinAdapter.SKINS[index]);
        Bitmap initeyeR = BitmapFactory.decodeResource(mcontext.getResources(), SkinAdapter.SKINSR[index]);

        //根据素材获取原图片
        Bitmap eyeL = Bitmap.createScaledBitmap(initeyeL, disX, disX, false);
        Bitmap eyeR = Bitmap.createScaledBitmap(initeyeR, disX, disX, false);

        //回收素材
        initeyeL.recycle();
        initeyeR.recycle();

        // canvas.drawbitmap
        canvas.drawBitmap(eyeL, width * leftX / 100 - eyeL.getWidth() / 2, height * leftY / 100 - eyeL.getHeight() / 2, null);
        canvas.drawBitmap(eyeR, width * rightX / 100 - eyeR.getWidth() / 2, height * rightY / 100 - eyeR.getHeight() / 2, null);

        //回收眼睛图片
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

    public interface SearchListener {
        void onFail(Exception e);

        void onSuccess(List<String> aList, double mValue);
    }

    private static double familNum;  //最大相似度

    //相似脸
    public static void searchImage(final Context mContext, final SearchListener mListener) {
        new Thread() {
            @Override
            public void run() {
                try {
                    File mFile = new File(Constants.cropPath);
                    HttpRequests requests = new HttpRequests(Constants.KEY, Constants.SCRETE, true, true);

                    //获取图片的face_id
                    PostParameters parameters = new PostParameters();
                    parameters.setImg(mFile);
                    JSONObject jsonObject = requests.detectionDetect(parameters);
                    JSONArray face = jsonObject.getJSONArray("face");
                    String face_id = face.getJSONObject(0).getString("face_id");

                    //查看相似脸face_id
                    PostParameters searchParams = new PostParameters();
                    searchParams.setKeyFaceId(face_id);
                    searchParams.setFacesetName("9e38e7283c2d4e3ca2e3bd3f9aa739c8");
                    searchParams.setCount(6);
                    JSONObject searchResult = requests.recognitionSearch(searchParams);
                    JSONArray candidate = searchResult.getJSONArray("candidate");
                    int size = candidate.length();
                    if (size <= 0) {
                        throw new Exception();
                    }

                    //查询所有图片的地址

                    final List<String> aUrl = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        String fFaceId = candidate.getJSONObject(i).getString("face_id");
                        if (i == 0) {
                            familNum = candidate.getJSONObject(i).getDouble("similarity");
                        }
                        PostParameters findParamters = new PostParameters();
                        findParamters.setFaceId(fFaceId);
                        JSONObject faceInfo = requests.infoGetFace(findParamters);
                        JSONArray face_info = faceInfo.getJSONArray("face_info");
                        JSONObject jsonObject1 = face_info.getJSONObject(0);
                        String mUrl = jsonObject1.getString("url");
                        aUrl.add(mUrl);
                    }

                    //用户主线程回调
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null) {
                                mListener.onSuccess(aUrl, familNum);
                            }
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null) {
                                mListener.onFail(e);
                            }
                        }
                    });
                }
            }
        }.start();
    }
}
