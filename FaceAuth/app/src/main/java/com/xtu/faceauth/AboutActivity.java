package com.xtu.faceauth;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.xtu.faceauth.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();
    }

    private void initViews() {
        TextView titleView = (TextView) findViewById(R.id.id_activity_title);
        titleView.setText("关于图言");
        TextView versionView = (TextView) findViewById(R.id.id_version);
        versionView.setText(getVersionName());
    }

    public String getVersionName() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
