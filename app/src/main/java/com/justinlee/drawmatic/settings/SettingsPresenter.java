package com.justinlee.drawmatic.settings;

import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.user.UserManager;

public class SettingsPresenter implements SettingsContract.Presenter {
    private MainContract.View mMainView;
    private MainContract.Presenter mMainPresenter;
    private SettingsContract.View mSettingsView;

    public SettingsPresenter(SettingsContract.View settingsView) {
        mSettingsView = settingsView;
        mSettingsView.setPresenter(this);
    }

    @Override
    public void updatePlayerName(String newPlayerName) {
        UserManager.getInstance().setUserName(newPlayerName);
    }

    @Override
    public void start() {

    }

    /**
     * ***********************************************************************************
     * Set MainView and MainPresenters to get reference to them
     * ***********************************************************************************
     */
    public void setMainView(MainContract.View mainView) {
        mMainView = mainView;
    }


    public void setMainPresenter(MainPresenter mainPresenter) {
        mMainPresenter = mainPresenter;
    }

    /**
     * ***********************************************************************************
     * Getters and Setters
     * ***********************************************************************************
     */
    public MainContract.View getMainView() {
        return mMainView;
    }

    public MainContract.Presenter getMainPresenter() {
        return mMainPresenter;
    }
}
