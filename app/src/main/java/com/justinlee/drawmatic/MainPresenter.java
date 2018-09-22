package com.justinlee.drawmatic;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.justinlee.drawmatic.objects.GameSettings;
import com.justinlee.drawmatic.objects.OnlineRoom;
import com.justinlee.drawmatic.online_cereate_room.CreateRoomFragment;
import com.justinlee.drawmatic.online_cereate_room.CreateRoomPresenter;
import com.justinlee.drawmatic.offline.OfflineFragment;
import com.justinlee.drawmatic.offline.OfflinePresenter;
import com.justinlee.drawmatic.online.OnlineFragment;
import com.justinlee.drawmatic.online.OnlinePresenter;
import com.justinlee.drawmatic.online_room_waiting.OnlineWaitingFragment;
import com.justinlee.drawmatic.online_room_waiting.OnlineWaitingPresenter;
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
    private OnlineWaitingFragment mOnlineWaitingFragment;

    // presenters
    private OnlinePresenter mOnlinePresenter;
    private OfflinePresenter mOfflinePresenter;
    private SettingsPresenter mSettingsPresenter;
    private CreateRoomPresenter mCreateRoomPresenter;
    private OnlineWaitingPresenter mOnlineWaitingPresenter;

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
        if (mOnlineWaitingFragment != null) {
            transaction.remove(mOnlineWaitingFragment);
            mOnlineWaitingFragment = null;
            mOnlineWaitingPresenter = null;
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
        if (mOnlineWaitingFragment != null) {
            transaction.remove(mOnlineWaitingFragment);
            mOnlineWaitingFragment = null;
            mOnlineWaitingPresenter = null;
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
    public void transToOnlineRoomCreationPage(int roomType) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mCreateRoomFragment == null) mCreateRoomFragment = CreateRoomFragment.newInstance();

        if (mOfflineFragment != null) transaction.hide(mOfflineFragment);
        if (mOnlineFragment != null) transaction.hide(mOnlineFragment);
        if (mSettingsFragment != null) transaction.hide(mSettingsFragment);

        if (mOnlineWaitingFragment != null) {
            transaction.remove(mOnlineWaitingFragment);
            mOnlineWaitingFragment = null;
            mOnlineWaitingPresenter = null;
        }
        // TODO add all others

        if (!mCreateRoomFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mCreateRoomFragment, "CREATE_ROOM");
        } else {
            transaction.show(mCreateRoomFragment);
        }

        if (mCreateRoomPresenter == null) mCreateRoomPresenter = new CreateRoomPresenter(mCreateRoomFragment, roomType);
        transaction.commit();

        mMainView.showOnlineRoomCreationPageUi();
    }

    @Override
    public void transToOnlineWaitingPage(OnlineRoom onlineRoom) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mOnlineWaitingFragment == null) mOnlineWaitingFragment = OnlineWaitingFragment.newInstance();

        if (mOfflineFragment != null) transaction.hide(mOfflineFragment);
        if (mOnlineFragment != null) transaction.hide(mOnlineFragment);
        if (mSettingsFragment != null) transaction.hide(mSettingsFragment);

        if (mCreateRoomFragment != null) {
            transaction.remove(mCreateRoomFragment);
            mCreateRoomFragment = null;
            mCreateRoomPresenter = null;
        }
        // TODO add all others

        if (!mOnlineWaitingFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mOnlineWaitingFragment, "CREATE_ROOM");
        } else {
            transaction.show(mOnlineWaitingFragment);
        }

        if (mOnlineWaitingPresenter == null) mOnlineWaitingPresenter = new OnlineWaitingPresenter(mOnlineWaitingFragment, onlineRoom);
        transaction.commit();

        mMainView.showOnlineWaitingPageUi();
    }

    @Override
    public void transToSetTopicPage(int gameType, GameSettings gameSettings) {
        // TODO go to set topic page
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
        if (mOnlineWaitingFragment != null) {
            transaction.remove(mOnlineWaitingFragment);
            mOnlineWaitingFragment = null;
            mOnlineWaitingPresenter = null;
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
