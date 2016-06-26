package com.xtu.faceauth.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xtu.faceauth.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/5/16.
 */
public class ImageUtils {

    //创建图片加载器对象 imageloader
    private static ImageLoader imageloader = ImageLoader.getInstance();

    //配置图片加载选项
    private static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.load_image_fail)
            .showImageOnFail(R.mipmap.load_fail_icon)
            .showImageOnLoading(R.mipmap.loading_image)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();


    /**
     * 加载网络图片并显示在界面上
     *
     * @param imageUrl  网络图片的地址
     * @param imageView 需要展示图片的视图
     */
    public static void Display(String imageUrl, ImageView imageView) {
        imageloader.displayImage(imageUrl, imageView, options);
    }

    /*
        通过图片路径获取图片的字节数组
     */
    public static byte[] getData(String mFilePath) {
        Bitmap mBitmap = BitmapFactory.decodeFile(mFilePath);
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 85, bas);
        mBitmap.recycle();
        return bas.toByteArray();
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree 旋转角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle  旋转角度
     * @param bitmap 原图
     * @return bitmap 旋转后的图片
     */
    public static Bitmap rotateImage(int angle, Bitmap bitmap) {
        // 图片旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 得到旋转后的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }
}
