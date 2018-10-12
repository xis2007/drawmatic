package com.justinlee.drawmatic.play;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;
import com.justinlee.drawmatic.objects.OnlineGame;

import java.util.ArrayList;

public interface PlayContract {

    interface View extends BaseView<Presenter> {
        void showOnlinePageUi();

        void showGameSelectionPageUi();

        void showSearchGamesPageUi();

        void hideOnlineSearchPageUi();

        void showOnlineRoomCreationPageUi(int roomType);

        void showOnlineGameSettingsPageUi();

        void showNoRoomsResultFoundMessage();

        void showRoomIsInGameMessage();
    }

    interface Presenter extends BasePresenter {
        void createRoomForOnlineNormalMode();

        void informToTransToSearchRoomsPage();

        void searchForRooms(PlayFragment playFragment, String inputString);

        void informToShowResultRooms(ArrayList<OnlineGame> onlineGamesList);

        void joinSelectedRoom(OnlineGame onlineGame);

        void informToTransToOnlineWaitingPage(OnlineGame onlineGame);

        void informToShowNoRoomsResultFoundMessage();

        void informToShowRoomIsInGameMessage();
    }
}
