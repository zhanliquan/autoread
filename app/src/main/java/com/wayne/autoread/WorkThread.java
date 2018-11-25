package com.wayne.autoread;

import android.util.Log;

public class WorkThread extends Thread {

    public volatile static boolean isRun = false;

    @Override
    public void run() {
        // 开始读的遍历
        try {
            Thread.sleep(5000);
            开始遍历Title();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void 开始遍历Title() throws Exception{
        //遍历横title
        Log.d("wayne", "开始遍历Title");
        for (int x = 250; x < 1000; x += 50) {
            if (!isRun) { break; }
            // input tap 250 280
            String cmd = "input tap " + x + " 280";
            Log.d("wayne", cmd);
            MyService.rootCommand(cmd);
            // 等10是 开始遍历页面
            Thread.sleep(5000);
            遍历页面();
        }
    }

    public void 遍历页面() throws Exception {
        Log.d("wayne", "遍历页面");
        for (int y = 400; y < 1000; y += 300) {
            if (!isRun) { break; }
            // input tap 250 280
            String cmd = "input tap 280 " + y;
            Log.d("wayne", cmd);
            // 点击页面
            MyService.rootCommand(cmd);
            Thread.sleep(5000);

            // 等10是 开始模拟阅读
            开始模拟阅读();
        }
    }

    public void 开始模拟阅读() throws Exception{
        // 每10 秒向上滑动一次。
        int i = 5;
        while (i-- > 0) {
            MyService.rootCommand(CmdConsts.滑动);
        }
        退出();
    }

    public void 退出() throws Exception {
        MyService.rootCommand(CmdConsts.退回);
        Thread.sleep(2000);
    }
}
