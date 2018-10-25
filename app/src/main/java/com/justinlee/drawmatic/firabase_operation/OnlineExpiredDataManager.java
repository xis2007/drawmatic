package com.justinlee.drawmatic.firabase_operation;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.storage.FirebaseStorage;
import com.justinlee.drawmatic.Drawmatic;
import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.constants.FirebaseConstants;
import com.justinlee.drawmatic.objects.Player;

import java.util.ArrayList;

public class OnlineExpiredDataManager {
    private Context mContext;
    private Player mCurrentPlayer;

    public OnlineExpiredDataManager(Context context) {
        mContext = context;
        mCurrentPlayer = ((MainPresenter) ((MainActivity) mContext).getMainPresenter()).getCurrentPlayer();
    }


    /**
     * Online room related operations (Firebase Firestore)
     */
    public void saveRoomToPref(String roomId) {
        SharedPreferences roomSharedPreferences = mContext.getSharedPreferences(FirebaseConstants.SharedPreferences.ROOMS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = roomSharedPreferences.edit();

        // checks whether there are other existing rooms registered in the sharedPreferences
        // multiple rooms stored in the sharedPreferences will be stored in consecutive integers in String format
        int numberToStoreRoomKey = 0;
        while (!roomSharedPreferences.getString(String.valueOf(numberToStoreRoomKey), String.valueOf(-1)).equals(String.valueOf(-1))) {
            numberToStoreRoomKey++;
        }

        editor.putString(String.valueOf(numberToStoreRoomKey), roomId).apply();
    }

    public ArrayList<String> deleteRoomFromPref() {
        SharedPreferences roomSharedPreferences = mContext.getSharedPreferences(FirebaseConstants.SharedPreferences.ROOMS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = roomSharedPreferences.edit();

        // get all room Ids that exist in the sharedPreferences as ArrayList
        // delete all of them from SharePreferences
        ArrayList<String> roomIdList = new ArrayList<>();
        int totalRoomKeysStored = 0;
        while (!roomSharedPreferences.getString(String.valueOf(totalRoomKeysStored), String.valueOf(-1)).equals(String.valueOf(-1))) {
            editor.remove(String.valueOf(totalRoomKeysStored));
            roomIdList.add(roomSharedPreferences.getString(String.valueOf(totalRoomKeysStored), String.valueOf(-1)));
            totalRoomKeysStored++;
        }
        editor.apply();

        return roomIdList;
    }

    public void deleteExpiredRoom(ArrayList<String> roomIdList) {
        for(String roomId : roomIdList) {
            Drawmatic.getmFirebaseDb()
                    .collection(FirebaseConstants.Firestore.COLLECTION_ROOMS)
                    .document(roomId)
                    .delete();
        }
    }


    /**
     * Online data related operations (Firebase Storage)
     */

    public void saveDataToPref(ArrayList<String> dataStringsList) {
        SharedPreferences roomSharedPreferences = mContext.getSharedPreferences(FirebaseConstants.SharedPreferences.GAME_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = roomSharedPreferences.edit();

        // checks whether there are other existing rooms registered in the sharedPreferences
        // multiple rooms stored in the sharedPreferences will be stored in consecutive integers in String format
        int numberToStoreDataKey = 0;
        while (!roomSharedPreferences.getString(String.valueOf(numberToStoreDataKey), String.valueOf(-1)).equals(String.valueOf(-1))) {
            numberToStoreDataKey++;
        }

        for(String dataString : dataStringsList) {
            editor.putString(String.valueOf(numberToStoreDataKey), dataString);
            numberToStoreDataKey++;
        }
        editor.apply();

    }

    public ArrayList<String> deleteDataFromPref() {
        SharedPreferences roomSharedPreferences = mContext.getSharedPreferences(FirebaseConstants.SharedPreferences.GAME_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = roomSharedPreferences.edit();

        // checks whether there are other existing rooms registered in the sharedpreferences
        // multiple rooms stored in the sharedPreferences will be stored in consecutive integers in String format
        ArrayList<String> dataStringsList = new ArrayList<>();
        int totalDataStoredCount = 0;
        while (!roomSharedPreferences.getString(String.valueOf(totalDataStoredCount), String.valueOf(-1)).equals(String.valueOf(-1))) {
            editor.remove(String.valueOf(totalDataStoredCount));
            dataStringsList.add(roomSharedPreferences.getString(String.valueOf(totalDataStoredCount), String.valueOf(-1)));
            totalDataStoredCount++;
        }

        editor.apply();

        return dataStringsList;
    }

    public void deleteExpiredStorageData(ArrayList<String> dataStringsList) {
        for (String dataString : dataStringsList) {
            FirebaseStorage.getInstance()
                    .getReferenceFromUrl(dataString)
                    .delete();
        }
    }
}
