package com.justinlee.drawmatic.online;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;
import com.justinlee.drawmatic.objects.OnlineSettings;

import java.util.ArrayList;

public interface OnlineContract {

    interface View extends BaseView<Presenter> {
        void showOnlinePageUi();

        void showOnlineSearchPageUi();

        void hideOnlineSearchPageUi();

        void showOnlineRoomCreationPageUi(int roomType);

        void showOnlineGameSettingsPageUi();
    }

    interface Presenter extends BasePresenter {
        void createRoomForOnlineNormalMode(OnlineFragment onlineFragment);

        void startPlayingOnline();

        void searchForRooms(OnlineFragment onlineFragment, String inputString);

        void informToShowResultRooms(ArrayList<OnlineSettings> onlineRoomSettings);

        void joinSelectedRoom(OnlineSettings onlineSettings);

        void informToTransToOnlineWaitingPage(OnlineSettings onlineSettings);
    }
}
