package com.justinlee.drawmatic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.justinlee.drawmatic.User.UserManager;
import com.justinlee.drawmatic.activities.LoginActivity;
import com.justinlee.drawmatic.bases.BaseActivity;
import com.justinlee.drawmatic.constants.Constants;
import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.util.BackLeaveAppBottomSheetDialog;
import com.justinlee.drawmatic.util.LeaveGameBottomSheetDialog;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends BaseActivity implements MainContract.View, BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "justinxx";

    private MainContract.Presenter mMainPresenter;
    private BottomNavigationViewEx mPrimaryNavigation;

    private TextView mLoadingHint;
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
    public void showLoadingUi(String loadingHint) {
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLoadingHint.setText(loadingHint);
    }

    @Override
    public void hideLoadingUi() {
        mLoadingLayout.setVisibility(View.GONE);
        mLoadingHint.setText("");
    }

    @Override
    public void showLeaveAppDialog() {
        BackLeaveAppBottomSheetDialog.newInstance(this).show(getSupportFragmentManager(), "LEAVE_APP_ALERT");
    }

    @Override
    public void showLeaveGameDialog(OnlineGame onlineGame) {
        LeaveGameBottomSheetDialog.newInstance(this).setOnlineGame(onlineGame).show(getSupportFragmentManager(), "LEAVE_GAME_ALERT");
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
        setNavigationItemBackground(1);
    }

    private void setLoadingViews() {
        mLoadingHint = findViewById(R.id.text_hint_loading);
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
                setNavigationItemBackground(0);
                mMainPresenter.transToOfflinePage();
                return true;
            case R.id.navigation_online:
                setNavigationItemBackground(1);
                mMainPresenter.transToOnlinePage();
                return true;
            case R.id.navigation_settings:
                setNavigationItemBackground(2);
                mMainPresenter.transToSettingsPage();
                return true;
        }
        return false;
    }

    void setNavigationItemBackground(int itemNum) {
        switch (itemNum) {
            case 0:
                mPrimaryNavigation.setItemBackground(0, R.drawable.oval_shadow_navigation_button);
                mPrimaryNavigation.setItemBackground(1, R.drawable.oval_blank_navigation_button);
                mPrimaryNavigation.setItemBackground(2, R.drawable.oval_blank_navigation_button);
                break;

            case 1:
                mPrimaryNavigation.setItemBackground(0, R.drawable.oval_blank_navigation_button);
                mPrimaryNavigation.setItemBackground(1, R.drawable.oval_shadow_navigation_button);
                mPrimaryNavigation.setItemBackground(2, R.drawable.oval_blank_navigation_button);
                break;

            case 2:
                mPrimaryNavigation.setItemBackground(0, R.drawable.oval_blank_navigation_button);
                mPrimaryNavigation.setItemBackground(1, R.drawable.oval_blank_navigation_button);
                mPrimaryNavigation.setItemBackground(2, R.drawable.oval_shadow_navigation_button);
                break;

            default:
                break;
        }
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
