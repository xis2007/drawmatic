package com.justinlee.drawmatic.in_game_drawing;

import android.os.CountDownTimer;

import com.divyanshu.draw.widget.DrawView;
import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.firabase_operation.FirestoreManager;
import com.justinlee.drawmatic.objects.Game;
import com.justinlee.drawmatic.objects.OfflineGame;
import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.util.LeaveGameBottomSheetDialog;

public class DrawingPresenter implements DrawingContract.Presenter {
    private static final String TAG = "justinx";

    private DrawingContract.View mDrawingView;

    private OnlineGame mOnlineGame;
    private OfflineGame mOfflineGame;

    public DrawingPresenter(DrawingContract.View setTopicView, Game game) {
        mDrawingView = setTopicView;
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
        LeaveGameBottomSheetDialog.newInstance((MainActivity) fragment.getActivity()).show(((MainActivity) fragment.getActivity()).getSupportFragmentManager(), "LEAVE_ROOM_ALERT");
    }

    @Override
    public void leaveRoom(DrawingFragment fragment) {

    }

    @Override
    public void transToGuessingPage(DrawingFragment fragment) {
        if (mOnlineGame != null) {
            ((MainActivity) fragment.getActivity()).getMainPresenter().transToGuessingPage(mOnlineGame);
        } else {
            ((MainActivity) fragment.getActivity()).getMainPresenter().transToGuessingPage(mOfflineGame);
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
        // TODO set topic after getting data from firestore
    }

    @Override
    public void setAndStartTimer() {
        new CountDownTimer((long) (mOnlineGame.getDrawingAndGuessingTimeAllowed() * 10 * 1000), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secUntilFinish = millisUntilFinished / 1000;
                mDrawingView.updateTimer(secUntilFinish);
            }

            @Override
            public void onFinish() {
                ((MainContract.View) ((DrawingFragment) mDrawingView).getActivity()).showLoadingUi();
//                new FirestoreManager(((SetTopicFragment) mDrawingView).getActivity()).updateCurrentStepProgressAndUploadTopic(mOnlineGame, ((SetTopicFragment) mSetTopicView).getEditTextTopicInput().getText().toString());
            }
        }.start();
    }

    @Override
    public void setCurrentStep() {
        if(mOnlineGame.isPlayersOddumbered()) {
            mDrawingView.showCurrentStep(mOnlineGame.getCurrentStep(), mOnlineGame.getTotalSteps());
        } else {
            mDrawingView.showCurrentStep(mOnlineGame.getCurrentStep(), mOnlineGame.getTotalSteps() + 1);
        }
    }

    @Override
    public void start() {
        setAndStartTimer();
        setCurrentStep();

        ((MainContract.View) ((DrawingFragment) mDrawingView).getActivity()).hideLoadingUi();
        new FirestoreManager(((DrawingFragment) mDrawingView).getActivity()).monitorDrawingProgress(mDrawingView, this, mOnlineGame);
    // TODO get topic (odd even numbers) ; set all players currentStepProgress to 0 (only game master does this); monitors when all players change progress to 1
    }
}
