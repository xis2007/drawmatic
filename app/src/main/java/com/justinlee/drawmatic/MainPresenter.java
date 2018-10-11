package com.justinlee.drawmatic;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.justinlee.drawmatic.User.UserManager;
import com.justinlee.drawmatic.constants.Constants;
import com.justinlee.drawmatic.in_game_drawing.DrawingFragment;
import com.justinlee.drawmatic.in_game_drawing.DrawingPresenter;
import com.justinlee.drawmatic.in_game_guessing.GuessingFragment;
import com.justinlee.drawmatic.in_game_guessing.GuessingPresenter;
import com.justinlee.drawmatic.in_game_result.GameResultFragment;
import com.justinlee.drawmatic.in_game_result.GameResultPresenter;
import com.justinlee.drawmatic.in_game_set_topic.SetTopicFragment;
import com.justinlee.drawmatic.in_game_set_topic.SetTopicPresenter;
import com.justinlee.drawmatic.instructions.InstructionsFragment;
import com.justinlee.drawmatic.instructions.InstructionsPresenter;
import com.justinlee.drawmatic.objects.Game;
import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.objects.Player;
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
    private InstructionsFragment mInstructionsFragment;
    private SettingsFragment mSettingsFragment;

    private CreateRoomFragment mCreateRoomFragment;
    private OnlineWaitingFragment mOnlineWaitingFragment;
    private SetTopicFragment mSetTopicFragment;
    private DrawingFragment mDrawingFragment;
    private GuessingFragment mGuessingFragment;
    private GameResultFragment mGameResultFragment;

    // presenters
    private OnlinePresenter mOnlinePresenter;
    private InstructionsPresenter mInstructionsPresenter;
    private SettingsPresenter mSettingsPresenter;

    private CreateRoomPresenter mCreateRoomPresenter;
    private OnlineWaitingPresenter mOnlineWaitingPresenter;
    private SetTopicPresenter mSetTopicPresenter;
    private DrawingPresenter mDrawingPresenter;
    private GuessingPresenter mGuessingPresenter;
    private GameResultPresenter mGameResultPresenter;

    public MainPresenter(MainContract.View mainView, FragmentManager fragmentManager) {
        mMainView = mainView;
        mFragmentManager = fragmentManager;
    }

    @Override
    public void transToOfflinePage() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mInstructionsFragment == null) mInstructionsFragment = InstructionsFragment.newInstance();

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

        if (!mInstructionsFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mInstructionsFragment, "OFFLINE");
        } else {
            transaction.show(mInstructionsFragment);
        }

        if (mInstructionsPresenter == null) mInstructionsPresenter = new InstructionsPresenter(mInstructionsFragment);
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

        if (mInstructionsFragment != null) transaction.hide(mInstructionsFragment);
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
        if (mGameResultFragment != null) {
            transaction.remove(mGameResultFragment);
            mGameResultFragment = null;
            mGameResultPresenter = null;
        }
        // TODO add all others

        if (!mOnlineFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mOnlineFragment, "ONLINE");
        } else {
            transaction.show(mOnlineFragment);
        }

        if (mOnlinePresenter == null) {
            mOnlinePresenter = new OnlinePresenter(mOnlineFragment);
            mOnlinePresenter.setMainView(mMainView);
            mOnlinePresenter.setMainPresenter(this);
        }
        transaction.commit();

//        if (mOnlineFragment.getSearchedResultContainer() != null) {
//            mOnlineFragment.getSearchedResultContainer().setVisibility(View.GONE);
//        }

        if(mOnlineFragment.getSearchedResultContainer() != null) {
            mOnlineFragment.showGameSelectionPageUi();
        }

        mMainView.showOnlinePageUi();
    }

    @Override
    public void transToOnlineSearchPage() {
        mMainView.showOnlineSearchPageUi();
    }

    @Override
    public void transToOnlineRoomCreationPage(int roomType) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mCreateRoomFragment == null) mCreateRoomFragment = CreateRoomFragment.newInstance();

        if (mInstructionsFragment != null) transaction.hide(mInstructionsFragment);
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
    public void transToOnlineWaitingPage(OnlineGame onlineGame) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mOnlineWaitingFragment == null) mOnlineWaitingFragment = OnlineWaitingFragment.newInstance();

        if (mInstructionsFragment != null) transaction.hide(mInstructionsFragment);
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
            mOnlineWaitingPresenter = new OnlineWaitingPresenter(mOnlineWaitingFragment, onlineGame);
            mOnlineWaitingPresenter.setMainView(mMainView);
            mOnlineWaitingPresenter.setMainPresenter(this);
        }
        transaction.commit();

        mMainView.showOnlineWaitingPageUi();
    }

    @Override
    public void transToSetTopicPage(OnlineGame onlineGame) {
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
    public void transToGameResultPage(Game game) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mGameResultFragment == null) mGameResultFragment = GameResultFragment.newInstance();

        if (mDrawingFragment != null) {
            transaction.remove(mDrawingFragment);
            mDrawingFragment = null;
            mDrawingPresenter = null;
        }

        if (mGuessingFragment != null) {
            transaction.remove(mGuessingFragment);
            mGuessingFragment = null;
            mGuessingPresenter = null;
        }

        if (!mGameResultFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mGameResultFragment, "GAME_RESULT");
        } else {
            transaction.show(mGameResultFragment);
        }

        if (mGameResultPresenter == null) {
            mGameResultPresenter = new GameResultPresenter(mGameResultFragment, game);
            mGameResultPresenter.setMainView(mMainView);
            mGameResultPresenter.setMainPresenter(this);
        }
        transaction.commit();

        mMainView.showGameResultPageUi();
    }

    @Override
    public void transToSettingsPage() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mSettingsFragment == null) mSettingsFragment = SettingsFragment.newInstance();

        if (mInstructionsFragment != null) transaction.hide(mInstructionsFragment);
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
    public void isLoading(String loadingHint) {
        mMainView.showLoadingUi(loadingHint);
    }

    @Override
    public void isNotLoading() {
        mMainView.hideLoadingUi();
    }

    @Override
    public void determineOnBackPressedActions() {
        // if main menu-related fragments are shown, it means a dialog should show to users to confirm they want to leave the app
        if(mOnlineFragment != null && !mOnlineFragment.isHidden()) mMainView.showLeaveAppDialog();
        if(mInstructionsFragment != null && !mInstructionsFragment.isHidden()) mMainView.showLeaveAppDialog();
        if(mSettingsFragment != null && !mSettingsFragment.isHidden()) mMainView.showLeaveAppDialog();

        // if user is in room creation page, user should return to main menu when back is pressed
        if(mCreateRoomFragment != null && !mCreateRoomFragment.isHidden()) transToOnlinePage();

        // if user is in room waiting page, user should return to main menu when back is pressed
        if(mOnlineWaitingFragment != null && !mOnlineWaitingFragment.isHidden()) mOnlineWaitingPresenter.leaveRoom(mOnlineWaitingFragment);

        // if user is in game or results page, user should be prompt with leave game confirmation dialog
        if(mSetTopicFragment != null && !mSetTopicFragment.isHidden()) mSetTopicPresenter.informActivityToPromptLeaveGameAlert();
        if(mDrawingFragment != null && !mDrawingFragment.isHidden()) mDrawingPresenter.informActivityToPromptLeaveGameAlert();
        if(mGuessingFragment != null && !mGuessingFragment.isHidden()) mGuessingPresenter.informActivityToPromptLeaveGameAlert();
        if(mGameResultFragment != null && !mGameResultFragment.isHidden()) mGameResultPresenter.informActivityToPromptLeaveGameAlert();

    }

    @Override
    public void informToShowLeaveGameDialog(OnlineGame onlineGame) {
        mMainView.showLeaveGameDialog(onlineGame);
    }

    @Override
    public void resetCurrentPlayerToParticipant() {
        mCurrentPlayer.setPlayerType(Constants.PlayerType.PARTICIPANT);
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
