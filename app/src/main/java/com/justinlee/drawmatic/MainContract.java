package com.justinlee.drawmatic;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;
import com.justinlee.drawmatic.objects.Game;
import com.justinlee.drawmatic.objects.OnlineGame;

public interface MainContract {
    interface View extends BaseView<Presenter> {
        // offline mode
        void showOfflinePageUi();
        void showOfflineGameSettingsPageUi();

        // online mode
        void showOnlinePageUi();
        void showOnlineSearchPageUi();
        void showOnlineRoomCreationPageUi();
        void showOnlineWaitingPageUi();

        // in-game pages
        void showSetTopicPageUi();
        void showDrawingPageUi();
        void showGuessingPageUi();
        void showGameResultPageUi();

        // settings
        void showSettingsPageUi();

        // loading Ui
        void showLoadingUi(String loadingHint);
        void hideLoadingUi();

        // OnBackPressed related
        void showLeaveAppDialog();
        void showLeaveGameDialog(OnlineGame onlineGame);
    }

    interface Presenter extends BasePresenter {
        // offline mode
        void transToOfflinePage();
        void transToOfflineGameSettingsPage();

        // online mode
        void transToOnlinePage();
        void transToOnlineSearchPage();
        void transToOnlineRoomCreationPage(int roomType);
        void transToOnlineWaitingPage(OnlineGame onlineGame);

        // in-game pages
        void transToSetTopicPage(OnlineGame onlineGame);
        void transToDrawingPage(Game game);
        void transToGuessingPage(Game game);
        void transToGameResultPage(Game game);

        // settings
        void transToSettingsPage();

        // loading UI
        void isLoading(String loadingHint);
        void isNotLoading();

        // OnBackPressed related
        void determineOnBackPressedActions();
        void informToShowLeaveGameDialog(OnlineGame onlineGame);

        // reseting player
        void resetCurrentPlayerToParticipant();
    }


}
