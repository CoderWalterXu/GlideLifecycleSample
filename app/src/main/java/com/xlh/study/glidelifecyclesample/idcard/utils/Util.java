package com.xlh.study.glidelifecyclesample.idcard.utils;

import android.os.Looper;

/**
 * @author: Watler Xu
 * time:2020/7/30
 * description:
 * version:0.0.1
 */
public class Util {

    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

}
