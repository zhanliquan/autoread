package com.wayne.autoread.work;

import android.util.Log;

import com.wayne.autoread.CmdConsts;
import com.wayne.autoread.MyService;

/**
 * 今日头条
 * maxX=1080
 * minY=300
 * maxY=1600
 * 启动应用=am start -n com.ss.android.article.news/.activity.MainActivity -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
 * 启动后操作=input tap 400 280
 * 阅读次数=3
 * 刷新一屏=input swipe 300 500 300 1000 300
 * 遍历页面={"x":280, "y": 400, "step":3, "stepLen": 300}
 * 模拟阅读={"cmd":"input swipe 500 1300 500 600 200", "count":5}
 * 退回={cmd:"input tap 60 150"}
 */
public class JrttWorkThread implements Runnable {
    private int maxX=1080;
    private int minY=300;
    private int maxY=1600;
    private String 启动应用 = "am start -n com.ss.android.article.news/.activity.MainActivity -a android.intent.action.MAIN -c android.intent.category.LAUNCHER";
    private String 启动后操作 = "input tap 400 280";
    private String 阅读次数 = "";
    private String 刷新一屏 = "input swipe 300 500 300 1000 300";
    private String 模拟阅读 = "input swipe 500 1300 500 600 300";
    private String 退回 = "input tap 60 150";


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

    /**
     * 遍历页面
     */
    class BianLiYeMian {
        private int x = 280;
        private int y = 400;
        private int step = 3;
        private int stepLen = 300;
    }
}
