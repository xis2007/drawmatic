package com.justinlee.drawmatic.in_game_result;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;

import java.util.ArrayList;

public interface GameResultContract {
    interface View extends BaseView<Presenter> {
        void showOnlineGameResults(ArrayList<String> resultStrings, ArrayList<String> authorStrings);

        void showOfflineGameResults(ArrayList<Object> resultObjects);
    }

    interface Presenter extends BasePresenter {
        void informActivityToPromptLeaveGameAlert();

        void informToShowOnlineGameResults(ArrayList<String> resultStrings);

        void deleteFirestoreRoomData();

        void deleteStorageImagesData();

        void doneViewingResult();
    }
}
