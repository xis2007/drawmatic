package com.justinlee.drawmatic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.justinlee.drawmatic.bases.BaseActivity;

public class MainActivity extends BaseActivity implements MainContract.View, BottomNavigationView.OnNavigationItemSelectedListener {

    private MainContract.Presenter mMainPresenter;
    private BottomNavigationViewEx mPrimaryNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBottomNavigation();
        initPresenter();
        setListeners();
    }

    @Override
    public void showOfflinePageUi() {
//        mPrimaryNavigation.setCurrentItem(0);
    }

    @Override
    public void showOfflineGameSettingsPageUi() {

    }

    @Override
    public void showOnlinePageUi() {
//        mPrimaryNavigation.setCurrentItem(1);
    }

    @Override
    public void showOnlineSearchPageUi() {

    }

    @Override
    public void showOnlineRoomCreationPageUi() {

    }

    @Override
    public void showOnlineGameSettingsPageUi() {

    }

    @Override
    public void showDrawingPageUi() {

    }

    @Override
    public void showGuessingPageUi() {

    }

    @Override
    public void showSettingsPageUi() {
//        mPrimaryNavigation.setCurrentItem(2);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mMainPresenter = presenter;
    }


    /**
     * ***********************************************************************************
     * Bottom Nav Settings
     * ***********************************************************************************
     */
    private void setBottomNavigation() {
        mPrimaryNavigation = findViewById(R.id.bottom_nav);
        mPrimaryNavigation.enableAnimation(false);
        mPrimaryNavigation.setSelectedItemId(R.id.navigation_online);
        mPrimaryNavigation.setCurrentItem(1);
    }


    /**
     * ***********************************************************************************
     * MainPresenter
     * ***********************************************************************************
     */
    private void initPresenter() {
        mMainPresenter = new MainPresenter(this, getFragmentManager());
        mMainPresenter.start();
    }


    /**
     * ***********************************************************************************
     * Listeners Setup
     * ***********************************************************************************
     */
    private void setListeners() {
        mPrimaryNavigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_offline:
                mMainPresenter.transToOfflinePage();
                return true;
            case R.id.navigation_online:
                mMainPresenter.transToOnlinePage();
                return true;
            case R.id.navigation_settings:
                mMainPresenter.transToSettingsPage();
                return true;
        }
        return false;
    }

    /**
     * ***********************************************************************************
     * Getters and Setters
     * ***********************************************************************************
     */
    public MainContract.Presenter getMainPresenter() {
        return mMainPresenter;
    }
}
