package com.justinlee.drawmatic.online;

public class OnlinePresenter implements OnlineContract.Presenter {
    private static final String TAG = "justin";

    OnlineContract.View mOnlineView;

    public OnlinePresenter(OnlineContract.View onlineView) {
        mOnlineView = onlineView;
        mOnlineView.setPresenter(this);
    }

    @Override
    public void startPlayingOnline() {

    }

    @Override
    public void start() {

    }
}
