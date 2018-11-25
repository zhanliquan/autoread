package com.wayne.autoread;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
    public static AppInfo getApkVersionInfo(String pk) {
        AppInfo appInfo = new AppInfo();
        appInfo.setPackageName(pk);
        InputStream inputstream = null;
        DataOutputStream os = null;
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("su");

            os = new DataOutputStream(proc.getOutputStream());
            os.writeBytes( "pm dump "+ pk + " | grep version" + "\n");
            os.writeBytes("exit\n");
            os.flush();

            inputstream = proc.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
            Log.d("wayne", "读出输出");
            String line = null;
            while ((line = bufferedreader.readLine()) != null) {
                Log.d("wayne", line);
                String tempLine = line.trim();
                if (tempLine.startsWith("versionName")){
                    int last = 0;
                    if (tempLine.indexOf(" ") == -1) {
                        last = tempLine.length();
                    }
                    appInfo.setVersionName(tempLine.substring(tempLine.indexOf("=") +1, last));
                }
            }
            Log.d("wayne - line", line);
            proc.waitFor();
        } catch (Exception e) {
            Log.d("wayne", e.getMessage(), e);
        } finally {
            try {
                if (inputstream != null) {
                    inputstream.close();
                }
                if (os != null) {
                    os.close();
                }
                if (proc != null) {
                    proc.destroy();
                }
            } catch (IOException e) {
                Log.d("wayne", e.getMessage(), e);
            }
        }
        return appInfo;
    }

    public static AppInfo getApkVersionInfoByPm(String pk) {
        AppInfo appInfo = new AppInfo();
        appInfo.setPackageName(pk);
        if (pk != null && pk != "") {
            return null;
        }
        PackageManager pm = MyApplication.AppContext.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(pk, 0);
            appInfo.setVersionName(info.versionName);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        return appInfo;
    }

    /**
     * 获取当前的Activity
     * @return
     */
    public static AppInfo getCurrActivity() {
        //
        return null;
    }

    public static DeviceInfo getCurrDeviceInfo(Activity context) {
        if (MyApplication.deviceInfo != null) {
            return MyApplication.deviceInfo;
        }
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setUuid(MyApplication.uuid);
        deviceInfo.setBrand(Build.BRAND);
        deviceInfo.setModel(Build.MODEL);


        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        deviceInfo.setHeightPixels(metrics.heightPixels);
        deviceInfo.setWidthPixels(metrics.widthPixels);

        deviceInfo.setName(Build.DEVICE);

        deviceInfo.setSdk(Build.VERSION.SDK_INT);
        deviceInfo.setRelease(Build.VERSION.RELEASE);

        deviceInfo.setSerialno(Build.SERIAL);
        deviceInfo.setVersionName(MyApplication.versionName);

        MyApplication.deviceInfo = deviceInfo;
        return deviceInfo;
    }


    public static int getScreenOffTime(Context context) {
        int screenOffTime = 0;
        try {
            screenOffTime = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Exception localException) {

        }
        return screenOffTime;
    }

    public static boolean startApp(final String pkg) {
        // logcat -c && logcat | grep ActivityManager | grep START | grep 'com.ss.android.article.news'
        if (pkg == null) {
            return false;
        }
        String cmd = "logcat | grep ActivityManager | grep START | grep '" + pkg + "'";
        InputStream inputstream = null;
        DataOutputStream os = null;
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("su");

            os = new DataOutputStream(proc.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.flush();

            inputstream = proc.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
            Log.d("wayne", "读出输出");
            String line = null;
            while ((line = bufferedreader.readLine()) != null) {
                Log.d("wayne", line);
                proc.destroy();
                return true;
            }
            Log.d("wayne - line", line);
        } catch (Exception e) {
            Log.d("wayne", e.getMessage(), e);
        } finally {
            try {
                if (inputstream != null) {
                    inputstream.close();
                }
                if (os != null) {
                    os.close();
                }
                if (proc != null) {
                    proc.destroy();
                }
            } catch (IOException e) {
                Log.d("wayne", e.getMessage(), e);
            }
        }
        return true;
    }
}
