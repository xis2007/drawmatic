package com.justinlee.drawmatic;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;

public interface MainContract {
    interface View extends BaseView<Presenter> {
        // offline mode
        void showOfflinePageUi();

        void showOfflineGameSettingsPageUi();

        // online mode
        void showOnlinePageUi();

        void showOnlineSearchPageUi();

        void showOnlineRoomCreationPageUi();

        void showOnlineGameSettingsPageUi();


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

        void transToOnlineRoomCreationPage();

        void transToOnlineGameSettingsPage();

        // in-game pages
        void transToDrawingPage();

        void transToGuessingPage();

        // settings
        void transToSettingsPage();
    }


}
