package com.justinlee.drawmatic.objects;

import java.util.ArrayList;

public class OnlineGame extends Game {
    private String mRoomId;
    private OnlineSettings mOnlineSettings;

    private float mSetTopicTimeAllowed;
    private float mDrawingAndGuessingTimeAllowed;

    private boolean mIsPlayersOddumbered;
    private int mTotalSteps;
    private int mCurrentStep;

    private ArrayList<String> mImageUrlStrings;

    public OnlineGame(String roomId, OnlineSettings onlineSettings) {
        mRoomId = roomId;
        mOnlineSettings = onlineSettings;

        mSetTopicTimeAllowed = 1f;
        mDrawingAndGuessingTimeAllowed = mOnlineSettings.getAttemptTime();

        mIsPlayersOddumbered = (mOnlineSettings.getPlayers().size() % 2) != 0;
        mTotalSteps = mIsPlayersOddumbered ? mOnlineSettings.getPlayers().size() : mOnlineSettings.getPlayers().size() + 1;
        mCurrentStep = 1;

        mImageUrlStrings = new ArrayList<>();
    }

    public String getRoomId() {
        return mRoomId;
    }

    public void setRoomId(String roomId) {
        mRoomId = roomId;
    }

    public OnlineSettings getOnlineSettings() {
        return mOnlineSettings;
    }

    public void setOnlineSettings(OnlineSettings onlineSettings) {
        mOnlineSettings = onlineSettings;
    }

    public float getSetTopicTimeAllowed() {
        return mSetTopicTimeAllowed;
    }

    public float getDrawingAndGuessingTimeAllowed() {
        return mDrawingAndGuessingTimeAllowed;
    }

    public boolean isPlayersOddumbered() {
        return mIsPlayersOddumbered;
    }

    public int getTotalSteps() {
        return mTotalSteps;
    }

    public int getCurrentStep() {
        return mCurrentStep;
    }

    public void increamentCurrentStep() {
        mCurrentStep++;
    }

    public ArrayList<String> getImageUrlStrings() {
        return mImageUrlStrings;
    }
}
