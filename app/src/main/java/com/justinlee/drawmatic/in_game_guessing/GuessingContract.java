package com.justinlee.drawmatic.in_game_guessing;

import android.graphics.Bitmap;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;
import com.justinlee.drawmatic.objects.Game;

public interface GuessingContract {
    interface View extends BaseView<Presenter> {
        void showOnlineDrawing(String topic);

        void showPreviousPlayer(String previousPlayer);

        void updateTimer(long currentCountDoenTime);

        void showCurrentStep(int currentStep, int numPlayers);

        void showWordCountHint(int wordCount);

        String getGuessingInput();

        void hideViews();

        /**
         * Offline Mode
         */
        void initiateNextStepButton(int currentStep, int numPlayers);

        void showOfflineDrawing(Bitmap drawing);
    }

    interface Presenter extends BasePresenter {
        void informActivityToPromptLeaveGameAlert();

        void leaveRoom(GuessingFragment fragment);

        void transToDrawingPage();

        void setOnlineDrawing(String imageUrl);

        void setPreviousPlayer();

        void setAndStartTimer();

        void setAndStartTimeOutTimer();

        void setCurrentStep();

        void startMonitoringPlayerGuessingProgress();

        void startGuessing();

        void unregisterListener();

        void stopCountDownTimer();

        void stopTimeOutTimer();

        void restartCountDownTimer();

        void setWordCountHint(String theWord);

        void updateGuessingStepProgressAndUploadGuessing();

        void finishGame(Game game);

        void transToNextPage();

        void saveGuessingAndTransToNextPage(String guessing);
    }
}
