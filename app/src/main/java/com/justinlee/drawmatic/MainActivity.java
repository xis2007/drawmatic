package com.justinlee.drawmatic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.justinlee.drawmatic.User.UserManager;
import com.justinlee.drawmatic.activities.LoginActivity;
import com.justinlee.drawmatic.bases.BaseActivity;
import com.justinlee.drawmatic.constants.Constants;
import com.justinlee.drawmatic.util.BackLeaveAppBottomSheetDialog;
import com.justinlee.drawmatic.util.LeaveGameBottomSheetDialog;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends BaseActivity implements MainContract.View, BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "justinxx";

    private MainContract.Presenter mMainPresenter;
    private BottomNavigationViewEx mPrimaryNavigation;

    private ConstraintLayout mLoadingLayout;
    private SpinKitView mLoadingView;

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
    public void showGameResultPageUi() {

    }

    @Override
    public void showSettingsPageUi() {
        mPrimaryNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingUi() {
        mLoadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingUi() {
        mLoadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void showLeaveAppDialog() {
        BackLeaveAppBottomSheetDialog.newInstance(this).show(getSupportFragmentManager(), "LEAVE_APP_ALERT");
    }

    @Override
    public void showLeaveGameDialog() {
        LeaveGameBottomSheetDialog.newInstance(this).show(getSupportFragmentManager(), "LEAVE_GAME_ALERT");
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

    @Override
    public void onBackPressed() {
        mMainPresenter.determineOnBackPressedActions();
//        super.onBackPressed();
    }

    /**
     * ***********************************************************************************
     * Bottom Nav and Views Setup
     * ***********************************************************************************
     */
    private void setBottomNavigation() {
        mPrimaryNavigation = findViewById(R.id.bottom_nav_primary);
        mPrimaryNavigation.enableAnimation(false);
        mPrimaryNavigation.enableShiftingMode(false);
        mPrimaryNavigation.setSelectedItemId(R.id.navigation_online);
        mPrimaryNavigation.setCurrentItem(1);
    }

    private void setLoadingViews() {
        mLoadingLayout = findViewById(R.id.layout_loading);
        mLoadingLayout.setVisibility(View.GONE);
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
     * NavigationItemListeners Setup
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
        setLoadingViews();
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
