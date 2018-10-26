package com.justinlee.drawmatic.online.createroom;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;
import com.justinlee.drawmatic.objects.OnlineSettings;

public interface CreateRoomContract {
    interface View extends BaseView<Presenter> {
        void showCreatedRoomUi();

        void promptNameInputAlert();
    }

    interface Presenter extends BasePresenter {
        void createRoom(String roomName, int numPlayers, float attemptTime);

        void cancelRoomCreation();

        void transToRoomWaitingPage(String roomId, OnlineSettings onlineSettings);

        void informToHideLoadingUi();

        void informToShowLoadingUi();

        /**
         * Offline methods
         */
        void startOfflineGame(int numPlayers);
    }
}
