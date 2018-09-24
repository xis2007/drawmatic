package com.justinlee.drawmatic.in_game_guessing;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.in_game_set_topic.SetTopicFragment;
import com.justinlee.drawmatic.objects.Game;
import com.justinlee.drawmatic.objects.OfflineGame;
import com.justinlee.drawmatic.objects.OnlineGame;

public class GuessingPresenter implements GuessingContract.Presenter {
    private GuessingContract.View mGuessingView;
    private OnlineGame mOnlineGame;
    private OfflineGame mOfflineGame;

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
    public void leaveRoom(SetTopicFragment fragment) {

    }

    @Override
    public void transToDrawingPage(GuessingFragment fragment) {
        if (mOnlineGame != null) {
            ((MainActivity) fragment.getActivity()).getMainPresenter().transToDrawingPage(mOnlineGame);
        } else {
            ((MainActivity) fragment.getActivity()).getMainPresenter().transToDrawingPage(mOfflineGame);
        }

    }

    @Override
    public void finishGame() {

    }

    @Override
    public void start() {

    }
}
