package com.justinlee.drawmatic;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.justinlee.drawmatic.cereate_room.CreateRoomFragment;
import com.justinlee.drawmatic.cereate_room.CreateRoomPresenter;
import com.justinlee.drawmatic.offline.OfflineFragment;
import com.justinlee.drawmatic.offline.OfflinePresenter;
import com.justinlee.drawmatic.online.OnlineFragment;
import com.justinlee.drawmatic.online.OnlinePresenter;
import com.justinlee.drawmatic.settings.SettingsFragment;
import com.justinlee.drawmatic.settings.SettingsPresenter;

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mMainView;

    private FragmentManager mFragmentManager;

    // fragments
    private OnlineFragment mOnlineFragment;
    private OfflineFragment mOfflineFragment;
    private SettingsFragment mSettingsFragment;
    private CreateRoomFragment mCreateRoomFragment;

    // presenters
    private OnlinePresenter mOnlinePresenter;
    private OfflinePresenter mOfflinePresenter;
    private SettingsPresenter mSettingsPresenter;
    private CreateRoomPresenter mCreateRoomPresenter;

    public MainPresenter(MainContract.View mainView, FragmentManager fragmentManager) {
        mMainView = mainView;
        mFragmentManager = fragmentManager;
    }

    @Override
    public void transToOfflinePage() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mOfflineFragment == null) mOfflineFragment = OfflineFragment.newInstance();

        if (mOnlineFragment != null) transaction.hide(mOnlineFragment);
        if (mSettingsFragment != null) transaction.hide(mSettingsFragment);

        if (mCreateRoomFragment != null) {
            transaction.remove(mCreateRoomFragment);
            mCreateRoomFragment = null;
            mCreateRoomPresenter = null;
        }
        // TODO add all others

        if (!mOfflineFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mOfflineFragment, "OFFLINE");
        } else {
            transaction.show(mOfflineFragment);
        }

        if (mOfflinePresenter == null) mOfflinePresenter = new OfflinePresenter(mOfflineFragment);
        transaction.commit();

        mMainView.showOfflinePageUi();
    }

    @Override
    public void transToOfflineGameSettingsPage() {

    }

    @Override
    public void transToOnlinePage() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mOnlineFragment == null) mOnlineFragment = OnlineFragment.newInstance();

        if (mOfflineFragment != null) transaction.hide(mOfflineFragment);
        if (mSettingsFragment != null) transaction.hide(mSettingsFragment);

        if (mCreateRoomFragment != null) {
            transaction.remove(mCreateRoomFragment);
            mCreateRoomFragment = null;
            mCreateRoomPresenter = null;
        }
        // TODO add all others

        if (!mOnlineFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mOnlineFragment, "ONLINE");
        } else {
            transaction.show(mOnlineFragment);
        }

        if (mOnlinePresenter == null) mOnlinePresenter = new OnlinePresenter(mOnlineFragment);
        transaction.commit();

        mMainView.showOnlinePageUi();
    }

    @Override
    public void transToOnlineSearchPage() {

    }

    @Override
    public void transToOnlineRoomCreationPage() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mCreateRoomFragment == null) mCreateRoomFragment = CreateRoomFragment.newInstance();

        if (mOfflineFragment != null) transaction.hide(mOfflineFragment);
        if (mOnlineFragment != null) transaction.hide(mOnlineFragment);
        if (mSettingsFragment != null) transaction.hide(mSettingsFragment);
        // TODO add all others

        if (!mCreateRoomFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mCreateRoomFragment, "CREATE_ROOM");
        } else {
            transaction.show(mCreateRoomFragment);
        }

        if (mCreateRoomPresenter == null) mCreateRoomPresenter = new CreateRoomPresenter(mCreateRoomFragment);
        transaction.commit();

        mMainView.showOnlineRoomCreationPageUi();
    }

    @Override
    public void transToOnlineGameSettingsPage() {

    }

    @Override
    public void transToDrawingPage() {

    }

    @Override
    public void transToGuessingPage() {

    }

    @Override
    public void transToSettingsPage() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mSettingsFragment == null) mSettingsFragment = SettingsFragment.newInstance();

        if (mOfflineFragment != null) transaction.hide(mOfflineFragment);
        if (mOnlineFragment != null) transaction.hide(mOnlineFragment);

        if (mCreateRoomFragment != null) {
            transaction.remove(mCreateRoomFragment);
            mCreateRoomFragment = null;
            mCreateRoomPresenter = null;
        }
        // TODO add all others

        if (!mSettingsFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mSettingsFragment, "SETTINGS");
        } else {
            transaction.show(mSettingsFragment);
        }

        if (mSettingsPresenter == null) mSettingsPresenter = new SettingsPresenter(mSettingsFragment);
        transaction.commit();

        mMainView.showSettingsPageUi();
    }

    @Override
    public void start() {
        // TODO change to transToOffline
        transToOnlinePage();
    }
}
