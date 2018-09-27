package com.justinlee.drawmatic.online;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;

public interface OnlineContract {

    interface View extends BaseView<Presenter> {
        void showOnlinePageUi();

        void showOnlineSearchPageUi();

        void showOnlineRoomCreationPageUi(int roomType);

        void showOnlineGameSettingsPageUi();
    }

    interface Presenter extends BasePresenter {
        void goToOnlineNormalMode(OnlineFragment onlineFragment);

        void startPlayingOnline();
    }
}
