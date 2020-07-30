package com.xlh.study.glidelifecyclesample.idcard;

import androidx.fragment.app.FragmentActivity;

/**
 * @author: Watler Xu
 * time:2020/7/30
 * description: 外观模式，供外界调用
 * version:0.0.1
 */
public class IDCardScan {

    public static IDCardScanManager with(FragmentActivity fragmentActivity){
        IDCardScanManager iDCardScanManager = new IDCardScanManager();
        iDCardScanManager.with(fragmentActivity);
        return iDCardScanManager;
    }

}
