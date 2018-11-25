package com.wayne.autoread.pojo;

public class ApkInfo {
    private Long apkInfoId;
    private String apkName;
    private String packageName;
    private String versionName;
    private String downloadUrl;

    public Long getApkInfoId() {
        return apkInfoId;
    }

    public void setApkInfoId(Long apkInfoId) {
        this.apkInfoId = apkInfoId;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}