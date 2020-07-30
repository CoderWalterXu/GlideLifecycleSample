package com.xlh.study.glidelifecyclesample.idcard.callback;

import com.xlh.study.glidelifecyclesample.bean.IDCardInfoBean;

/**
 * @author: Watler Xu
 * time:2020/7/30
 * description: 扫身份证成功失败返回结果的回调接口，给最上调用层的回调
 * version:0.0.1
 */
public interface IDCardScanResultCallback {

    void onIDCardScanSuccess(IDCardInfoBean idCardInfoBean);

    void onIDCardError(String resCode, String resMsg);

}
