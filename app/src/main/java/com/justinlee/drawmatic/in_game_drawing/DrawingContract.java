package com.justinlee.drawmatic.in_game_drawing;

import com.divyanshu.draw.widget.DrawView;
import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;

public interface DrawingContract {
    interface View extends BaseView<Presenter> {
        void showTopic(String topic);

        void showPreviousPlayer(String previousPlayer);

        void updateTimer(long currentCountDoenTime);

        void showCurrentStep(int currentStep, int numPlayers);

        void hideViews();

        /**
         * Offline Mode
         */
        void initiateNextStepButton(int currentStep, int numPlayers);
    }

    interface Presenter extends BasePresenter {
        void informActivityToPromptLeaveGameAlert();

        void leaveRoom(DrawingFragment fragment);

        void transToGuessingPage();

        void clearDrawing(DrawView drawView);

        void undoDrawing(DrawView drawView);

        void redoDrawing(DrawView drawView);

        void setTopic(String topicString);

        void setPreviousPlayer();

        void setAndStartTimer();

        void setAndStartTimeOutTimer();

        void setCurrentStep();

        void startMonitoringPlayerProgress();

        void updateDrawingStepProgressAndUploadImageUrl(String downloadUrl);

        void saveUrlToOnlineGameObject(String downloadUrl);

        void unregisterListener();

        void uploadImageAndGetImageUrl();

        void stopCountDownTimer();

        void stopTimeOutTimer();

        void restartCountDownTimer();

        void removeRoomListenerRegistration();

        void saveUnproperlyProcessedData();

        void startDrawing();

        /**
         * Offline Mode
         */
        void saveDrawingAndTransToGuessingPage(DrawView drawView);
    }
}
