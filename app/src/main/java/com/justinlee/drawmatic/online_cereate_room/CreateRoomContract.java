package com.justinlee.drawmatic.online_cereate_room;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;
import com.justinlee.drawmatic.objects.OnlineSettings;

public interface CreateRoomContract {
    interface View extends BaseView<Presenter> {
        void showCreatedRoomUi();

        void promptNameInputAlert();

        void promptRoomExistingAlert();
    }

    interface Presenter extends BasePresenter {
        void checkForRoomExistance(String roomName, int numPlayers, float attemptTime);

        void createRoom(String roomName, int numPlayers, float attemptTime);

        void cancelRoomCreation();

        void transToRoomWaitingPage(String roomId, OnlineSettings onlineSettings);

        void informToHideLoadingUi();

        void informToShowLoadingUi();

        void informRoomExists(OnlineSettings onlineSettings);
    }
}
