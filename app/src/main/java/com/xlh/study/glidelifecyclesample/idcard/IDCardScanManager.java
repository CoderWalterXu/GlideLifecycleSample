package com.xlh.study.glidelifecyclesample.idcard;

import android.app.Application;
import android.content.Context;


import com.xlh.study.glidelifecyclesample.idcard.callback.IDCardScanResultCallback;
import com.xlh.study.glidelifecyclesample.idcard.lifecycle.LifeCycleListener;
import com.xlh.study.glidelifecyclesample.idcard.lifecycle.SupportManagerFragment;
import com.xlh.study.glidelifecyclesample.idcard.utils.Util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * @author: Watler Xu
 * time:2020/7/30
 * description: LifeCycleListener接口实现类，根据生命周期回调，做相应处理
 * version:0.0.1
 */
public class IDCardScanManager implements LifeCycleListener {


    Context mContext;

    IDCardScanEngine mIDCardScanEngine;

    private final String FRAGMENT_ACTIVITY_NAME = "FRAGMENT_ACTIVITY_NAME";


    public void with(Context context){
        if (context == null) {
            throw new IllegalArgumentException("You cannot start a load on a null Context");
        }
        this.mContext = context;

        createIDCardScanEngine(context);
        bind(context);

    }

    private void createIDCardScanEngine(Context context) {
        mIDCardScanEngine = IDCardScanEngine.getInstance(context);
    }

    /**
     * @param context 传入的上下文
     */
    public void bind(Context context) {
        if (Util.isOnMainThread() && !(context instanceof Application)) {
            if (context instanceof FragmentActivity) {
                bindFragmentActivity((FragmentActivity) context);
            }
        }
    }

    private void bindFragmentActivity(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        supportFragmentGet(fragmentManager);
    }

    /**
     * 创建Fragment 并依托于当前Activity(当然Glide做了缓存去重操作)
     * @param fragmentManager
     */
    private void supportFragmentGet(FragmentManager fragmentManager) {

        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_ACTIVITY_NAME);
        if (fragment == null) {
            SupportManagerFragment current = new SupportManagerFragment(this);

            fragmentManager.beginTransaction()
                    .add(current, FRAGMENT_ACTIVITY_NAME)
                    .commitAllowingStateLoss();

        }
    }

    public void startScanIDCard(IDCardScanResultCallback idCardScanResultCallback){
        mIDCardScanEngine.startScanIDCard(idCardScanResultCallback);
    }


    @Override
    public void onPause() {
        mIDCardScanEngine.pauseScanIDCard();
    }

    @Override
    public void onStop() {
        mIDCardScanEngine.stopScanIDCard();
    }

    @Override
    public void onDestroy() {
        mIDCardScanEngine.destoryIDCardScan();
    }


}