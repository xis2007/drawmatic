package com.justinlee.drawmatic.objects;

import java.util.ArrayList;

public class OnlineSettings extends GameSettings {
    private int mGameMode;
    private String mRoomName;
    private int mMaxPlayers;
    private float mAttemptTime;
    private ArrayList<Player> mPlayers;

    public OnlineSettings(int gameMode, String roomName, int maxPlayers, float attemptTime, ArrayList<Player> players) {
        mGameMode = gameMode;
        mRoomName = roomName;
        mMaxPlayers = maxPlayers;
        mAttemptTime = attemptTime;
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

    public float getAttemptTime() {
        return mAttemptTime;
    }

    public ArrayList<Player> getPlayers() {
        return mPlayers;
    }
}
