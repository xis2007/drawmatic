package com.justinlee.drawmatic;

import android.app.Application;
import android.content.Context;

import com.google.firebase.firestore.FirebaseFirestore;

public class Drawmatic extends Application {
    private static Context mContext;
    private static FirebaseFirestore mFirebaseDb;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        initializeFirebaseDb();
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static FirebaseFirestore getmFirebaseDb() {
        return mFirebaseDb;
    }

    private void initializeFirebaseDb() {
        mFirebaseDb = FirebaseFirestore.getInstance();
    }


}
