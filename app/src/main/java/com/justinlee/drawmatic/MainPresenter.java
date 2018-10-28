package com.justinlee.drawmatic;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.justinlee.drawmatic.firabase.RemoteSettingsManager;
import com.justinlee.drawmatic.user.UserManager;
import com.justinlee.drawmatic.constants.Constants;
import com.justinlee.drawmatic.gaming.drawing.DrawingFragment;
import com.justinlee.drawmatic.gaming.drawing.DrawingPresenter;
import com.justinlee.drawmatic.gaming.guessing.GuessingFragment;
import com.justinlee.drawmatic.gaming.guessing.GuessingPresenter;
import com.justinlee.drawmatic.gaming.result.GameResultFragment;
import com.justinlee.drawmatic.gaming.result.GameResultPresenter;
import com.justinlee.drawmatic.gaming.settopic.SetTopicFragment;
import com.justinlee.drawmatic.gaming.settopic.SetTopicPresenter;
import com.justinlee.drawmatic.instructions.InstructionsFragment;
import com.justinlee.drawmatic.instructions.InstructionsPresenter;
import com.justinlee.drawmatic.objects.Game;
import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.objects.Player;
import com.justinlee.drawmatic.play.PlayFragment;
import com.justinlee.drawmatic.play.PlayPresenter;
import com.justinlee.drawmatic.online.createroom.CreateRoomFragment;
import com.justinlee.drawmatic.online.createroom.CreateRoomPresenter;
import com.justinlee.drawmatic.online.roomwaiting.OnlineWaitingFragment;
import com.justinlee.drawmatic.online.roomwaiting.OnlineWaitingPresenter;
import com.justinlee.drawmatic.settings.SettingsFragment;
import com.justinlee.drawmatic.settings.SettingsPresenter;

public class MainPresenter implements MainContract.Presenter {
    private Player mCurrentPlayer;

    private MainContract.View mMainView;

    private FragmentManager mFragmentManager;

    // fragments
    private PlayFragment mPlayFragment;
    private InstructionsFragment mInstructionsFragment;
    private SettingsFragment mSettingsFragment;

    private CreateRoomFragment mCreateRoomFragment;
    private OnlineWaitingFragment mOnlineWaitingFragment;
    private SetTopicFragment mSetTopicFragment;
    private DrawingFragment mDrawingFragment;
    private GuessingFragment mGuessingFragment;
    private GameResultFragment mGameResultFragment;

    // presenters
    private PlayPresenter mPlayPresenter;
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
    public void checkIfAppUpdateIsRequired() {
        new RemoteSettingsManager((MainActivity) mMainView, this).checkForAppUpdateRequirement();
    }

    @Override
    public void promptUpdateRequirementMessage() {
        mMainView.showUpdateRequirementDialog();
    }

    @Override
    public void transToInstructionsPage() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mInstructionsFragment == null) mInstructionsFragment = InstructionsFragment.newInstance();

        if (mPlayFragment != null) transaction.hide(mPlayFragment);
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

        if (!mInstructionsFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mInstructionsFragment, Constants.FragmentFlag.FLAG_INSTRUCTIONS);
        } else {
            transaction.show(mInstructionsFragment);
        }

        if (mInstructionsPresenter == null) mInstructionsPresenter = new InstructionsPresenter(mInstructionsFragment);
        transaction.commit();

        mMainView.showInstructionsPageUi();
    }

    @Override
    public void transToOfflineGameSettingsPage() {

    }

    @Override
    public void transToPlayPage() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mPlayFragment == null) mPlayFragment = PlayFragment.newInstance();

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

        if (!mPlayFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mPlayFragment, Constants.FragmentFlag.FLAG_PLAY);
        } else {
            transaction.show(mPlayFragment);
        }

        if (mPlayPresenter == null) {
            mPlayPresenter = new PlayPresenter(mPlayFragment);
            mPlayPresenter.setMainView(mMainView);
            mPlayPresenter.setMainPresenter(this);
        }
        transaction.commit();


        if (mPlayFragment.getSearchedResultContainer() != null) {
            mPlayFragment.showGameSelectionPageUi();
        }

        // any time the user trans to play page, the user status should be reset to participant
        // ex. when room master leaves the game, leaves the room, finishes game...etc
        resetCurrentPlayerToParticipant();

        mMainView.showPlayPageUi();
    }

    @Override
    public void transToOnlineSearchPage() {
        mMainView.showOnlineSearchPageUi();
    }

    @Override
    public void transToGameCreationPage(int roomType) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mCreateRoomFragment == null) mCreateRoomFragment = CreateRoomFragment.newInstance();

        if (mInstructionsFragment != null) transaction.hide(mInstructionsFragment);
        if (mPlayFragment != null) transaction.hide(mPlayFragment);
        if (mSettingsFragment != null) transaction.hide(mSettingsFragment);

        if (mOnlineWaitingFragment != null) {
            transaction.remove(mOnlineWaitingFragment);
            mOnlineWaitingFragment = null;
            mOnlineWaitingPresenter = null;
        }

        if (!mCreateRoomFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mCreateRoomFragment, Constants.FragmentFlag.FLAG_CREATE_ROOM);
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
        if (mPlayFragment != null) transaction.hide(mPlayFragment);
        if (mSettingsFragment != null) transaction.hide(mSettingsFragment);

        if (mCreateRoomFragment != null) {
            transaction.remove(mCreateRoomFragment);
            mCreateRoomFragment = null;
            mCreateRoomPresenter = null;
        }

        if (!mOnlineWaitingFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mOnlineWaitingFragment, Constants.FragmentFlag.FLAG_WAITING);
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
    public void transToSetTopicPage(Game game) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mSetTopicFragment == null) mSetTopicFragment = SetTopicFragment.newInstance();

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

        if (!mSetTopicFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mSetTopicFragment, Constants.FragmentFlag.FLAG_SET_TOPIC);
        } else {
            transaction.show(mSetTopicFragment);
        }
        if (mSetTopicPresenter == null) {
            mSetTopicPresenter = new SetTopicPresenter(mSetTopicFragment, game);
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

        if (!mDrawingFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mDrawingFragment, Constants.FragmentFlag.FLAG_DRAWING);
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

        if (!mGuessingFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mGuessingFragment, Constants.FragmentFlag.FLAG_GUESSING);
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
            transaction.add(R.id.fragment_container_main, mGameResultFragment, Constants.FragmentFlag.FLAG_GAME_RESULT);
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
        if (mPlayFragment != null) transaction.hide(mPlayFragment);

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

        if (!mSettingsFragment.isAdded()) {
            transaction.add(R.id.fragment_container_main, mSettingsFragment, Constants.FragmentFlag.FLAG_SETTINGS);
        } else {
            transaction.show(mSettingsFragment);
        }

        if (mSettingsPresenter == null) {
            mSettingsPresenter = new SettingsPresenter(mSettingsFragment);
            mSettingsPresenter.setMainView(mMainView);
            mSettingsPresenter.setMainPresenter(this);
        }
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
    public void informToShowTapToNextStepUi() {
        mMainView.showTapToNextStepUi();
    }

    @Override
    public void determineOnBackPressedActions() {
        // if main menu-related fragments are shown, it means a dialog should show to users to confirm they want to leave the app
        if (mPlayFragment != null && !mPlayFragment.isHidden()) mMainView.showLeaveAppDialog();
        if (mInstructionsFragment != null && !mInstructionsFragment.isHidden()) mMainView.showLeaveAppDialog();
        if (mSettingsFragment != null && !mSettingsFragment.isHidden()) mMainView.showLeaveAppDialog();

        // if user is in room creation page, user should return to main menu when back is pressed
        if (mCreateRoomFragment != null && !mCreateRoomFragment.isHidden()) transToPlayPage();

        // if user is in room waiting page, user should return to main menu when back is pressed
        if (mOnlineWaitingFragment != null && !mOnlineWaitingFragment.isHidden()) mOnlineWaitingPresenter.leaveRoom(mOnlineWaitingFragment);

        // if user is in game or results page, user should be prompt with leave game confirmation dialog
        if (mSetTopicFragment != null && !mSetTopicFragment.isHidden()) mSetTopicPresenter.informActivityToPromptLeaveGameAlert();
        if (mDrawingFragment != null && !mDrawingFragment.isHidden()) mDrawingPresenter.informActivityToPromptLeaveGameAlert();
        if (mGuessingFragment != null && !mGuessingFragment.isHidden()) mGuessingPresenter.informActivityToPromptLeaveGameAlert();
        if (mGameResultFragment != null && !mGameResultFragment.isHidden()) mGameResultPresenter.informActivityToPromptLeaveGameAlert();

    }

    @Override
    public void informToShowLeaveGameDialog(OnlineGame onlineGame) {
        mMainView.showLeaveGameDialog(onlineGame);
    }

    @Override
    public void resetCurrentPlayerToParticipant() {
        if (mCurrentPlayer != null) mCurrentPlayer.setPlayerType(Constants.PlayerType.PARTICIPANT);
    }

    @Override
    public void start() {
        transToPlayPage();
        initializeCurrentPlayer();
    }


    private void initializeCurrentPlayer() {
        mCurrentPlayer = new Player(UserManager.getInstance().getUserName(), UserManager.getInstance().getUserId(), Constants.PlayerType.PARTICIPANT);
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
