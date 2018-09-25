package com.justinlee.drawmatic.in_game_guessing;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;

public interface GuessingContract {
    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
        void promptLeaveRoomAlert(GuessingFragment fragment);

        void leaveRoom(GuessingFragment fragment);

        void transToDrawingPage(GuessingFragment fragment);

        void finishGame();
    }
}
