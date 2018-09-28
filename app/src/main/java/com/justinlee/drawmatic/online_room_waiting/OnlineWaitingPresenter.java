package com.justinlee.drawmatic.online_room_waiting;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.firabase_operation.FirestoreManager;
import com.justinlee.drawmatic.objects.GameSettings;
import com.justinlee.drawmatic.objects.OfflineSettings;
import com.justinlee.drawmatic.objects.OnlineSettings;
import com.justinlee.drawmatic.objects.Player;

import java.util.ArrayList;

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
        Player userAdPlayer = ((MainPresenter) ((MainActivity) fragment.getActivity()).getMainPresenter()).getCurrentPlayer();;
        new FirestoreManager(fragment.getContext()).leaveRoom(fragment, mOnlineSettings, userAdPlayer);
    }

    @Override
    public void deleteRoom(OnlineWaitingFragment fragment) {
        new FirestoreManager(fragment.getContext()).deleteRoom(this, mOnlineSettings, mOnlineWaitingView);
    }

    @Override
    public void startPlayingOnline(OnlineWaitingFragment fragment) {
        informToShowLoadingUi();
        new FirestoreManager(fragment.getContext()).startOnlineGame((OnlineWaitingFragment) mOnlineWaitingView, mOnlineSettings);
    }

    @Override
    public void informToHideLoadingUi() {
        ((MainActivity) ((OnlineWaitingFragment) mOnlineWaitingView).getActivity()).hideLoadingUi();
    }

    @Override
    public void informToShowLoadingUi() {
        ((MainActivity) ((OnlineWaitingFragment) mOnlineWaitingView).getActivity()).showLoadingUi();
    }

    @Override
    public void syncOnlineNewRoomStatus(ArrayList<OnlineSettings> newOnlineSettings) {
        mOnlineSettings = newOnlineSettings.get(0);
        if (mOnlineSettings != null) {
            ((OnlineWaitingFragment) mOnlineWaitingView).getAdapter().swapList(mOnlineSettings.getPlayers());
        }
    }

    @Override
    public void start() {
        // TODO search and check for the room created on server
        mOnlineWaitingView.showRoomNameUi(mOnlineSettings.getRoomName());
        new FirestoreManager(((OnlineWaitingFragment) mOnlineWaitingView).getActivity()).syncRoomStatus(mOnlineWaitingView, this, mOnlineSettings);
    }


    /**
     * ***********************************************************************************
     * Getters and Setters
     * ***********************************************************************************
     */
    public OnlineWaitingContract.View getOnlineWaitingView() {
        return mOnlineWaitingView;
    }

    public OnlineSettings getOnlineSettings() {
        return mOnlineSettings;
    }
}
