package com.justinlee.drawmatic.in_game_guessing;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;
import com.justinlee.drawmatic.objects.Game;

public interface GuessingContract {
    interface View extends BaseView<Presenter> {
        void showDrawing(String topic);

        void showPreviousPlayer(String previousPlayer);

        void updateTimer(long currentCountDoenTime);

        void showCurrentStep(int currentStep, int numPlayers);

        void showWordCountHint(int wordCount);

        String getGuessingInput();
    }

    interface Presenter extends BasePresenter {
        void informActivityToPromptLeaveGameAlert();

        void leaveRoom(GuessingFragment fragment);

        void transToDrawingPage();

        void setDrawing(String imageUrl);

        void setPreviousPlayer();

        void setAndStartTimer();

        void setCurrentStep();

        void startMonitoringPlayerGuessingProgress();

        void startGuessing();

        void unregisterListener();

        void restartCountDownTimer();

        void setWordCountHint(String theWord);

        void finishGame(Game game);
    }
}
