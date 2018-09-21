package com.justinlee.drawmatic.online_cereate_room;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;

public interface CreateRoomContract {
    interface View extends BaseView<Presenter> {
        void showCreatedRoomUi();
    }

    interface Presenter extends BasePresenter {
        void sendRoomCreationRequest();

        void cancelRoomCreation(CreateRoomFragment createRoomFragment);

        void transToRoomWaitingPage(CreateRoomFragment createRoomFragment, String roomName, int numPlayers, float attemptTime);
    }
}
