package com.pythoncat.a04.base;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * @author Administrator
 *         2016/12/11
 *         com.pythoncat.a03
 * @apiNote add stetho
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }


}
