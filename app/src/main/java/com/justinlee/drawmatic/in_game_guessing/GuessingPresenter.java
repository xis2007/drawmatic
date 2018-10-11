package com.justinlee.drawmatic.in_game_guessing;

import android.os.CountDownTimer;

import com.google.firebase.firestore.ListenerRegistration;
import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.firabase_operation.OnlineInGameManager;
import com.justinlee.drawmatic.firabase_operation.OnlineRoomManager;
import com.justinlee.drawmatic.objects.Game;
import com.justinlee.drawmatic.objects.OfflineGame;
import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.util.StringUtil;

public class GuessingPresenter implements GuessingContract.Presenter {
    private static final String TAG = "justinxxxxx";

    private MainContract.View mMainView;
    private MainContract.Presenter mMainPresenter;
    private GuessingContract.View mGuessingView;

    private OnlineGame mOnlineGame;
    private OfflineGame mOfflineGame;

    private CountDownTimer mCountDownTimer;

    private ListenerRegistration mRoomListenerRegistration;
    private ListenerRegistration mGuessingListenerRegistration;

    public GuessingPresenter(GuessingContract.View setTopicView, Game game) {
        mGuessingView = setTopicView;
        mGuessingView.setPresenter(this);

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
    public void leaveRoom(GuessingFragment fragment) {
        mGuessingListenerRegistration.remove();
    }

    @Override
    public void transToDrawingPage() {
        if (mOnlineGame != null) {
            ((MainActivity) mMainView).getMainPresenter().transToDrawingPage(mOnlineGame);
        } else {
            ((MainActivity) mMainView).getMainPresenter().transToDrawingPage(mOfflineGame);
        }

    }

    @Override
    public void setDrawing(String imageUrl) {
        mGuessingView.showDrawing(imageUrl);
    }

    @Override
    public void setAndStartTimer() {
        mCountDownTimer = new CountDownTimer((long) (mOnlineGame.getDrawingAndGuessingTimeAllowed() * 20 * 1000), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secUntilFinish = millisUntilFinished / 1000;
                mGuessingView.updateTimer(secUntilFinish);
            }

            @Override
            public void onFinish() {
                mMainView.showLoadingUi(((MainActivity) mMainView).getResources().getString(R.string.hint_loading_loading_data));
                String inputGuessing = mGuessingView.getGuessingInput();
                if(inputGuessing == null) inputGuessing = "";
                new OnlineInGameManager((MainActivity) mMainView).updateGuessingStepProgressAndUploadGuessing(GuessingPresenter.this, mOnlineGame, inputGuessing);
            }
        }.start();
    }

    @Override
    public void setCurrentStep() {
        mGuessingView.showCurrentStep(mOnlineGame.getCurrentStep(), mOnlineGame.getTotalSteps());
    }

    @Override
    public void startMonitoringPlayerGuessingProgress() {
        mGuessingListenerRegistration = new OnlineInGameManager((MainActivity) mMainView).monitorGuessingProgress(mGuessingView, this, mOnlineGame);
    }

    @Override
    public void startGuessing() {
        mMainView.hideLoadingUi();
        setAndStartTimer();
    }

    @Override
    public void unregisterListener() {
        mGuessingListenerRegistration.remove();
    }

    @Override
    public void setWordCountHint(String theWord) {
        if(StringUtil.isEmptyString(theWord)) {
            mGuessingView.showWordCountHint(0);
        } else {
            mGuessingView.showWordCountHint(theWord.length());
        }

    }

    @Override
    public void finishGame(Game game) {
        mMainPresenter.transToGameResultPage(game);
    }

    @Override
    public void start() {
        mRoomListenerRegistration = new OnlineRoomManager((MainActivity) mMainView).syncRoomStatusWhileInGame(mMainView, mMainPresenter, mOnlineGame);
        new OnlineInGameManager((MainActivity) mMainView).retrieveDrawingAndWordCount(mGuessingView, this, mOnlineGame);
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


    /**
     * ***********************************************************************************
     * Getters and Setters
     * ***********************************************************************************
     */
    public CountDownTimer getCountDownTimer() {
        return mCountDownTimer;
    }

    public ListenerRegistration getRoomListenerRegistration() {
        return mRoomListenerRegistration;
    }
}
