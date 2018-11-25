package com.wayne.autoread.work;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.wayne.autoread.RootCmdManager;
import com.wayne.autoread.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * 今日头条
 * {
 * "maxX": 1080,
 * "minY": 300,
 * "maxY": 1600,
 * "readCount": 5,
 * "startCmd": "am start -n com.ss.android.article.news/.activity.MainActivity -a android.intent.action.MAIN -c android.intent.category.LAUNCHER",
 * "preCmd": "input tap 400 280",
 * "bianLiYeMian": {
 * "y": 400,
 * "count": 3,
 * "stepLen": 300
 * },
 * "refreshCmd": "input swipe 300 500 300 1000 300",
 * "readCmd": "input swipe 500 1300 500 600 200",
 * "backCmd": "input tap 60 150"
 * }
 */
public class JrttWorker extends Worker {
    public volatile static boolean isRun = false;
    private int maxX = 1080;
    private int minY = 300;
    private int maxY = 1600;
    private int readCount = 5;
    private String startCmd = "am start -n com.ss.android.article.news/.activity.MainActivity -a android.intent.action.MAIN -c android.intent.category.LAUNCHER";
    private String preCmd = "input tap 400 280";
    private BianLiYeMian bianLiYeMian;
    private String refreshCmd = "input swipe 300 500 300 1000 300";
    private String readCmd = "input swipe 500 1300 500 600 200";
    private String backCmd = "input tap 60 150";

    private Properties properties;
    private String pkgName = "com.ss.android.article.news";

    private int currCount = 0;

    @Override
    public void run() {
        try {
            System.out.println("JrttWorker start run");
            isRun = true;
            currCount = 0;
            RootCmdManager.run("logcat -c");
            RootCmdManager.run(startCmd);
            Utils.startApp(pkgName);
            RootCmdManager.run(preCmd); // 点击热点
            Thread.sleep(5000);
            while (currCount < readCount) {
                traverseScreen();
                RootCmdManager.run(refreshCmd);
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        isRun = false;
    }

    public void traverseScreen() throws IOException, InterruptedException {
        Log.d("wayne", "遍历页面");
        for (int i = 0; i < bianLiYeMian.getCount(); i++) {
            if (!isRun) {
                break;
            }
            // input tap 250 280
            currCount ++;
            int y = bianLiYeMian.getY() + i * bianLiYeMian.getStepLen();
            String cmd = "input tap 500 " + y;
            Log.d("wayne", cmd);
            RootCmdManager.run(cmd);
            Thread.sleep(5000);
            virtualRead();
            if (currCount >= readCount) {
                throw new RuntimeException("已阅读" + currCount + "条");
            }
        }
    }

    /**
     * 模拟阅读
     */
    public void virtualRead() throws InterruptedException, IOException {
        // 每10 秒向上滑动一次。
        int i = 5;
        while (i-- > 0) {
            RootCmdManager.run(readCmd);
        }
        goBack();
    }

    public void goBack() throws InterruptedException, IOException {
        RootCmdManager.run(backCmd);
        Thread.sleep(2000);
    }

    @Override
    public String toString() {
        return "JrttWorker{" +
                "maxX=" + maxX +
                ", minY=" + minY +
                ", maxY=" + maxY +
                ", readCount=" + readCount +
                ", startCmd='" + startCmd + '\'' +
                ", preCmd='" + preCmd + '\'' +
                ", bianLiYeMian=" + bianLiYeMian +
                ", refreshCmd='" + refreshCmd + '\'' +
                ", readCmd='" + readCmd + '\'' +
                ", backCmd='" + backCmd + '\'' +
                ", properties=" + properties +
                ", pkgName='" + pkgName + '\'' +
                ", currCount=" + currCount +
                '}';
    }

    @Override
    public void shutdown() {
        isRun = false;
    }

    public static boolean isIsRun() {
        return isRun;
    }

    public static void setIsRun(boolean isRun) {
        JrttWorker.isRun = isRun;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getStartCmd() {
        return startCmd;
    }

    public void setStartCmd(String startCmd) {
        this.startCmd = startCmd;
    }

    public String getPreCmd() {
        return preCmd;
    }

    public void setPreCmd(String preCmd) {
        this.preCmd = preCmd;
    }

    public BianLiYeMian getBianLiYeMian() {
        return bianLiYeMian;
    }

    public void setBianLiYeMian(BianLiYeMian bianLiYeMian) {
        this.bianLiYeMian = bianLiYeMian;
    }

    public String getRefreshCmd() {
        return refreshCmd;
    }

    public void setRefreshCmd(String refreshCmd) {
        this.refreshCmd = refreshCmd;
    }

    public String getReadCmd() {
        return readCmd;
    }

    public void setReadCmd(String readCmd) {
        this.readCmd = readCmd;
    }

    public String getBackCmd() {
        return backCmd;
    }

    public void setBackCmd(String backCmd) {
        this.backCmd = backCmd;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public int getCurrCount() {
        return currCount;
    }

    public void setCurrCount(int currCount) {
        this.currCount = currCount;
    }
}

/**
 * 遍历页面
 */
class BianLiYeMian {
    private int y = 400;
    private int count = 3;
    private int stepLen = 300;

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStepLen() {
        return stepLen;
    }

    public void setStepLen(int stepLen) {
        this.stepLen = stepLen;
    }

    @Override
    public String toString() {
        return "BianLiYeMian{" +
                "y=" + y +
                ", count=" + count +
                ", stepLen=" + stepLen +
                '}';
    }
}