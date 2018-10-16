package com.justinlee.drawmatic.objects;

import android.util.Log;

import com.google.firebase.firestore.ServerTimestamp;
import com.justinlee.drawmatic.util.PlayerIdComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class OnlineSettings extends GameSettings {
    private static final String TAG = "playerssssss";

    private String mRoomName;
    private String mCreator;
    private int mGameMode;
    private int mMaxPlayers;
//    private int mCurrentNumPlayers;
    private float mAttemptTime;
//    private int mCurrentStep;
    private boolean mIsInGame;
    private ArrayList<Player> mPlayers;

    @ServerTimestamp private Date timeStamp;

    public OnlineSettings() {
    }

    public OnlineSettings(int gameMode, String roomName, String creator, int maxPlayers, float attemptTime, ArrayList<Player> players) {
        mGameMode = gameMode;
        mRoomName = roomName;
        mCreator = creator;
        mMaxPlayers = maxPlayers;
//        mCurrentNumPlayers = 1;
        mAttemptTime = attemptTime;
//        mCurrentStep = 1;
        mIsInGame = false;
        mPlayers = players;
    }

    public int getGameMode() {
        return mGameMode;
    }

    public String getRoomName() {
        return mRoomName;
    }

    public String getCreator() {
        return mCreator;
    }

    public void setCreator(String creator) {
        mCreator = creator;
    }

    public void setRoomName(String roomName) {
        mRoomName = roomName;
    }

    public int getMaxPlayers() {
        return mMaxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        mMaxPlayers = maxPlayers;
    }

//    public int getCurrentNumPlayers() {
//        return mCurrentNumPlayers;
//    }

//    public void setCurrentNumPlayers(int currentNumPlayers) {
//        mCurrentNumPlayers = currentNumPlayers;
//    }

    public float getAttemptTime() {
        return mAttemptTime;
    }

    public void setAttemptTime(float attemptTime) {
        mAttemptTime = attemptTime;
    }

//    public int getCurrentStep() {
//        return mCurrentStep;
//    }

//    public void setCurrentStep(int currentStep) {
//        mCurrentStep = currentStep;
//    }

    public ArrayList<Player> getPlayers() {
        return mPlayers;
    }

    public boolean isInGame() {
        return mIsInGame;
    }

    public void setInGame(boolean inGame) {
        mIsInGame = inGame;
    }

    public void setPlayers(ArrayList<Player> players) {
        mPlayers = players;
    }

    public void setGameModfe(int gameMode) {
        mGameMode = gameMode;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ArrayList<Player> generateSortedPlayersListById() {
        ArrayList<Player> sortedPlayersList = new ArrayList<>(mPlayers);
        Collections.sort(sortedPlayersList, new PlayerIdComparator());
        if(sortedPlayersList != null) {
            for(int i = 0; i < sortedPlayersList.size(); i++) {
                Log.d(TAG, "generateSortedPlayersListById: player " + i + " = " + sortedPlayersList.get(i).getPlayerName());
            }
        }

        return sortedPlayersList;
    }
}
