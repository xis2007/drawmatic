package com.justinlee.drawmatic;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;

import com.justinlee.drawmatic.User.UserManager;
import com.justinlee.drawmatic.constants.Constants;
import com.justinlee.drawmatic.in_game_drawing.DrawingFragment;
import com.justinlee.drawmatic.in_game_drawing.DrawingPresenter;
import com.justinlee.drawmatic.in_game_guessing.GuessingFragment;
import com.justinlee.drawmatic.in_game_guessing.GuessingPresenter;
import com.justinlee.drawmatic.in_game_set_topic.SetTopicFragment;
import com.justinlee.drawmatic.in_game_set_topic.SetTopicPresenter;
import com.justinlee.drawmatic.objects.Game;
import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.objects.OnlineSettings;
import com.justinlee.drawmatic.objects.Player;
import com.justinlee.drawmatic.offline.OfflineFragment;
import com.justinlee.drawmatic.offline.OfflinePresenter;
import com.justinlee.drawmatic.online.OnlineFragment;
import com.justinlee.drawmatic.online.OnlinePresenter;
import com.justinlee.drawmatic.online_cereate_room.CreateRoomFragment;
import com.justinlee.drawmatic.online_cereate_room.CreateRoomPresenter;
import com.justinlee.drawmatic.online_room_waiting.OnlineWaitingFragment;
import com.justinlee.drawmatic.online_room_waiting.OnlineWaitingPresenter;
import com.justinlee.drawmatic.settings.SettingsFragment;
import com.justinlee.drawmatic.settings.SettingsPresenter;

public class MainPresenter implements MainContract.Presenter {
    private Player mCurrentPlayer;

    private MainContract.View mMainView;

    private FragmentManager mFragmentManager;

    // fragments
    private OnlineFragment mOnlineFragment;
    private OfflineFragment mOfflineFragment;
    private SettingsFragment mSettingsFragment;

    private CreateRoomFragment mCreateRoomFragment;
    private OnlineWaitingFragment mOnlineWaitingFragment;
    private SetTopicFragment mSetTopicFragment;
    private DrawingFragment mDrawingFragment;
    private GuessingFragment mGuessingFragment;

    // presenters
    private OnlinePresenter mOnlinePresenter;
    private OfflinePresenter mOfflinePresenter;
    private SettingsPresenter mSettingsPresenter;

    private CreateRoomPresenter mCreateRoomPresenter;
    private OnlineWaitingPresenter mOnlineWaitingPresenter;
    private SetTopicPresenter mSetTopicPresenter;
    private DrawingPresenter mDrawingPresenter;
    private GuessingPresenter mGuessingPresenter;

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
        if (mSetTopicFragment != null) {
            transaction.remove(mSetTopicFragment);
            mSetTopicFragment = null;
            mSetTopicPresenter = null;
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
        if (mSetTopicFragment != null) {
            transaction.remove(mSetTopicFragment);
            mSetTopicFragment = null;
            mSetTopicPresenter = null;
        }
        if (mGuessingFragment != null) {
            transaction.remove(mGuessingFragment);
            mGuessingFragment = null;
            mGuessingPresenter = null;
        }
        if (mDrawingFragment != null) {
            transaction.remove(mDrawingFragment);
            mDrawingFragment = null;
            mDrawingPresenter = null;
        }
        // TODO add all others

        if (!mOnlineFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mOnlineFragment, "ONLINE");
        } else {
            transaction.show(mOnlineFragment);
        }

        if (mOnlinePresenter == null) mOnlinePresenter = new OnlinePresenter(mOnlineFragment);
        transaction.commit();

        if (mOnlineFragment.getSearchResultRecyclerView() != null) {
            mOnlineFragment.getSearchResultRecyclerView().setVisibility(View.GONE);
        }

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

        if (mCreateRoomPresenter == null) {
            mCreateRoomPresenter = new CreateRoomPresenter(mCreateRoomFragment, roomType);
            mCreateRoomPresenter.setMainView(mMainView);
            mCreateRoomPresenter.setMainPresenter(this);
        }
        transaction.commit();

        mMainView.showOnlineRoomCreationPageUi();
    }

    @Override
    public void transToOnlineWaitingPage(OnlineSettings onlineRoomSettings) {
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

        if (mOnlineWaitingPresenter == null) {
            mOnlineWaitingPresenter = new OnlineWaitingPresenter(mOnlineWaitingFragment, onlineRoomSettings);
            mOnlineWaitingPresenter.setMainView(mMainView);
            mOnlineWaitingPresenter.setMainPresenter(this);
        }
        transaction.commit();

        mMainView.showOnlineWaitingPageUi();
    }

    @Override
    public void transToSetTopicPage(int gameType, OnlineGame onlineGame) {
        // TODO go to set topic page
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mSetTopicFragment == null) mSetTopicFragment = SetTopicFragment.newInstance();

        if (mOnlineWaitingFragment != null) {
            transaction.remove(mOnlineWaitingFragment);
            mOnlineWaitingFragment = null;
            mOnlineWaitingPresenter = null;
        }
        // TODO add all others

        if (!mSetTopicFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mSetTopicFragment, "SET_TOPIC");
        } else {
            transaction.show(mSetTopicFragment);
        }
        if (mSetTopicPresenter == null) {
            mSetTopicPresenter = new SetTopicPresenter(mSetTopicFragment, onlineGame);
            mSetTopicPresenter.setMainView(mMainView);
            mSetTopicPresenter.setMainPresenter(this);
        }

        transaction.commit();

        mMainView.showSetTopicPageUi();
    }

    @Override
    public void transToDrawingPage(Game game) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mDrawingFragment == null) mDrawingFragment = DrawingFragment.newInstance();

        if (mSetTopicFragment != null) {
            transaction.remove(mSetTopicFragment);
            mSetTopicFragment = null;
            mSetTopicPresenter = null;
        }
        if (mGuessingFragment != null) {
            transaction.remove(mGuessingFragment);
            mGuessingFragment = null;
            mGuessingPresenter = null;
        }
        // TODO add all others

        if (!mDrawingFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mDrawingFragment, "DRAWING");
        } else {
            transaction.show(mDrawingFragment);
        }

        if (mDrawingPresenter == null) {
            mDrawingPresenter = new DrawingPresenter(mDrawingFragment, game);
            mDrawingPresenter.setMainView(mMainView);
            mDrawingPresenter.setMainPresenter(this);
        }
        transaction.commit();

        mMainView.showDrawingPageUi();
    }

    @Override
    public void transToGuessingPage(Game game) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mGuessingFragment == null) mGuessingFragment = GuessingFragment.newInstance();

        if (mDrawingFragment != null) {
            transaction.remove(mDrawingFragment);
            mDrawingFragment = null;
            mDrawingPresenter = null;
        }
        // TODO add all others

        if (!mGuessingFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mGuessingFragment, "GUESSING");
        } else {
            transaction.show(mGuessingFragment);
        }

        if (mGuessingPresenter == null) {
            mGuessingPresenter = new GuessingPresenter(mGuessingFragment, game);
            mGuessingPresenter.setMainView(mMainView);
            mGuessingPresenter.setMainPresenter(this);
        }
        transaction.commit();

        mMainView.showGuessingPageUi();
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
        if (mSetTopicFragment != null) {
            transaction.remove(mSetTopicFragment);
            mSetTopicFragment = null;
            mSetTopicPresenter = null;
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
    public void isLoading() {
        mMainView.showLoadingUi();
    }

    @Override
    public void isNotLoading() {
        mMainView.hideLoadingUi();
    }

    @Override
    public void start() {
        // TODO change to transToOffline
        transToOnlinePage();
        initializeCurrentPlayer();
    }


    private void initializeCurrentPlayer() {
        mCurrentPlayer = new Player(UserManager.getInstance().getUserName(), UserManager.getInstance().getUserId(), Constants.PlayerType.PARTICIPANT, -1);
    }


    /**
     * ***********************************************************************************
     * Getters and Setters
     * ***********************************************************************************
     */
    public Player getCurrentPlayer() {
        return mCurrentPlayer;
    }
}
