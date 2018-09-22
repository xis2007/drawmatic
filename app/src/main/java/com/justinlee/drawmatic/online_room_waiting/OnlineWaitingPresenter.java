package com.justinlee.drawmatic.online_room_waiting;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.objects.OnlineRoom;

public class OnlineWaitingPresenter implements OnlineWaitingContract.Presenter {

    private OnlineWaitingContract.View mOnlineWaitingView;

    private OnlineRoom mOnlineRoom;

    public OnlineWaitingPresenter(OnlineWaitingContract.View onlineWaitingView, OnlineRoom onlineRoom) {
        mOnlineWaitingView = onlineWaitingView;
        mOnlineWaitingView.setPresenter(this);

        mOnlineRoom = onlineRoom;
    }

    @Override
    public void leaveRoom(OnlineWaitingFragment fragment) {
        ((MainActivity) fragment.getActivity()).getMainPresenter().transToOnlinePage();
    }

    @Override
    public void startPlayingOnline(OnlineWaitingFragment fragment) {
        ((MainActivity) fragment.getActivity()).getMainPresenter().transToSetTopicPage(mOnlineRoom.getGameMode(), mOnlineRoom);
    }

    @Override
    public void start() {
        // TODO search and check for the room created on server
        mOnlineWaitingView.showRoomNameUi(mOnlineRoom.getRoomName());
    }

    public OnlineWaitingContract.View getOnlineWaitingView() {
        return mOnlineWaitingView;
    }

    public OnlineRoom getOnlineRoom() {
        return mOnlineRoom;
    }
}
