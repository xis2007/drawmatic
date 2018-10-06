package com.justinlee.drawmatic.in_game_set_topic;

import android.os.CountDownTimer;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.firabase_operation.OnlineInGameManager;
import com.justinlee.drawmatic.objects.Game;
import com.justinlee.drawmatic.objects.OfflineGame;
import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.util.LeaveGameBottomSheetDialog;

public class SetTopicPresenter implements SetTopicContract.Presenter {
    private MainContract.View mMainView;
    private MainContract.Presenter mMainPresenter;

    private SetTopicContract.View mSetTopicView;

    private OnlineGame mOnlineGame;
    private OfflineGame mOfflineGame;

    public SetTopicPresenter(SetTopicContract.View setTopicView, Game game) {
        mSetTopicView = setTopicView;
        mSetTopicView.setPresenter(this);

        if (game instanceof OnlineGame) {
            mOnlineGame = (OnlineGame) game;
            mOfflineGame = null;
        } else {
            mOnlineGame = null;
            mOfflineGame = (OfflineGame) game;
        }
    }

    @Override
    public void promptLeaveRoomWarning(SetTopicFragment fragment) {
        LeaveGameBottomSheetDialog.newInstance((MainActivity) mMainView).show(((MainActivity) mMainView).getSupportFragmentManager(), "LEAVE_ROOM_ALERT");
    }

    @Override
    public void leaveRoom(SetTopicFragment fragment) {

    }

    @Override
    public void setAndStartTimer() {
        new CountDownTimer(10 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secUntilFinish = millisUntilFinished / 1000;
                mSetTopicView.updateTimer(secUntilFinish);
            }

            @Override
            public void onFinish() {
                mMainView.showLoadingUi();
//                new FirestoreManager((MainActivity) mMainView).updateSetTopicStepProgressAndUploadTopic(mOnlineGame, ((SetTopicFragment) mSetTopicView).getEditTextTopicInput().getText().toString());
                new OnlineInGameManager((MainActivity) mMainView).updateSetTopicStepProgressAndUploadTopic(mOnlineGame, ((SetTopicFragment) mSetTopicView).getEditTextTopicInput().getText().toString());
            }
        }.start();
    }

    @Override
    public void setCurrentStep() {
        mSetTopicView.showCurrentStep(mOnlineGame.getCurrentStep(), mOnlineGame.getTotalSteps());
    }

    @Override
    public void transToDrawingPageOnline() {
        if (mOnlineGame != null) {
            ((MainActivity) mMainView).getMainPresenter().transToDrawingPage(mOnlineGame);
        } else {
            ((MainActivity) mMainView).getMainPresenter().transToDrawingPage(mOfflineGame);
        }

    }

    @Override
    public void start() {
        setAndStartTimer();
        setCurrentStep();
//        new FirestoreManager((MainActivity) mMainView).monitorSetTopicProgress(mMainView, this, mOnlineGame);
        new OnlineInGameManager((MainActivity) mMainView).monitorSetTopicProgress(mMainView, this, mOnlineGame);
    }



    /**
     * *********************************************************************************
     * Getters and Setters
     * **********************************************************************************
     */
    public OnlineGame getOnlineGame() {
        return mOnlineGame;
    }

    public OfflineGame getOfflineGame() {
        return mOfflineGame;
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
