package com.justinlee.drawmatic.gaming.settopic;

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
import com.justinlee.drawmatic.util.timer.TimeOutTimer;

public class SetTopicPresenter implements SetTopicContract.Presenter {
    private MainContract.View mMainView;
    private MainContract.Presenter mMainPresenter;
    private SetTopicContract.View mSetTopicView;

    private boolean mIsInOfflineMode;
    private OnlineGame mOnlineGame;
    private OfflineGame mOfflineGame;

    private CountDownTimer mCountDownTimer;
    private long mCountDownTime = -1;
    private CountDownTimer mTimeOutTimer;

    private ListenerRegistration mRoomListenerRegistration;

    public SetTopicPresenter(SetTopicContract.View setTopicView, Game game) {
        mSetTopicView = setTopicView;
        mSetTopicView.setPresenter(this);

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
    public void leaveRoom(SetTopicFragment fragment) {

    }

    @Override
    public void setAndStartTimer() {
        mCountDownTimer = new CountDownTimer((long) (mOnlineGame.getSetTopicTimeAllowed() * 30 * 1000), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secUntilFinish = millisUntilFinished / 1000;
                mSetTopicView.updateTimer(secUntilFinish);
                mCountDownTime = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                mTimeOutTimer = null;
                updateSetTopicStepProgressAndUploadTopic();
                setAndStartTimeOutTimer();
            }
        }.start();
    }

    @Override
    public void updateSetTopicStepProgressAndUploadTopic() {
        mMainView.showLoadingUi(((MainActivity) mMainView).getResources().getString(R.string.hint_loading_loading_data));
        String inputTopic = mSetTopicView.getEditTextTopicInput();
        if (inputTopic == null) inputTopic = Constants.NO_STRING;

        new OnlineInGameManager((MainActivity) mMainView).updateSetTopicStepProgressAndUploadTopic(mOnlineGame, inputTopic);
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
    public void setCurrentStep() {
        mSetTopicView.showCurrentStep(mOnlineGame.getCurrentStep(), mOnlineGame.getTotalSteps());
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
                    mSetTopicView.updateTimer(secUntilFinish);
                    mCountDownTime = millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    updateSetTopicStepProgressAndUploadTopic();
                    setAndStartTimeOutTimer();
                }
            }.start();
        }
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
    public void transToDrawingPage() {
        if (mIsInOfflineMode) {
            mOfflineGame.increamentCurrentStep();
            ((MainActivity) mMainView).getMainPresenter().transToDrawingPage(mOfflineGame);
        } else {
            mOnlineGame.increamentCurrentStep();
            stopTimeOutTimer();
            ((MainActivity) mMainView).getMainPresenter().transToDrawingPage(mOnlineGame);
        }
    }


    @Override
    public void start() {
        if (mIsInOfflineMode) {
            mSetTopicView.hideTimer();
            mSetTopicView.initiateNextStepButton(mOfflineGame.getCurrentStep(), mOfflineGame.getTotalSteps());
        } else {
            setAndStartTimer();
            setCurrentStep();
            mRoomListenerRegistration = new OnlineRoomManager((MainActivity) mMainView).syncRoomStatusWhileInGame(mMainView, mMainPresenter, mOnlineGame);
            new OnlineInGameManager((MainActivity) mMainView).monitorSetTopicProgress(this, mOnlineGame);
        }
    }

    /**
     * *********************************************************************************
     * Offline Mode
     * **********************************************************************************
     */
    @Override
    public void saveGuessingAndTransToDrawingPage(String guessing) {
        mOfflineGame.getOfflineSettings().addItemToResultList(guessing);
        transToDrawingPage();
    }

    /**
     * *********************************************************************************
     * Getters and Setters
     * **********************************************************************************
     */
    public OnlineGame getOnlineGame() {
        return mOnlineGame;
    }

    public boolean isInOfflineMode() {
        return mIsInOfflineMode;
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
