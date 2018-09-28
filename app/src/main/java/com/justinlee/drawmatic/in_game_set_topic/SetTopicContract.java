package com.justinlee.drawmatic.in_game_set_topic;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;

public interface SetTopicContract {
    interface View extends BaseView<Presenter> {
        void updateTimer(long currentCountDoenTime);

        void showCurrentStep(int currentStep, int maxPlayers);
    }

    interface Presenter extends BasePresenter {
        void promptLeaveRoomWarning(SetTopicFragment fragment);

        void leaveRoom(SetTopicFragment fragment);

        void setAndStartTimer();

        void setCurrentStep();

        void transToDrawingPageOnline(SetTopicFragment fragment);
    }
}
