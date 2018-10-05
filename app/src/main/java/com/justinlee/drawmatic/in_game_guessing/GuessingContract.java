package com.justinlee.drawmatic.in_game_guessing;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;
import com.justinlee.drawmatic.objects.Game;

public interface GuessingContract {
    interface View extends BaseView<Presenter> {
        void showDrawing(String topic);

        void updateTimer(long currentCountDoenTime);

        void showCurrentStep(int currentStep, int numPlayers);

        void showWordCountHint(int wordCount);

        String getGuessingInput();
    }

    interface Presenter extends BasePresenter {
        void promptLeaveRoomAlert(GuessingFragment fragment);

        void leaveRoom(GuessingFragment fragment);

        void transToDrawingPage();

        void setDrawing(String imageUrl);

        void setAndStartTimer();

        void setCurrentStep();

        void startMonitoringPlayerGuessingProgress();

        void startGuessing();

        void unregisterListener();

        void setWordCountHint(int wordCount);

        void finishGame(Game game);
    }
}
