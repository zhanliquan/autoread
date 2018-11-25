package com.wayne.autoread;

import android.app.Application;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.wayne.autoread.mina.MinaService;
import com.wayne.autoread.mina.SessionManager;
import com.wayne.autoread.work.AutoReadThread;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    private AMLisitener amLisitener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                WorkThread.isRun = true;
                MyApplication.autoReadThread = new AutoReadThread();
                MyApplication.autoReadThread.start();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MinaService.class);
                startService(intent);
//                System.out.println(Utils.getApkVersionInfo("com.ss.android.article.news"));
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkThread.isRun = false;
                System.out.println(MyApplication.uuid);
                String deviceInfoJson = JSON.toJSONString(Utils.getCurrDeviceInfo(MainActivity.this));
                System.out.println("wayne:" + deviceInfoJson);
                MyApplication.autoReadThread.running = false;
                MyApplication.autoReadThread = null;
            }
        });

        findViewById(R.id.btn_sent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> msg = new HashMap<String, Object>();
                if (MyApplication.deviceInfo == null) {
                    MyApplication.deviceInfo = Utils.getCurrDeviceInfo(MainActivity.this);
                }
                msg.put("deviceInfo", JSON.toJSONString(MyApplication.deviceInfo));
                msg.put("msgType", "register");
                SessionManager.getInstance().writeToServer(JSON.toJSONString(msg));
                System.out.println(Utils.getScreenOffTime(MainActivity.this));
            }
        });

        Map<String, Object> msg = new HashMap<String, Object>();
        if (MyApplication.deviceInfo == null) {
            MyApplication.deviceInfo = Utils.getCurrDeviceInfo(MainActivity.this);
        }
        msg.put("deviceInfo", JSON.toJSONString(MyApplication.deviceInfo));
        msg.put("msgType", "register");
        SessionManager.getInstance().writeToServer(JSON.toJSONString(msg));
    }

    /**
     * 监听AM的启动应用
     */
    public void register() {
        amLisitener = new AMLisitener();
        amLisitener.doLisitener();
    }

    @Override
    protected void onDestroy() {
        if (amLisitener != null) {
            amLisitener.destory();
        }
        super.onDestroy();
    }
}
