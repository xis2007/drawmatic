package com.justinlee.drawmatic.in_game_result;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.firabase_operation.OnlineInGameManager;
import com.justinlee.drawmatic.objects.Game;
import com.justinlee.drawmatic.objects.OfflineGame;
import com.justinlee.drawmatic.objects.OnlineGame;

import java.util.ArrayList;

public class GameResultPresenter implements GameResultContract.Presenter {
    private MainContract.View mMainView;
    private MainContract.Presenter mMainPresenter;

    private GameResultContract.View mGameResultView;

    private OnlineGame mOnlineGame;
    private OfflineGame mOfflineGame;

    public GameResultPresenter(GameResultContract.View gameResultView, Game game) {
        mGameResultView = gameResultView;
        mGameResultView.setPresenter(this);

        if (game instanceof OnlineGame) {
            mOnlineGame = (OnlineGame) game;
            mOfflineGame = null;
        } else {
            mOnlineGame = null;
            mOfflineGame = (OfflineGame) game;
        }
    }

    @Override
    public void informActivityToPromptLeaveGameAlert() {
        mMainPresenter.informToShowLeaveGameDialog(mOnlineGame);
    }

    @Override
    public void informToShowResults(ArrayList<String> resultStrings) {
        mGameResultView.showResults(resultStrings);
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
        new OnlineInGameManager((MainActivity) mMainView).deleteDataAfterResult(mOnlineGame);
        mMainPresenter.resetCurrentPlayerToParticipant();
        mMainPresenter.transToPlayPage();
    }

    @Override
    public void start() {
        new OnlineInGameManager((MainActivity) mMainView).retrieveGameResults(mGameResultView, this, mOnlineGame);
    }


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
