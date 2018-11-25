package com.wayne.autoread.work;

import com.alibaba.fastjson.JSON;
import com.wayne.autoread.pojo.ApkInfo;

import java.util.HashMap;
import java.util.Map;

public class WorkerFactory {
    public static HashMap<String, Class> workMap = new HashMap<String, Class>();
    static {
        workMap.put("com.ss.android.article.news", JrttWorker.class);
    }

    public static Worker createFromMsg(Map<String, String> msg) {
        if (msg == null) {
            return null;
        }
        Worker worker = null;
        Object apkInfoStr = msg.get("apkInfo");
        if (apkInfoStr != null) {
            ApkInfo apkInfo = JSON.parseObject(apkInfoStr.toString(), ApkInfo.class);
            String packageName = apkInfo.getPackageName();
            String cmd = msg.get("script");
            cmd = cmd.replaceAll("\r\n", "");
            Class cls = workMap.get(packageName);
            if (cmd != null && cls != null){
                worker = (Worker) JSON.parseObject(cmd, cls);
            }
        }
        return worker;
    }
}
