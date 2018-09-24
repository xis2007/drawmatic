package com.justinlee.drawmatic.objects;

public class OnlineGame extends Game {
    private OnlineSettings mOnlineSettings;

    private float mSetTopicTimeAllowed;
    private float mDrawingAndGuessingTimeAllowed;

    private boolean mIsPlayersOddumbered;
    private int mTotalStpes;
    private int mCurrentStep;

    public OnlineGame(OnlineSettings onlineSettings) {
        mOnlineSettings = onlineSettings;

        mSetTopicTimeAllowed = 0.5f;
        mDrawingAndGuessingTimeAllowed = mOnlineSettings.getAttemptTime();

        mIsPlayersOddumbered = (mOnlineSettings.getPlayers().size() % 2) != 0;
        mTotalStpes = mIsPlayersOddumbered ? mOnlineSettings.getPlayers().size() : mOnlineSettings.getPlayers().size() + 1;
        mCurrentStep = 1;
    }

    public OnlineSettings getOnlineSettings() {
        return mOnlineSettings;
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

    public int getTotalStpes() {
        return mTotalStpes;
    }

    public int getCurrentStep() {
        return mCurrentStep;
    }
}
