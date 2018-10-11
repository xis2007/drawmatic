package com.justinlee.drawmatic.instructions;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;

public interface InstructionsContract {
    interface View extends BaseView<Presenter> {
        void showOfflinePageUi();

        void showOfflineSearchPageUi();

        void showOfflineGameSettingsPageUi();
    }

    interface Presenter extends BasePresenter {
        void startPlayingOffline();
    }
}
