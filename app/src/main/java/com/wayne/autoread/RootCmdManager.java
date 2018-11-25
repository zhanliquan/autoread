package com.wayne.autoread;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;

public class RootCmdManager {
    private static RootCmdManager instance;
    private Process process;
    private DataOutputStream os;

    private RootCmdManager() {

    }

    public static RootCmdManager getInstance() throws IOException {
        if (instance == null) {
            synchronized(RootCmdManager.class) {
                if (instance == null) {
                    instance = new RootCmdManager();
                    instance.init();
                }
            }
        }
        return instance;
    }

    private void init() throws IOException {
        System.out.println("RootCmdManager init");
        process = Runtime.getRuntime().exec("su");
        os = new DataOutputStream(process.getOutputStream());
        System.out.println("os:" + os);
    }

    public static void run(String cmd) throws IOException {
        System.out.println("instance: " + instance);
        if (instance == null) {
            getInstance();
        }
        System.out.println("RootCmdManager run: " + cmd);
        instance.os.writeBytes(cmd + "\n");
        instance.os.flush();
    }

    public synchronized static void destroy() {
        if (instance == null) {
            return;
        }
        try {
            if (instance.os != null) {
                instance.os.close();
            }
            if (instance.process != null) {
                instance.process.destroy();
            }
        } catch (Exception e) {
            Log.d("wayne", e.getMessage(), e);
        }
        instance.process = null;
        instance.os = null;
        instance = null;
    }
}
