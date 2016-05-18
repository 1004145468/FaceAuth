package com.xtu.faceauth.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.xtu.faceauth.R;

import java.io.ByteArrayOutputStream;

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
            .cacheInMemory(true).cacheOnDisc(true)
            .displayer(new FadeInBitmapDisplayer(10)).build();


    /**
     * 加载网络图片并显示在界面上
     * @param imageUrl   网络图片的地址
     * @param imageView  需要展示图片的视图
     */
   public static void Display(String imageUrl,ImageView imageView){
       imageloader.displayImage(imageUrl,imageView, options);
   }

    /*
        通过图片路径获取图片的字节数组
        
     */
    public static byte[] getData(String mFilePath){
        Bitmap mBitmap = BitmapFactory.decodeFile(mFilePath);
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG,85,bas);
        mBitmap.recycle();
        return bas.toByteArray();
    }
}
