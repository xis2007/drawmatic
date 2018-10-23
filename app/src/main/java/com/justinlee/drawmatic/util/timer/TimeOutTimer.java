package com.justinlee.drawmatic.util.timer;

import android.os.CountDownTimer;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.firabase_operation.OnlineInGameManager;
import com.justinlee.drawmatic.objects.OnlineGame;

public class TimeOutTimer extends CountDownTimer {
    private MainActivity mMainActivity;
    private OnlineGame mOnlineGame;

    public TimeOutTimer(long millisInFuture, long countDownInterval, MainActivity mainActivity, OnlineGame onlineGame) {
        super(millisInFuture, countDownInterval);
        mMainActivity = mainActivity;
        mOnlineGame = onlineGame;
    }

    @Override
    public void onTick(long millisUntilFinished) {

    }

    @Override
    public void onFinish() {
        new OnlineInGameManager(mMainActivity).leaveRoomAndDeleteDataWhileInGame(mOnlineGame);
    }
}
