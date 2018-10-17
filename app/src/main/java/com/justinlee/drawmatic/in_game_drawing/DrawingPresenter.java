package com.justinlee.drawmatic.in_game_drawing;

import android.os.CountDownTimer;

import com.divyanshu.draw.widget.DrawView;
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
import com.justinlee.drawmatic.util.TopicDrawingRetrievingUtil;

public class DrawingPresenter implements DrawingContract.Presenter {
    private static final String TAG = "justinx";

    private MainContract.View mMainView;
    private MainContract.Presenter mMainPresenter;
    private DrawingContract.View mDrawingView;

    private OnlineGame mOnlineGame;
    private OfflineGame mOfflineGame;

    private CountDownTimer mCountDownTimer;

    private ListenerRegistration mRoomListenerRegistration;
    private ListenerRegistration mDrawingListenerRegistration;

    public DrawingPresenter(DrawingContract.View drawingView, Game game) {
        mDrawingView = drawingView;
        mDrawingView.setPresenter(this);

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
    public void leaveRoom(DrawingFragment fragment) {

    }

    @Override
    public void transToGuessingPage() {
        if (mOnlineGame != null) {
            ((MainActivity) mMainView).getMainPresenter().transToGuessingPage(mOnlineGame);
        } else {
            ((MainActivity) mMainView).getMainPresenter().transToGuessingPage(mOfflineGame);
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
    public void setTopic(String topicString) {
        mDrawingView.showTopic(topicString);
    }

    @Override
    public void setPreviousPlayer() {
        mDrawingView.showPreviousPlayer(new TopicDrawingRetrievingUtil((MainActivity) mMainView, mOnlineGame, ((MainPresenter) mMainPresenter).getCurrentPlayer()).calcPlayerNameWhereTopicOrDrawingIsRetrieved());
    }

    @Override
    public void setAndStartTimer(final DrawingFragment drawingFragment) {
        mCountDownTimer = new CountDownTimer((long) (mOnlineGame.getDrawingAndGuessingTimeAllowed() * 20 * 1000), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secUntilFinish = millisUntilFinished / 1000;
                mDrawingView.updateTimer(secUntilFinish);
            }

            @Override
            public void onFinish() {
                mMainView.showLoadingUi(((MainActivity) mMainView).getResources().getString(R.string.hint_loading_loading_data));
                // TODO change below parameters
                new OnlineInGameManager((MainActivity) mMainView).uploadImageAndGetImageUrl(DrawingPresenter.this, mOnlineGame, ((MainActivity) mMainView).findViewById(R.id.drawView));

            }
        }.start();
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
    public void updateDrawingStepProgressAndUploadImageUrl(String downloadUrl) {
        new OnlineInGameManager((MainActivity) mMainView).updateDrawingStepProgressAndUploadImageUrl(DrawingPresenter.this, mOnlineGame, downloadUrl);

    }

    @Override
    public void saveUrlToOnlineGameObject(String downloadUrl) {
        mOnlineGame.getImageUrlStrings().add(downloadUrl);
    }

    @Override
    public void unregisterListener() {
        mDrawingListenerRegistration.remove();
    }

    @Override
    public void restartCountDownTimer() {
        mCountDownTimer.start();
    }

    @Override
    public void startDrawing() {
        mMainView.hideLoadingUi();
        setAndStartTimer((DrawingFragment) mDrawingView);
    }

    @Override
    public void start() {
        // start by preparing this step, need to get topic and set all player progress to 0 first
        mRoomListenerRegistration = new OnlineRoomManager((MainActivity) mMainView).syncRoomStatusWhileInGame(mMainView, mMainPresenter, mOnlineGame);
        new OnlineInGameManager((MainActivity) mMainView).retrieveTopic(mDrawingView, this, mOnlineGame);
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
