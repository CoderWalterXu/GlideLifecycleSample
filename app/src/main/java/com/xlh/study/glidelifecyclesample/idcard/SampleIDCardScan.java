package com.xlh.study.glidelifecyclesample.idcard;

import android.content.Context;

import com.xlh.study.glidelifecyclesample.bean.IDCardInfoBean;
import com.xlh.study.glidelifecyclesample.idcard.callback.IDCardScanResultCallback;
import com.xlh.study.glidelifecyclesample.idcard.utils.LogUtils;


/**
 * @author: Watler Xu
 * time:2020/7/30
 * description:
 * version:0.0.1
 */
public class SampleIDCardScan implements IIDCardScan {

    private volatile static SampleIDCardScan instance;

    IDCardScanResultCallback listener;

    Context mContext;

    public static SampleIDCardScan getInstance(Context context) {
        if (instance == null) {
            synchronized (SampleIDCardScan.class) {
                if (instance == null) {
                    instance = new SampleIDCardScan(context);
                }
            }
        }
        return instance;
    }

    public SampleIDCardScan(Context context) {
        this.mContext = context;
        // 初始化

    }

    @Override
    public void startIDCardScan(IDCardScanResultCallback idCardScanResultCallback) {
        LogUtils.e("SampleIDCardScan--startIDCardScan");
        this.listener = idCardScanResultCallback;

        handleScanResult();


    }

    @Override
    public void pauseScanIDCard() {
        LogUtils.e("SampleIDCardScan--pauseScanIDCard");
    }

    private void handleScanResult() {
        // 示例代码
        if(true){
            IDCardInfoBean idCardInfoBean = new IDCardInfoBean();
            idCardInfoBean.setIdcardName("示例姓名哈哈");
            idCardInfoBean.setIdcardNumber("示例身份证12343434");

            listener.onIDCardScanSuccess(idCardInfoBean);
        }else{
            listener.onIDCardError("101","识别错误");
        }
    }

    @Override
    public void stopIDCardScan() {
        LogUtils.e("SampleIDCardScan--stopIDCardScan");
    }

    @Override
    public void destoryIDCardScan() {
        LogUtils.e("SampleIDCardScan--destoryIDCardScan");
    }
}
