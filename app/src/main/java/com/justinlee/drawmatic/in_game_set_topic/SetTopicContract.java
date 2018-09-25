package com.justinlee.drawmatic.in_game_set_topic;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;

public interface SetTopicContract {
    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
        void promptLeaveRoomWarning(SetTopicFragment fragment);

        void leaveRoom(SetTopicFragment fragment);

        void transToDrawingPageOnline(SetTopicFragment fragment);
    }
}
