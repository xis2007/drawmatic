package com.justinlee.drawmatic.objects;

import java.util.ArrayList;

public class OnlineSettings extends GameSettings {
    private String mRoomName;
    private int mGameMode;
    private int mMaxPlayers;
    private int mCurrentNumPlayers;
    private float mAttemptTime;
    private int mCurrentStep;
    private boolean mIsInGame;
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
        mIsInGame = false;
        mPlayers = players;
    }

    public int getGameMode() {
        return mGameMode;
    }

    public String getRoomName() {
        return mRoomName;
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

    public int getCurrentNumPlayers() {
        return mCurrentNumPlayers;
    }

    public void setCurrentNumPlayers(int currentNumPlayers) {
        mCurrentNumPlayers = currentNumPlayers;
    }

    public float getAttemptTime() {
        return mAttemptTime;
    }

    public void setAttemptTime(float attemptTime) {
        mAttemptTime = attemptTime;
    }

    public int getCurrentStep() {
        return mCurrentStep;
    }

    public void setCurrentStep(int currentStep) {
        mCurrentStep = currentStep;
    }

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
}
