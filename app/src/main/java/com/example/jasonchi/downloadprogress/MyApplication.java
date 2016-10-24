package com.example.jasonchi.downloadprogress;

import android.app.Application;

/**
 * Created by JasonChi on 2016/10/14.
 */

public class MyApplication extends Application {

    private static MyApplication myApplication;

    public static MyApplication getInstance(){
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }
}
