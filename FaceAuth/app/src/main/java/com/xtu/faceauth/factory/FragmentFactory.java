package com.xtu.faceauth.factory;

import android.support.v4.app.Fragment;

import com.xtu.faceauth.fragment.FamilFragment;
import com.xtu.faceauth.fragment.FriendsFragment;
import com.xtu.faceauth.fragment.FuncpicFragment;
import com.xtu.faceauth.fragment.OwnerFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/15.
 */
public class FragmentFactory {

    //管理所有碎片的工厂
    private static Map<Integer, Fragment> mFragments = new HashMap<Integer, Fragment>();

    public static Fragment getFragment(int index) {
        Fragment mFragment = mFragments.get(index);
        if (mFragment == null) {
            //创建该Fragement对象并添加
            switch (index) {
                case 0:
                    mFragment = new FuncpicFragment();
                    break;
                case 1:
                    mFragment = new FamilFragment();
                    break;
                case 2:
                    mFragment = new FriendsFragment();
                    break;
                case 3:
                    mFragment = new OwnerFragment();
                    break;
            }
            mFragments.put(index,mFragment);
        }
        return mFragment;
    }
}
