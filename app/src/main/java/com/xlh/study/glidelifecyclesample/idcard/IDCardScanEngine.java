package com.xlh.study.glidelifecyclesample.idcard;

import android.content.Context;

import com.xlh.study.glidelifecyclesample.idcard.callback.IDCardScanResultCallback;
import com.xlh.study.glidelifecyclesample.idcard.utils.LogUtils;


/**
 * @author: Watler Xu
 * time:2020/7/30
 * description:
 * version:0.0.1
 */
public class IDCardScanEngine {

    Context mContext;
    IIDCardScan mIIDCardScan;
    IDCardScanResultCallback mIdCardScanResultCallback;

    private static volatile IDCardScanEngine instance;

    public static IDCardScanEngine getInstance(Context context) {
        if (instance == null) {
            synchronized (IDCardScanEngine.class) {
                if (instance == null) {
                    instance = new IDCardScanEngine(context);
                }
            }
        }
        return instance;
    }

    public IDCardScanEngine(Context context) {
        this.mContext = context;

        mIIDCardScan = getIDCardScanEngine(context);
    }

    /**
     * 通过策略模式
     * 根据机型，初始化不同的身份证读取实现
     *
     * @param context
     * @return
     */
    public IIDCardScan getIDCardScanEngine(Context context) {

        // 仅示例演示
        return SampleIDCardScan.getInstance(context);

        // 实际应根据model判断机型
        /*if(...){
            return ...;
        }
        return null;*/

    }

    public void startScanIDCard(IDCardScanResultCallback idCardScanResultCallback){

        this.mIdCardScanResultCallback = idCardScanResultCallback;

        mIIDCardScan.startIDCardScan(idCardScanResultCallback);
    }

    public void pauseScanIDCard(){
        LogUtils.e("IDCardScanEngine--pauseScanIDCard");
        mIIDCardScan.pauseScanIDCard();
    }

    public void stopScanIDCard(){
        LogUtils.e("IDCardScanEngine--stopScanIDCard");
        mIIDCardScan.stopIDCardScan();
    }

    public void destoryIDCardScan(){
        LogUtils.e("IDCardScanEngine--destoryIDCardScan");
        mIIDCardScan.destoryIDCardScan();
        instance = null;
    }
}
