package com.wayne.autoread;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
                Log.d("wayne ==", "应用启动了");
                WorkThread thread = new WorkThread();
                thread.start();
            }
        }
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!WorkThread.isRun) {
            return super.onStartCommand(intent, flags, startId);
        }
        Log.d("wayne", "fdfdfdfdf");
        if (intent != null) {
            String cmd = intent.getStringExtra("startCmd");
            System.out.println(cmd);
        }
//      rootCommand("input swipe 1000 1000 1000 200 90");
        Log.d("wayne", "开始监听");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sl = "logcat -c && logcat | grep ActivityManager | grep START | grep 'com.ss.android.article.news'";
                registerAppStart(sl, 2);
                Log.d("wayne", "启动应用");
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        rootCommand(CmdConsts.startAppCmd);
        return super.onStartCommand(intent, flags, startId);
    }

    public void execCommand(String command) throws IOException {
        // start the ls command running
        //String[] args =  new String[]{"sh", "-c", command};
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec(command);        //这句话就是shell与高级语言间的调用
        //如果有参数的话可以用另外一个被重载的exec方法
        //实际上这样执行时启动了一个子进程,它没有父进程的控制台
        //也就看不到输出,所以我们需要用输出流来得到shell执行后的输出
        InputStream inputstream = proc.getInputStream();
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
        // read the ls output
        String line = "";
        StringBuilder sb = new StringBuilder(line);
        while ((line = bufferedreader.readLine()) != null) {
            //System.out.println(line);
            sb.append(line);
            sb.append('\n');
        }
        //tv.setText(sb.toString());
        //使用exec执行不会等执行成功以后才返回,它会立即返回
        //所以在某些情况下是很要命的(比如复制文件的时候)
        //使用wairFor()可以等待命令执行完成以后才返回
        try {
            if (proc.waitFor() != 0) {
                System.err.println("exit value = " + proc.exitValue());
            }
        }
        catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    public static boolean rootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Log.d("wayne", "ROOT REE" + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                Log.d("wayne", e.getMessage(), e);
            }
        }
        Log.d("wayne" , "Root SUC");
        return true;
    }

    public void registerAppStart(String command, int what) {
        // logcat -c && logcat | grep ActivityManager | grep START | grep 'com.ss.android.article.news'

        InputStream inputstream = null;
        DataOutputStream os = null;
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("su");

            os = new DataOutputStream(proc.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();

            inputstream = proc.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
            Log.d("wayne", "读出输出");
            String line = null;
            while ((line = bufferedreader.readLine()) != null) {
                Log.d("wayne", line);
                proc.destroy();
                handler.obtainMessage(what).sendToTarget();
                break;
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
    }

}

