package com.justinlee.drawmatic.instructions;

public class InstructionsPresenter implements InstructionsContract.Presenter {
    private InstructionsContract.View mOfflineView;

    public InstructionsPresenter(InstructionsContract.View offlineView) {
        mOfflineView = offlineView;
        mOfflineView.setPresenter(this);
    }

    @Override
    public void startPlayingOffline() {

    }

    @Override
    public void start() {

    }
}
