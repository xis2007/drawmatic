package com.justinlee.drawmatic;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;
import com.justinlee.drawmatic.objects.Game;
import com.justinlee.drawmatic.objects.OnlineGame;

public interface MainContract {
    interface View extends BaseView<Presenter> {
        void showUpdateRequirementDialog();

        // offline mode
        void showInstructionsPageUi();

        void showOfflineGameSettingsPageUi();

        // online mode
        void showPlayPageUi();

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

        void showTapToNextStepUi();

        // OnBackPressed related
        void showLeaveAppDialog();

        void showLeaveGameDialog(OnlineGame onlineGame);
    }

    interface Presenter extends BasePresenter {
        void checkIfAppUpdateIsRequired();

        void promptUpdateRequirementMessage();

        // offline mode
        void transToInstructionsPage();

        void transToOfflineGameSettingsPage();

        // online mode
        void transToPlayPage();

        void transToOnlineSearchPage();

        void transToGameCreationPage(int roomType);

        void transToOnlineWaitingPage(OnlineGame onlineGame);

        // in-game pages
        void transToSetTopicPage(Game game);

        void transToDrawingPage(Game game);

        void transToGuessingPage(Game game);

        void transToGameResultPage(Game game);

        // settings
        void transToSettingsPage();

        // loading UI
        void isLoading(String loadingHint);

        void isNotLoading();

        void informToShowTapToNextStepUi();

        // OnBackPressed related
        void determineOnBackPressedActions();

        void informToShowLeaveGameDialog(OnlineGame onlineGame);

        // reseting player
        void resetCurrentPlayerToParticipant();
    }


}
