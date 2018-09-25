package com.justinlee.drawmatic.in_game_drawing;

import com.divyanshu.draw.widget.DrawView;
import com.justinlee.drawmatic.MainActivity;
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
    public void start() {

    }
}
