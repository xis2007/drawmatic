package com.justinlee.drawmatic.offline;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;

public interface OfflineContract {
    interface View extends BaseView<Presenter> {
        void showOfflinePageUi();

        void showOfflineSearchPageUi();

        void showOfflineGameSettingsPageUi();
    }

    interface Presenter extends BasePresenter {
        void startPlayingOffline();
    }
}
