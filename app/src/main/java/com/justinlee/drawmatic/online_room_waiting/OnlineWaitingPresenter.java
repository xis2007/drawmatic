package com.justinlee.drawmatic.online_room_waiting;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.firabase_operation.FirestoreManager;
import com.justinlee.drawmatic.objects.GameSettings;
import com.justinlee.drawmatic.objects.OfflineSettings;
import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.objects.OnlineSettings;

public class OnlineWaitingPresenter implements OnlineWaitingContract.Presenter {

    private OnlineWaitingContract.View mOnlineWaitingView;

    private OnlineSettings mOnlineSettings;
    private OfflineSettings mOfflineSettings;

    public OnlineWaitingPresenter(OnlineWaitingContract.View onlineWaitingView, GameSettings onlineRoom) {
        mOnlineWaitingView = onlineWaitingView;
        mOnlineWaitingView.setPresenter(this);

        if (onlineRoom instanceof OnlineSettings) {
            mOnlineSettings = (OnlineSettings) onlineRoom;
            mOfflineSettings = null;
        } else {
            mOnlineSettings = null;
            mOfflineSettings = (OfflineSettings) onlineRoom;
        }
    }

    @Override
    public void leaveRoom(final OnlineWaitingFragment fragment) {
        new FirestoreManager(fragment.getContext()).deleteRoom(this, mOnlineSettings, mOnlineWaitingView);
    }

    @Override
    public void startPlayingOnline(OnlineWaitingFragment fragment) {
        ((MainActivity) fragment.getActivity()).getMainPresenter().transToSetTopicPage(mOnlineSettings.getGameMode(), new OnlineGame(mOnlineSettings));
    }

    @Override
    public void informToHideLoadingUi(OnlineWaitingFragment fragment) {
        ((MainActivity) fragment.getActivity()).hideLoadingUi();
    }

    @Override
    public void informToShowLoadingUi(OnlineWaitingFragment fragment) {
        ((MainActivity) fragment.getActivity()).showLoadingUi();
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
