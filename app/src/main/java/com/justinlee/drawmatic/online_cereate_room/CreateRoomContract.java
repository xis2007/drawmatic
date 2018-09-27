package com.justinlee.drawmatic.online_cereate_room;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;
import com.justinlee.drawmatic.objects.OnlineSettings;

public interface CreateRoomContract {
    interface View extends BaseView<Presenter> {
        void showCreatedRoomUi();
    }

    interface Presenter extends BasePresenter {
        void createRoom(CreateRoomFragment createRoomFragment, String roomName, int numPlayers, float attemptTime);

        void cancelRoomCreation(CreateRoomFragment createRoomFragment);

        void transToRoomWaitingPage(CreateRoomFragment createRoomFragment, OnlineSettings onlineSettings);

        void informToHideLoadingUi(CreateRoomFragment createRoomFragment);

        void informToShowLoadingUi(CreateRoomFragment createRoomFragment);
    }
}
