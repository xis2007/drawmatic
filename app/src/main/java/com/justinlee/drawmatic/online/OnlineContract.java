package com.justinlee.drawmatic.online;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;
import com.justinlee.drawmatic.objects.OnlineGame;

import java.util.ArrayList;

public interface OnlineContract {

    interface View extends BaseView<Presenter> {
        void showOnlinePageUi();

        void showOnlineSearchPageUi();

        void hideOnlineSearchPageUi();

        void showOnlineRoomCreationPageUi(int roomType);

        void showOnlineGameSettingsPageUi();

        void showNoRoomsResultFoundMessage();

        void showRoomIsInGameMessage();
    }

    interface Presenter extends BasePresenter {
        void createRoomForOnlineNormalMode(OnlineFragment onlineFragment);

        void startPlayingOnline();

        void searchForRooms(OnlineFragment onlineFragment, String inputString);

        void informToShowResultRooms(ArrayList<OnlineGame> onlineGamesList);

        void joinSelectedRoom(OnlineGame onlineGame);

        void informToTransToOnlineWaitingPage(OnlineGame onlineGame);

        void informToShowNoRoomsResultFoundMessage();

        void informToShowRoomIsInGameMessage();
    }
}
