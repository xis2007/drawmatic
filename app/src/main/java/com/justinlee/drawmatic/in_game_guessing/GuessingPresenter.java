package com.justinlee.drawmatic.in_game_guessing;

import android.graphics.Bitmap;
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
import com.justinlee.drawmatic.util.TopicDrawingRetrievingUtil;

public class GuessingPresenter implements GuessingContract.Presenter {
    private static final String TAG = "justinxxxxx";

    private MainContract.View mMainView;
    private MainContract.Presenter mMainPresenter;
    private GuessingContract.View mGuessingView;

    private boolean mIsInOfflineMode;
    private OnlineGame mOnlineGame;
    private OfflineGame mOfflineGame;

    private CountDownTimer mCountDownTimer;

    private ListenerRegistration mRoomListenerRegistration;
    private ListenerRegistration mGuessingListenerRegistration;

    public GuessingPresenter(GuessingContract.View setTopicView, Game game) {
        mGuessingView = setTopicView;
        mGuessingView.setPresenter(this);

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
    public void setOnlineDrawing(String imageUrl) {
        mGuessingView.showOnlineDrawing(imageUrl);
    }

    @Override
    public void setPreviousPlayer() {
        mGuessingView.showPreviousPlayer(new TopicDrawingRetrievingUtil((MainActivity) mMainView, mOnlineGame, ((MainPresenter) mMainPresenter).getCurrentPlayer()).calcPlayerNameWhereTopicOrDrawingIsRetrieved());
    }

    @Override
    public void setAndStartTimer() {
        mCountDownTimer = new CountDownTimer((long) (mOnlineGame.getDrawingAndGuessingTimeAllowed() * 30 * 1000), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secUntilFinish = millisUntilFinished / 1000;
                mGuessingView.updateTimer(secUntilFinish);
            }

            @Override
            public void onFinish() {
                updateGuessingStepProgressAndUploadGuessing();
            }
        }.start();
    }

    @Override
    public void updateGuessingStepProgressAndUploadGuessing() {
        mMainView.showLoadingUi(((MainActivity) mMainView).getResources().getString(R.string.hint_loading_loading_data));
        String inputGuessing = mGuessingView.getGuessingInput();
        if(inputGuessing == null) inputGuessing = "";

        new OnlineInGameManager((MainActivity) mMainView).updateGuessingStepProgressAndUploadGuessing(GuessingPresenter.this, mOnlineGame, inputGuessing);
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
    public void stopCountDownTimer() {
        if(mCountDownTimer != null) mCountDownTimer.cancel();
    }

    @Override
    public void restartCountDownTimer() {
//        if(mCountDownTimer != null) mCountDownTimer.start();
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
        if(mIsInOfflineMode) {
            mMainPresenter.informToShowTapToNextStepUi();
            mGuessingView.hideViews();
            mGuessingView.initiateNextStepButton(mOfflineGame.getCurrentStep(), mOfflineGame.getTotalSteps());
            mGuessingView.showOfflineDrawing((Bitmap) mOfflineGame.getOfflineSettings().getGuessingAndDrawingsList().get(mOfflineGame.getCurrentStep() - 2));
        } else {
            mRoomListenerRegistration = new OnlineRoomManager((MainActivity) mMainView).syncRoomStatusWhileInGame(mMainView, mMainPresenter, mOnlineGame);
            new OnlineInGameManager((MainActivity) mMainView).retrieveDrawingAndWordCount(mGuessingView, this, mOnlineGame);

        }
    }

    /**
     * ***********************************************************************************
     * Offline Mode
     * ***********************************************************************************
     */
    @Override
    public void transToNextPage() {
        if(mOfflineGame.getCurrentStep() == mOfflineGame.getTotalSteps()) {
            finishGame(mOfflineGame);
        } else {
            mOfflineGame.increamentCurrentStep();
            transToDrawingPage();
        }
    }

    @Override
    public void saveGuessingAndTransToNextPage(String guessing) {
        mOfflineGame.getOfflineSettings().addItemToResultList(guessing);
        transToNextPage();
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

    public boolean isInOfflineMode() {
        return mIsInOfflineMode;
    }
}
