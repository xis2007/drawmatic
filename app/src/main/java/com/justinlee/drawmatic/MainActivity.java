package com.justinlee.drawmatic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.justinlee.drawmatic.User.UserManager;
import com.justinlee.drawmatic.activities.LoginActivity;
import com.justinlee.drawmatic.bases.BaseActivity;
import com.justinlee.drawmatic.constants.Constants;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends BaseActivity implements MainContract.View, BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "justinxx";

    private MainContract.Presenter mMainPresenter;
    private BottomNavigationViewEx mPrimaryNavigation;

    private boolean mIsUserInGame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            init();
        } else {
            promptForLogin();
        }

        Log.d(TAG, "saveUserInfo: user name is: " + UserManager.getInstance().getUserName());
        Log.d(TAG, "saveUserInfo: user id is: " + UserManager.getInstance().getUserId());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.Login.LOGIN_ACTIVITY && resultCode == Constants.Login.LOGIN_SUCCESS) {
            init();
        } else if (requestCode == Constants.Login.LOGIN_ACTIVITY && resultCode == Constants.Login.LOGIN_EXIT) {
            finish();
        }
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
     * Initialization and Login
     * ***********************************************************************************
     */

    private void init() {
        setBottomNavigation();
        initPresenter();
        setListeners();
    }

    private void promptForLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent, Constants.Login.LOGIN_ACTIVITY);
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
