package com.justinlee.drawmatic.online_room_waiting;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.objects.OnlineSettings;

public class OnlineWaitingPresenter implements OnlineWaitingContract.Presenter {

    private OnlineWaitingContract.View mOnlineWaitingView;

    private OnlineSettings mOnlineSettings;

    public OnlineWaitingPresenter(OnlineWaitingContract.View onlineWaitingView, OnlineSettings onlineRoom) {
        mOnlineWaitingView = onlineWaitingView;
        mOnlineWaitingView.setPresenter(this);

        mOnlineSettings = onlineRoom;
    }

    @Override
    public void leaveRoom(OnlineWaitingFragment fragment) {
        ((MainActivity) fragment.getActivity()).getMainPresenter().transToOnlinePage();
    }

    @Override
    public void startPlayingOnline(OnlineWaitingFragment fragment) {
        ((MainActivity) fragment.getActivity()).getMainPresenter().transToSetTopicPage(mOnlineSettings.getGameMode(), new OnlineGame(mOnlineSettings));
    }

    @Override
    public void start() {
        // TODO search and check for the room created on server
        mOnlineWaitingView.showRoomNameUi(mOnlineSettings.getRoomName());
    }

    public OnlineWaitingContract.View getOnlineWaitingView() {
        return mOnlineWaitingView;
    }

    public OnlineSettings getOnlineSettings() {
        return mOnlineSettings;
    }
}
