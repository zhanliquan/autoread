package com.wayne.autoread;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AMLisitener {
    private Process proc;

    public void doLisitener() {
        InputStream inputstream = null;
        DataOutputStream os = null;
        try {
            proc = Runtime.getRuntime().exec("su");

            os = new DataOutputStream(proc.getOutputStream());
            os.writeBytes( "logcat | grep \"ActivityManager: START\"" + "\n");
            os.writeBytes("exit\n");
            os.flush();

            inputstream = proc.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            final BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
            Thread l = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String line = null;
                        while ((line = bufferedreader.readLine()) != null) {
                            String tempLine = line.trim();
                            int start = tempLine.indexOf("cmp=");
                            if (start > 0) {
                                String cmp = tempLine.substring(start + 4, tempLine.indexOf(" ", start));
                                System.out.println(cmp);
                                System.out.println(getPkn(cmp));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            l.start();
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

    public void destory () {
        if (proc != null) {
            proc.destroy();
        }

    }

    public static String getPkn(String temp) {
        int i = temp.indexOf('/');
        if (i > 0) {
            return temp.substring(0, i);
        }
        return null;
    }
}
