package com.app.mybook.util;

import android.content.Context;

/**
 * Created by 王海 on 2015/4/15.
 */
public class MyApplication extends com.activeandroid.app.Application {
    private static Context context;
    @Override public void onCreate() { super.onCreate();context = getApplicationContext(); }
    public static Context getContext() { return context; }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public MyApplication() {
        super();
    }
}
