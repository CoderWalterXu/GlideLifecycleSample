package com.xlh.study.glidelifecyclesample.idcard;


import com.xlh.study.glidelifecyclesample.idcard.callback.IDCardScanResultCallback;

/**
 * @author: Watler Xu
 * time:2020/7/30
 * description:对各种身份证扫码的最高层抽象
 * version:0.0.1
 */
public interface IIDCardScan {

    void startIDCardScan(IDCardScanResultCallback idCardScanResultCallback);

    void pauseScanIDCard();

    void stopIDCardScan();

    void destoryIDCardScan();
}