package com.justinlee.drawmatic.objects;

public class OfflineGame extends Game {

    private OfflineSettings mOfflineSettings;

    private boolean mIsPlayersOddNumbered;
    private int mTotalSteps;
    private int mCurrentStep;

    public OfflineGame(OfflineSettings offlineSettings) {
        mOfflineSettings = offlineSettings;

        mIsPlayersOddNumbered = (mOfflineSettings.getNumPlayers() % 2) != 0;
        mTotalSteps = mIsPlayersOddNumbered ? mOfflineSettings.getNumPlayers() : mOfflineSettings.getNumPlayers() + 1;
        mCurrentStep = 1;
    }

    public boolean isPlayersOddumbered() {
        return mIsPlayersOddNumbered;
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

    public OfflineSettings getOfflineSettings() {
        return mOfflineSettings;
    }
}
