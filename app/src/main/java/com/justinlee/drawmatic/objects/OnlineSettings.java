package com.justinlee.drawmatic.objects;

import java.util.ArrayList;

public class OnlineSettings extends GameSettings {
    private String mRoomName;
    private int mGameMode;
    private int mMaxPlayers;
    private int mCurrentNumPlayers;
    private float mAttemptTime;
    private int mCurrentStep;
    private ArrayList<Player> mPlayers;

    public OnlineSettings() {
    }

    public OnlineSettings(int gameMode, String roomName, int maxPlayers, float attemptTime, ArrayList<Player> players) {
        mGameMode = gameMode;
        mRoomName = roomName;
        mMaxPlayers = maxPlayers;
        mCurrentNumPlayers = 1;
        mAttemptTime = attemptTime;
        mCurrentStep = 1;
        mPlayers = players;
    }

    public int getGameMode() {
        return mGameMode;
    }

    public String getRoomName() {
        return mRoomName;
    }

    public int getMaxPlayers() {
        return mMaxPlayers;
    }

    public int getCurrentNumPlayers() {
        return mCurrentNumPlayers;
    }

    public float getAttemptTime() {
        return mAttemptTime;
    }

    public int getCurrentStep() {
        return mCurrentStep;
    }

    public ArrayList<Player> getPlayers() {
        return mPlayers;
    }
}
