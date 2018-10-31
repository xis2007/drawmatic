package com.justinlee.drawmatic.gaming.guessing;

import android.graphics.Bitmap;
import android.os.CountDownTimer;

import com.google.firebase.firestore.ListenerRegistration;
import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.constants.Constants;
import com.justinlee.drawmatic.firabase.OnlineExpiredDataManager;
import com.justinlee.drawmatic.firabase.OnlineInGameManager;
import com.justinlee.drawmatic.firabase.OnlineRoomManager;
import com.justinlee.drawmatic.objects.Game;
import com.justinlee.drawmatic.objects.OfflineGame;
import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.util.StringUtil;
import com.justinlee.drawmatic.util.TopicDrawingRetrievingUtil;
import com.justinlee.drawmatic.util.timer.TimeOutTimer;

public class GuessingPresenter implements GuessingContract.Presenter {
    private MainContract.View mMainView;
    private MainContract.Presenter mMainPresenter;
    private GuessingContract.View mGuessingView;

    private boolean mIsInOfflineMode;
    private OnlineGame mOnlineGame;
    private OfflineGame mOfflineGame;

    private CountDownTimer mCountDownTimer;
    private long mCountDownTime = -1;
    private CountDownTimer mTimeOutTimer;

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
        if (mIsInOfflineMode) {
            ((MainActivity) mMainView).getMainPresenter().transToDrawingPage(mOfflineGame);
        } else {
            stopTimeOutTimer();
            ((MainActivity) mMainView).getMainPresenter().transToDrawingPage(mOnlineGame);

        }
    }

    @Override
    public void prepareToGuess(String previousTopic, String imageUrl) {
        setWordCountHint(previousTopic);
        setOnlineDrawing(imageUrl);
        setPreviousPlayer();
        setCurrentStep();
        startMonitoringPlayerGuessingProgress();
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
                setAndStartTimeOutTimer();
                updateGuessingStepProgressAndUploadGuessing();
            }
        }.start();
    }

    /**
     * After each round ends, this timer should be started to count for 15 minutes
     * After 15 minutes, if not all players are finished, it means somehting is wrong and will be timed out
     */
    @Override
    public void setAndStartTimeOutTimer() {
        mTimeOutTimer = new TimeOutTimer((long) (15 * 1000), 1000, (MainActivity) mMainView, mOnlineGame).start();
    }

    @Override
    public void updateGuessingStepProgressAndUploadGuessing() {
        mMainPresenter.isLoading(((MainActivity) mMainView).getResources().getString(R.string.hint_loading_loading_data));
        String inputGuessing = mGuessingView.getGuessingInput();
        if (inputGuessing == null) inputGuessing = Constants.NO_STRING;

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
        if (mCountDownTimer != null) mCountDownTimer.cancel();
    }

    @Override
    public void stopTimeOutTimer() {
        if (mTimeOutTimer != null) mTimeOutTimer.cancel();
    }

    @Override
    public void restartCountDownTimer() {
        if (mCountDownTime > 0) {
            mCountDownTimer = new CountDownTimer(mCountDownTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long secUntilFinish = millisUntilFinished / 1000;
                    mGuessingView.updateTimer(secUntilFinish);
                    mCountDownTime = millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    setAndStartTimeOutTimer();
                    updateGuessingStepProgressAndUploadGuessing();
                }
            }.start();
        }
    }

    @Override
    public void setWordCountHint(String theWord) {
        if (StringUtil.isEmptyString(theWord)) {
            mGuessingView.showWordCountHint(0);
        } else {
            mGuessingView.showWordCountHint(theWord.length());
        }
    }

    @Override
    public void completedGame(Game game) {
        finishGame(game);
        unregisterListener();
    }

    @Override
    public void completedGuessing() {
        mOnlineGame.increamentCurrentStep();
        transToDrawingPage();
        unregisterListener();
    }

    @Override
    public void finishGame(Game game) {
        stopTimeOutTimer();
        mMainPresenter.transToGameResultPage(game);
    }

    @Override
    public void removeRoomListenerRegistration() {
        mRoomListenerRegistration.remove();
    }

    @Override
    public void saveUnproperlyProcessedData() {
        OnlineExpiredDataManager onlineExpiredDataManager = new OnlineExpiredDataManager((MainActivity) mMainView);
        onlineExpiredDataManager.saveRoomToPref(mOnlineGame.getRoomId());
        onlineExpiredDataManager.saveDataToPref(mOnlineGame.getImageUrlStrings());
    }

    @Override
    public void start() {
        if (mIsInOfflineMode) {
            mMainPresenter.informToShowTapToNextStepUi();
            mGuessingView.hideViews();
            mGuessingView.initiateNextStepButton(mOfflineGame.getCurrentStep(), mOfflineGame.getTotalSteps());
            mGuessingView.showOfflineDrawing((Bitmap) mOfflineGame.getOfflineSettings().getGuessingAndDrawingsList().get(mOfflineGame.getCurrentStep() - 2));
            setWordCountHint((String) mOfflineGame.getOfflineSettings().getGuessingAndDrawingsList().get(mOfflineGame.getCurrentStep() - 3));

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
        if (mOfflineGame.getCurrentStep() == mOfflineGame.getTotalSteps()) {
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
    public boolean isInOfflineMode() {
        return mIsInOfflineMode;
    }
}
