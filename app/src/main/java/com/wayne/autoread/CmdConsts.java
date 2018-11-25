package com.wayne.autoread;

public class CmdConsts {
    public static String startAppCmd = "am start -n com.ss.android.article.news/.activity.MainActivity " +
            "-a android.intent.action.MAIN " +
            "-c android.intent.category.LAUNCHER";

    public static String 滑动 = "input swipe 500 1300 500 600 200";

    // 设置-开发者选项-指针位置开关
    public static String 点击 = "input tap 250 280"; // x美50个为一个菜单。 需要按普通的设备适配

    // 退回坐标 80 150
    public static String 退回 = "input tap 60 150";
}
