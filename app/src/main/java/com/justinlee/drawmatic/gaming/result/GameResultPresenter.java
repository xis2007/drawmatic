package com.justinlee.drawmatic.gaming.result;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.firabase.OnlineInGameManager;
import com.justinlee.drawmatic.objects.Game;
import com.justinlee.drawmatic.objects.OfflineGame;
import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.util.TopicDrawingRetrievingUtil;

import java.util.ArrayList;

public class GameResultPresenter implements GameResultContract.Presenter {
    private MainContract.View mMainView;
    private MainContract.Presenter mMainPresenter;

    private GameResultContract.View mGameResultView;

    private boolean mIsInOfflineMode;
    private OnlineGame mOnlineGame;
    private OfflineGame mOfflineGame;

    public GameResultPresenter(GameResultContract.View gameResultView, Game game) {
        mGameResultView = gameResultView;
        mGameResultView.setPresenter(this);

        if (game instanceof OnlineGame) {
            mIsInOfflineMode = false;
            mOnlineGame = (OnlineGame) game;
            mOfflineGame = null;
        } else {
            mIsInOfflineMode = true;
            mOnlineGame = null;
            mOfflineGame = (OfflineGame) game;
        }
    }

    @Override
    public void informActivityToPromptLeaveGameAlert() {
        mMainPresenter.informToShowLeaveGameDialog(mOnlineGame);
    }

    @Override
    public void informToShowOnlineGameResults(ArrayList<String> resultStrings) {
        mGameResultView.showOnlineGameResults(resultStrings, new TopicDrawingRetrievingUtil((MainActivity) mMainView, mOnlineGame, ((MainPresenter) mMainPresenter).getCurrentPlayer()).calcOrderedPlayersForResults());
        mMainPresenter.isNotLoading();
    }

    @Override
    public void deleteFirestoreRoomData() {

    }

    @Override
    public void deleteStorageImagesData() {

    }

    @Override
    public void doneViewingResult() {
        if (mIsInOfflineMode) {
            mMainPresenter.transToPlayPage();
        } else {
            new OnlineInGameManager((MainActivity) mMainView).deleteDataAfterResult(mOnlineGame);
            mMainPresenter.resetCurrentPlayerToParticipant();
            mMainPresenter.transToPlayPage();
        }

    }

    @Override
    public void start() {
        if (mIsInOfflineMode) {
            mMainPresenter.informToShowTapToNextStepUi();
            mGameResultView.showOfflineGameResults(mOfflineGame.getOfflineSettings().getGuessingAndDrawingsList());
        } else {
            new OnlineInGameManager((MainActivity) mMainView).retrieveGameResults(mGameResultView, this, mOnlineGame);
        }
    }

    /**
     * ***********************************************************************************
     * Offline Mode
     * ***********************************************************************************
     */


    /**
     * ***********************************************************************************
     * Set MainView and MainPresenters to get reference to them
     * ***********************************************************************************
     */
    public void setMainView(MainContract.View mainView) {
        mMainView = mainView;
    }


    public void setMainPresenter(MainPresenter mainPresenter) {
        mMainPresenter = mainPresenter;
    }
}
