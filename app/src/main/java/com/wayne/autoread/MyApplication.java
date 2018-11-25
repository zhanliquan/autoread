package com.wayne.autoread;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.wayne.autoread.work.AutoReadThread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;

public class MyApplication extends Application {
    public static String uuid;
    public static String versionName;
    public static String packageName="com.wayne.autoread";
    public static DeviceInfo deviceInfo;
    public static Context AppContext;
    public static AutoReadThread autoReadThread;
    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = getApplicationContext();
        init();
    }

    public static void init() {
        try {
            File uuidFile = new File(AppContext.getFilesDir().getAbsolutePath(), "UUID");
            if (uuidFile.exists()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(uuidFile)));
                String line = reader.readLine();
                reader.close();
                if (line != null && !"".equals(line)) {
                    MyApplication.uuid = line;
                } else {
                    MyApplication.uuid =  UUID.randomUUID().toString();
                }
            } else {
                UUID uuid = UUID.randomUUID();
                if (uuidFile.createNewFile()) {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(uuidFile)));
                    writer.write(uuid.toString());
                    writer.close();
                }
                MyApplication.uuid = uuid.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyApplication.uuid =  UUID.randomUUID().toString();
        }

        PackageManager pm = MyApplication.AppContext.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(packageName, 0);
            versionName = info.versionName;
        } catch(Exception e) {
            e.printStackTrace();
        }

        String apkRoot="chmod 777 "+AppContext.getPackageCodePath();
        MyService.rootCommand(apkRoot);
    }
}
