package com.xlh.study.glidelifecyclesample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.xlh.study.glidelifecyclesample.bean.IDCardInfoBean;
import com.xlh.study.glidelifecyclesample.idcard.IDCardScan;
import com.xlh.study.glidelifecyclesample.idcard.callback.IDCardScanResultCallback;
import com.xlh.study.glidelifecyclesample.idcard.utils.LogUtils;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnStartScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        btnStartScan = findViewById(R.id.btn_start_scan);
        btnStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScanIDCard();
            }
        });
    }

    private void startScanIDCard() {
        IDCardScan.with(this).startScanIDCard(new IDCardScanResultCallback(){
            @Override
            public void onIDCardScanSuccess(IDCardInfoBean idCardInfoBean) {
                StringBuilder succesSb = new StringBuilder();
                succesSb.append(idCardInfoBean.getIdcardName());
                succesSb.append("\r\n");
                succesSb.append(idCardInfoBean.getIdcardNumber());

                Toast.makeText(getApplicationContext(),succesSb,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onIDCardError(String resCode, String resMsg) {
                StringBuilder errorSb = new StringBuilder();
                errorSb.append(resCode);
                errorSb.append("\r\n");
                errorSb.append(resMsg);

                Toast.makeText(getApplicationContext(),errorSb,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e("MainActivity--onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.e("MainActivity--onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e("MainActivity--onDestroy");
    }
}
