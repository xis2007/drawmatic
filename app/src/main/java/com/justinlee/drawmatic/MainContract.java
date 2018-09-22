package com.justinlee.drawmatic;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;
import com.justinlee.drawmatic.objects.GameSettings;
import com.justinlee.drawmatic.objects.OnlineRoom;

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
        void showDrawingPageUi();
        void showGuessingPageUi();

        // settings
        void showSettingsPageUi();
    }

    interface Presenter extends BasePresenter {
        // offline mode
        void transToOfflinePage();
        void transToOfflineGameSettingsPage();

        // online mode
        void transToOnlinePage();
        void transToOnlineSearchPage();
        void transToOnlineRoomCreationPage(int roomType);
        void transToOnlineWaitingPage(OnlineRoom onlineRoom);

        // in-game pages
        void transToSetTopicPage(int gameType, GameSettings gameSettings);
        void transToDrawingPage();
        void transToGuessingPage();

        // settings
        void transToSettingsPage();
    }


}
