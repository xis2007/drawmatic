package com.justinlee.drawmatic.online_room_waiting;

public class OnlineWaitingPresenter implements OnlineWaitingContract.Presenter {
    private OnlineWaitingContract.View mOnlineWaitingView;

    private String mRoomName;
    private int mNumPlayers;
    private float mAttemptTime;

    public OnlineWaitingPresenter(OnlineWaitingContract.View onlineWaitingView, String roomName, int numPlayers, float attemptTime) {
        mOnlineWaitingView = onlineWaitingView;
        mOnlineWaitingView.setPresenter(this);

        mRoomName = roomName;
        mNumPlayers = numPlayers;
        mAttemptTime = attemptTime;
    }

    @Override
    public void startPlayingOnline() {

    }

    @Override
    public void start() {

    }
}
