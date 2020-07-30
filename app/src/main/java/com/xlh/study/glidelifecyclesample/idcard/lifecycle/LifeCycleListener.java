package com.xlh.study.glidelifecyclesample.idcard.lifecycle;

/**
 * @author: Watler Xu
 * time:2020/7/30
 * description: 需要监听的生命周期
 * version:0.0.1
 */
public interface LifeCycleListener {

    void onPause();

    void onStop();

    void onDestroy();

}
