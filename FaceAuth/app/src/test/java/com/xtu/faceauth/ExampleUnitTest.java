package com.xtu.faceauth;

import android.util.Log;

import com.xtu.faceauth.utils.BmobUtils;

import org.junit.Test;

import cn.bmob.v3.listener.SaveListener;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        BmobUtils.startLogin("1000", "1004145468", new SaveListener() {
            @Override
            public void onSuccess() {
                Log.d("", "onSuccess: ");
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}