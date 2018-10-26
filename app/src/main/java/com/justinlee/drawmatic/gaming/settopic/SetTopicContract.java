package com.justinlee.drawmatic.gaming.settopic;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;

public interface SetTopicContract {
    interface View extends BaseView<Presenter> {
        void updateTimer(long currentCountDownTime);

        void showCurrentStep(int currentStep, int maxPlayers);

        String getEditTextTopicInput();

        void hideTimer();

        /**
         * Offline Mode
         */
        void initiateNextStepButton(int currentStep, int numPlayers);
    }

    interface Presenter extends BasePresenter {
        void informActivityToPromptLeaveGameAlert();

        void leaveRoom(SetTopicFragment fragment);

        void setAndStartTimer();

        void setCurrentStep();

        void stopCountDownTimer();

        void stopTimeOutTimer();

        void restartCountDownTimer();

        void transToDrawingPage();

        void updateSetTopicStepProgressAndUploadTopic();

        void setAndStartTimeOutTimer();

        void removeRoomListenerRegistration();

        void saveUnproperlyProcessedData();

        /**
         * Offline Mode
         */
        void saveGuessingAndTransToDrawingPage(String guessing);
    }
}
