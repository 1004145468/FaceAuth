package com.xtu.faceauth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xtu.faceauth.utils.ImageUtils;

import uk.co.senab.photoview.PhotoView;

public class PhotoActivity extends AppCompatActivity{

    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        PhotoView mImageView = (PhotoView) findViewById(R.id.id_photo);
        mUrl = getIntent().getStringExtra("url");
        ImageUtils.Display(mUrl, mImageView);

    }
}
