package com.example.frame.utils;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * @author mxs on 2019-06-13
 * Grade 配置的 manifestPlaceholders 获取工具类
 */
public class ManifestUtil {

    /**
     * 在 Application 中获取key
     * @param application 传入 Application
     * @param key 在manifestPlaceholders 中配置的name
     * @return
     */
    public static String getApplicationValue(Application application, String key) {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = application.getPackageManager().getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (applicationInfo == null){
            return "";
        }
        return applicationInfo.metaData.getString(key);
    }
}
