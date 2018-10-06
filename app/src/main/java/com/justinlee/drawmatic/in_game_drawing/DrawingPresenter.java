package com.justinlee.drawmatic.in_game_drawing;

import android.os.CountDownTimer;
import android.util.Log;

import com.divyanshu.draw.widget.DrawView;
import com.google.firebase.firestore.ListenerRegistration;
import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.firabase_operation.OnlineInGameManager;
import com.justinlee.drawmatic.objects.Game;
import com.justinlee.drawmatic.objects.OfflineGame;
import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.util.LeaveGameBottomSheetDialog;

public class DrawingPresenter implements DrawingContract.Presenter {
    private static final String TAG = "justinx";

    private MainContract.View mMainView;
    private MainContract.Presenter mMainPresenter;

    private DrawingContract.View mDrawingView;

    private OnlineGame mOnlineGame;
    private OfflineGame mOfflineGame;

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
    public void promptLeaveRoomAlert(DrawingFragment fragment) {
        LeaveGameBottomSheetDialog.newInstance((MainActivity) mMainView).show(((MainActivity) mMainView).getSupportFragmentManager(), "LEAVE_ROOM_ALERT");
    }

    @Override
    public void leaveRoom(DrawingFragment fragment) {

    }

    @Override
    public void transToGuessingPage() {
        if (mOnlineGame != null) {
            Log.d(TAG, "transToGuessingPage: trasiting to guessing page");
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
    public void setAndStartTimer(final DrawingFragment drawingFragment) {
        new CountDownTimer((long) (mOnlineGame.getDrawingAndGuessingTimeAllowed() * 20 * 1000), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secUntilFinish = millisUntilFinished / 1000;
                mDrawingView.updateTimer(secUntilFinish);
            }

            @Override
            public void onFinish() {
                mMainView.showLoadingUi();
                // TODO change below parameters
//                new FirestoreManager((MainActivity) mMainView).uploadImageAndGetImageUrl(DrawingPresenter.this, mOnlineGame, ((MainActivity) mMainView).findViewById(R.id.drawView));
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
//        mDrawingListenerRegistration = new FirestoreManager((MainActivity) mMainView).monitorDrawingProgress(mDrawingView, this, mOnlineGame);
        mDrawingListenerRegistration = new OnlineInGameManager((MainActivity) mMainView).monitorDrawingProgress(mDrawingView, this, mOnlineGame);
    }

    @Override
    public void updateDrawingStepProgressAndUploadImageUrl(String downloadUrl) {
//        new FirestoreManager((MainActivity) mMainView).updateDrawingStepProgressAndUploadImageUrl(DrawingPresenter.this, mOnlineGame, downloadUrl);
        new OnlineInGameManager((MainActivity) mMainView).updateDrawingStepProgressAndUploadImageUrl(DrawingPresenter.this, mOnlineGame, downloadUrl);

    }

    @Override
    public void unregisterListener() {
        mDrawingListenerRegistration.remove();
    }

    @Override
    public void startDrawing() {
        mMainView.hideLoadingUi();
        setAndStartTimer((DrawingFragment) mDrawingView);
    }

    @Override
    public void start() {
        // start by preparing this step, need to get topic and set all player progress to 0 first
//        new FirestoreManager((MainActivity) mMainView).retrieveTopic(mDrawingView, this, mOnlineGame);
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
}
