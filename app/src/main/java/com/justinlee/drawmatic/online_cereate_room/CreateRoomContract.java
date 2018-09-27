package com.justinlee.drawmatic.online_cereate_room;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;
import com.justinlee.drawmatic.objects.OnlineSettings;

public interface CreateRoomContract {
    interface View extends BaseView<Presenter> {
        void showCreatedRoomUi();

        void showLoadingSign();

        void hideLoadingSign();
    }

    interface Presenter extends BasePresenter {
        void createRoom(CreateRoomFragment createRoomFragment, String roomName, int numPlayers, float attemptTime);

        void cancelRoomCreation(CreateRoomFragment createRoomFragment);

        void transToRoomWaitingPage(CreateRoomFragment createRoomFragment, OnlineSettings onlineSettings);
    }
}
