package com.justinlee.drawmatic;

import android.app.Application;
import android.content.Context;

public class Drawmatic extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getAppContext() {
        return mContext;
    }
}
