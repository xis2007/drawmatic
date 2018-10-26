package com.justinlee.drawmatic.settings;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;

public interface SettingsContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void updatePlayerName(String newPlayerName);
    }
}
