package com.justinlee.drawmatic.in_game_guessing;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;
import com.justinlee.drawmatic.in_game_set_topic.SetTopicFragment;

public interface GuessingContract {
    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
        void leaveRoom(SetTopicFragment fragment);

        void transToDrawingPage(GuessingFragment fragment);

        void finishGame();
    }
}
