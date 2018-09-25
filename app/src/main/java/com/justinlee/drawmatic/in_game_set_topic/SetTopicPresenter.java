package com.justinlee.drawmatic.in_game_set_topic;

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
    public void transToDrawingPageOnline(SetTopicFragment fragment) {
        if (mOnlineGame != null) {
            ((MainActivity) fragment.getActivity()).getMainPresenter().transToDrawingPage(mOnlineGame);
        } else {
            ((MainActivity) fragment.getActivity()).getMainPresenter().transToDrawingPage(mOfflineGame);
        }

    }

    @Override
    public void start() {

    }
}
