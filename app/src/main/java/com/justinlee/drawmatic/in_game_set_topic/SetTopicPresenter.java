package com.justinlee.drawmatic.in_game_set_topic;

import android.os.CountDownTimer;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.objects.Game;
import com.justinlee.drawmatic.objects.OfflineGame;
import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.util.LeaveGameBottomSheetDialog;

public class SetTopicPresenter implements SetTopicContract.Presenter {
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
        LeaveGameBottomSheetDialog.newInstance((MainActivity) fragment.getActivity()).show(((MainActivity) fragment.getActivity()).getSupportFragmentManager(), "LEAVE_ROOM_ALERT");
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
                ((MainActivity) ((SetTopicFragment) mSetTopicView).getActivity()).getMainPresenter().transToDrawingPage(mOnlineGame);
            }
        }.start();
    }

    @Override
    public void setCurrentStep() {
        mSetTopicView.showCurrentStep(mOnlineGame.getCurrentStep(), mOnlineGame.getTotalSteps());
    }

    @Override
    public void transToDrawingPageOnline(SetTopicFragment fragment) {
        if (mOnlineGame != null) {
            ((MainActivity) fragment.getActivity()).getMainPresenter().transToDrawingPage(mOnlineGame);
        } else {
            ((MainActivity) fragment.getActivity()).getMainPresenter().transToDrawingPage(mOfflineGame);
        }

    }

    @Override
    public void start() {
        setAndStartTimer();
        setCurrentStep();
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
}
