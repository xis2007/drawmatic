package com.justinlee.drawmatic.settings;

public class SettingsPresenter implements SettingsContract.Presenter{
    private SettingsContract.View mSettingsView;

    public SettingsPresenter(SettingsContract.View settingsView) {
        mSettingsView = settingsView;
        mSettingsView.setPresenter(this);
    }

    @Override
    public void loadUserProfile() {

    }

    @Override
    public void start() {

    }
}
