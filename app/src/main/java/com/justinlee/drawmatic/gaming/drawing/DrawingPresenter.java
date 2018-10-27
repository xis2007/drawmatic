package com.justinlee.drawmatic.gaming.drawing;

import android.graphics.Bitmap;
import android.os.CountDownTimer;

import com.divyanshu.draw.widget.DrawView;
import com.google.firebase.firestore.ListenerRegistration;
import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.firabase.OnlineExpiredDataManager;
import com.justinlee.drawmatic.firabase.OnlineInGameManager;
import com.justinlee.drawmatic.firabase.OnlineRoomManager;
import com.justinlee.drawmatic.objects.Game;
import com.justinlee.drawmatic.objects.OfflineGame;
import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.util.DrawViewToImageGenerator;
import com.justinlee.drawmatic.util.TopicDrawingRetrievingUtil;
import com.justinlee.drawmatic.util.timer.TimeOutTimer;

public class DrawingPresenter implements DrawingContract.Presenter {
    private static final String TAG = "justinx";

    private MainContract.View mMainView;
    private MainContract.Presenter mMainPresenter;
    private DrawingContract.View mDrawingView;

    private boolean mIsInOfflineMode;
    private OnlineGame mOnlineGame;
    private OfflineGame mOfflineGame;

    private CountDownTimer mCountDownTimer;
    private CountDownTimer mTimeOutTimer;

    private ListenerRegistration mRoomListenerRegistration;
    private ListenerRegistration mDrawingListenerRegistration;

    public DrawingPresenter(DrawingContract.View drawingView, Game game) {
        mDrawingView = drawingView;
        mDrawingView.setPresenter(this);

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
    public void leaveRoom(DrawingFragment fragment) {

    }

    @Override
    public void transToGuessingPage() {
        if (mIsInOfflineMode) {
            mOfflineGame.increamentCurrentStep();
            ((MainActivity) mMainView).getMainPresenter().transToGuessingPage(mOfflineGame);
        } else {
            mOnlineGame.increamentCurrentStep();
            stopTimeOutTimer();
            ((MainActivity) mMainView).getMainPresenter().transToGuessingPage(mOnlineGame);
        }
    }

    @Override
    public void clearDrawing(DrawView drawView) {
        drawView.clearCanvas();
    }

    @Override
    public void undoDrawing(DrawView drawView) {
        drawView.undo();
    }

    @Override
    public void redoDrawing(DrawView drawView) {
        drawView.redo();
    }

    @Override
    public void prepareToDraw(String topicString) {
        setTopic(topicString);
        setCurrentStep();
        setPreviousPlayer();
        startMonitoringPlayerProgress();
    }

    @Override
    public void setTopic(String topicString) {
        mDrawingView.showTopic(topicString);
    }

    @Override
    public void setPreviousPlayer() {
        mDrawingView.showPreviousPlayer(new TopicDrawingRetrievingUtil((MainActivity) mMainView,
                mOnlineGame,
                ((MainPresenter) mMainPresenter).getCurrentPlayer()).calcPlayerNameWhereTopicOrDrawingIsRetrieved());
    }

    @Override
    public void setAndStartTimer() {
        mCountDownTimer = new CountDownTimer((long) (mOnlineGame.getDrawingAndGuessingTimeAllowed() * 60 * 1000), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secUntilFinish = millisUntilFinished / 1000;
                mDrawingView.updateTimer(secUntilFinish);
            }

            @Override
            public void onFinish() {
                uploadImageAndGetImageUrl();
                setAndStartTimeOutTimer();
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
    public void uploadImageAndGetImageUrl() {
        mMainView.showLoadingUi(((MainActivity) mMainView).getResources().getString(R.string.hint_loading_loading_data));
        // TODO change below parameters
        new OnlineInGameManager((MainActivity) mMainView)
                .uploadImageAndGetImageUrl(DrawingPresenter.this, mOnlineGame, ((MainActivity) mMainView).findViewById(R.id.drawView));

    }

    @Override
    public void setCurrentStep() {
        mDrawingView.showCurrentStep(mOnlineGame.getCurrentStep(), mOnlineGame.getTotalSteps());
    }

    @Override
    public void startMonitoringPlayerProgress() {
        mDrawingListenerRegistration = new OnlineInGameManager((MainActivity) mMainView).monitorDrawingProgress(mDrawingView, this, mOnlineGame);
    }

    @Override
    public void updateAndSaveImageUrl(String downloadUrl) {
        updateDrawingStepProgressAndUploadImageUrl(downloadUrl);
        saveUrlToOnlineGameObject(downloadUrl);
    }

    @Override
    public void updateDrawingStepProgressAndUploadImageUrl(String downloadUrl) {
        new OnlineInGameManager((MainActivity) mMainView).updateDrawingStepProgressAndUploadImageUrl(DrawingPresenter.this, mOnlineGame, downloadUrl);
    }

    @Override
    public void saveUrlToOnlineGameObject(String downloadUrl) {
        mOnlineGame.getImageUrlStrings().add(downloadUrl);
    }

    @Override
    public void completedDrawing() {
        transToGuessingPage();
        unregisterListener();
    }

    @Override
    public void unregisterListener() {
        mDrawingListenerRegistration.remove();
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
//        if(mCountDownTimer != null) mCountDownTimer.start();
    }

    @Override
    public void startDrawing() {
        mMainView.hideLoadingUi();
        setAndStartTimer();
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
            mDrawingView.hideViews();
            mDrawingView.initiateNextStepButton(mOfflineGame.getCurrentStep(), mOfflineGame.getTotalSteps());
            mDrawingView.showTopic((String) mOfflineGame.getOfflineSettings().getGuessingAndDrawingsList().get(mOfflineGame.getCurrentStep() - 2));
        } else {
            // start by preparing this step, need to get topic and set all player progress to 0 first
            mRoomListenerRegistration = new OnlineRoomManager((MainActivity) mMainView).syncRoomStatusWhileInGame(mMainView, mMainPresenter, mOnlineGame);
            new OnlineInGameManager((MainActivity) mMainView).retrieveTopic(mDrawingView, this, mOnlineGame);
        }
    }

    /**
     * ***********************************************************************************
     * Offline Mode
     * ***********************************************************************************
     */
    @Override
    public void saveDrawingAndTransToGuessingPage(DrawView drawView) {
        Bitmap drawingBitmap = new DrawViewToImageGenerator().generateBitmapFrom(drawView);
        mOfflineGame.getOfflineSettings().addItemToResultList(drawingBitmap);
        transToGuessingPage();
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
