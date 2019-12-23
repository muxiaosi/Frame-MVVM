package com.example.frame;

import android.app.Application;
import com.example.frame.widge.CustomToast;

/**
 * @author mxs
 * @date 2019-06-26 17:58
 * @description TODO
 */
public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CustomToast.getInstance(this);
        instance = this;
    }
}
