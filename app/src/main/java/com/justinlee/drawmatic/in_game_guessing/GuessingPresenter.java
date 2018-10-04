package com.justinlee.drawmatic.in_game_guessing;

import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.google.firebase.firestore.ListenerRegistration;
import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.firabase_operation.FirestoreManager;
import com.justinlee.drawmatic.objects.Game;
import com.justinlee.drawmatic.objects.OfflineGame;
import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.util.LeaveGameBottomSheetDialog;

public class GuessingPresenter implements GuessingContract.Presenter {
    private static final String TAG = "justinxxxxx";

    private MainContract.View mMainView;
    private MainContract.Presenter mMainPresenter;

    private GuessingContract.View mGuessingView;

    private OnlineGame mOnlineGame;
    private OfflineGame mOfflineGame;

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
    public void promptLeaveRoomAlert(GuessingFragment fragment) {
        LeaveGameBottomSheetDialog.newInstance((MainActivity) mMainView).show(((MainActivity) mMainView).getSupportFragmentManager(), "LEAVE_ROOM_ALERT");
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
        new CountDownTimer((long) (mOnlineGame.getDrawingAndGuessingTimeAllowed() * 20 * 1000), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secUntilFinish = millisUntilFinished / 1000;
                mGuessingView.updateTimer(secUntilFinish);
            }

            @Override
            public void onFinish() {
                mMainView.showLoadingUi();
                // TODO change below null parameter to string
                new FirestoreManager((MainActivity) mMainView).updateGuessingStepProgressAndUploadGuessing(GuessingPresenter.this, mOnlineGame, null);
            }
        }.start();
    }

    @Override
    public void setCurrentStep() {
        mGuessingView.showCurrentStep(mOnlineGame.getCurrentStep(), mOnlineGame.getTotalSteps());
    }

    @Override
    public void startMonitoringPlayerGuessingProgress() {
        mGuessingListenerRegistration = new FirestoreManager((MainActivity) mMainView).monitorGuessingProgress(mGuessingView, this, mOnlineGame);
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
    public void setWordCountHint(int wordCount) {
        mGuessingView.showWordCountHint(wordCount);
    }

    @Override
    public void finishGame() {
        mMainPresenter.transToOnlinePage();
        mMainPresenter.isNotLoading();
        Snackbar.make(((MainActivity) mMainView).findViewById(R.id.fragment_container_main), "game finished", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void start() {
        Log.d(TAG, "start: start to retrieve drawing");
        new FirestoreManager((MainActivity) mMainView).retrieveDrawingAndWordCount(mGuessingView, this, mOnlineGame);
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
