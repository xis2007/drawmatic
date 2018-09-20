package com.justinlee.drawmatic.offline;

public class OfflinePresenter implements OfflineContract.Presenter {
    private OfflineContract.View mOfflineView;

    public OfflinePresenter(OfflineContract.View offlineView) {
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
