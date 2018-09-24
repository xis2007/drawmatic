package com.justinlee.drawmatic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.justinlee.drawmatic.bases.BaseActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends BaseActivity implements MainContract.View, BottomNavigationView.OnNavigationItemSelectedListener {

    private MainContract.Presenter mMainPresenter;
    private BottomNavigationViewEx mPrimaryNavigation;

    private boolean mIsUserInGame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBottomNavigation();
        initPresenter();
        setListeners();
    }

    @Override
    public void showOfflinePageUi() {
        mPrimaryNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void showOfflineGameSettingsPageUi() {

    }

    @Override
    public void showOnlinePageUi() {
        mPrimaryNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void showOnlineSearchPageUi() {
        mPrimaryNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void showOnlineRoomCreationPageUi() {
        mPrimaryNavigation.setVisibility(View.GONE);
    }

    @Override
    public void showOnlineWaitingPageUi() {
        mPrimaryNavigation.setVisibility(View.GONE);
    }

    @Override
    public void showSetTopicPageUi() {
        mPrimaryNavigation.setVisibility(View.GONE);
    }

    @Override
    public void showDrawingPageUi() {
        mPrimaryNavigation.setVisibility(View.GONE);
    }

    @Override
    public void showGuessingPageUi() {
        mPrimaryNavigation.setVisibility(View.GONE);
    }

    @Override
    public void showSettingsPageUi() {
        mPrimaryNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mMainPresenter = checkNotNull(presenter);
    }


    /**
     * ***********************************************************************************
     * Bottom Nav Settings
     * ***********************************************************************************
     */
    private void setBottomNavigation() {
        mPrimaryNavigation = findViewById(R.id.bottom_nav_primary);
        mPrimaryNavigation.enableAnimation(false);
        mPrimaryNavigation.enableShiftingMode(false);
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
     * BottomNavigation Operations
     * ***********************************************************************************
     */

//    private void hideAllNav() {
//        mPrimaryNavigation.setVisibility(View.GONE);
//        mInGameNavigation.setVisibility(View.GONE);
//    }
//
//    private void showPrimaryNav() {
//        mPrimaryNavigation.setVisibility(View.VISIBLE);
//        mInGameNavigation.setVisibility(View.GONE);
//    }
//
//    private void showInGameDrawingNav() {
//        mPrimaryNavigation.setVisibility(View.GONE);
//        mInGameNavigation.setVisibility(View.VISIBLE);
//    }


    /**
     * ***********************************************************************************
     * Getters and Setters
     * ***********************************************************************************
     */
    public MainContract.Presenter getMainPresenter() {
        return mMainPresenter;
    }
}
