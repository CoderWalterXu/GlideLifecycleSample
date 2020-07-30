package com.xlh.study.glidelifecyclesample.idcard.lifecycle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * @author: Watler Xu
 * time:2020/7/30
 * description: 空Fragmnet,监听Activity的生命周期
 * version:0.0.1
 */
public class SupportManagerFragment extends Fragment {

    private LifeCycleListener lifeCycle;

    public SupportManagerFragment(@NonNull LifeCycleListener lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public void addListener(@NonNull LifeCycleListener listener) {
        this.lifeCycle = listener;
    }

    LifeCycleListener getLifeCycle() {
        return lifeCycle;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (lifeCycle != null) {
            lifeCycle.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (lifeCycle != null) {
            lifeCycle.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (lifeCycle != null) {
            lifeCycle.onDestroy();
        }
    }

}
