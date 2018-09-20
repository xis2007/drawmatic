package com.justinlee.drawmatic.settings;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;

public interface SettingsContract {
    interface View extends BaseView<Presenter> {
        void updateProfile();
    }

    interface Presenter extends BasePresenter {
        void loadUserProfile();
    }
}
