package com.justinlee.drawmatic.online_room_waiting;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.firabase_operation.FirestoreManager;
import com.justinlee.drawmatic.objects.GameSettings;
import com.justinlee.drawmatic.objects.OfflineSettings;
import com.justinlee.drawmatic.objects.OnlineSettings;
import com.justinlee.drawmatic.objects.Player;

import java.util.ArrayList;

public class OnlineWaitingPresenter implements OnlineWaitingContract.Presenter {
    private MainContract.View mMainView;
    private MainContract.Presenter mMainPresenter;

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
        Player userAdPlayer = ((MainPresenter) ((MainActivity) mMainView).getMainPresenter()).getCurrentPlayer();;
        new FirestoreManager((MainActivity) mMainView).leaveRoom(fragment, mOnlineSettings, userAdPlayer);
    }

    @Override
    public void deleteRoom() {
        new FirestoreManager((MainActivity) mMainView).deleteRoom(this, mOnlineSettings, mOnlineWaitingView);
    }

    @Override
    public void startPlayingOnline(OnlineWaitingFragment fragment) {
        informToShowLoadingUi();
        new FirestoreManager((MainActivity) mMainView).startOnlineGame(fragment, mOnlineSettings);
    }

    @Override
    public void informToHideLoadingUi() {
        mMainView.hideLoadingUi();
    }

    @Override
    public void informToShowLoadingUi() {
        mMainView.showLoadingUi();
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
        new FirestoreManager((MainActivity) mMainView).syncRoomStatus(mOnlineWaitingView, this, mOnlineSettings);
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


    /**
     * ***********************************************************************************
     * Set MainView and MainPresenters to get reference to them
     * ***********************************************************************************
     */
    public void setMainView(MainContract.View mainView) {
        mMainView = mainView;
    }


    public void setMainPresenter(MainPresenter mainPresenter) {
        mMainPresenter = mainPresenter;
    }
}
