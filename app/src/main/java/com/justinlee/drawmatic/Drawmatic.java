package com.justinlee.drawmatic;

import android.app.Application;
import android.content.Context;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.justinlee.drawmatic.constants.FirebaseConstants;

public class Drawmatic extends Application {
    private static Context mContext;
    private static FirebaseFirestore mFirebaseDb;
    private static StorageReference mStorageReference;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        initializeFirebaseDb();
        initializeFirebaseStorage();
    }



    public static Context getAppContext() {
        return mContext;
    }

    public static FirebaseFirestore getmFirebaseDb() {
        return mFirebaseDb;
    }

    public static StorageReference getStorageReference() {
        return mStorageReference;
    }

    private void initializeFirebaseDb() {
        mFirebaseDb = FirebaseFirestore.getInstance();
    }

    private void initializeFirebaseStorage() {
        mStorageReference = FirebaseStorage.getInstance().getReference(FirebaseConstants.Storage.REF_ROOMS);
    }
}
