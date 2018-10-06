package com.justinlee.drawmatic.in_game_result;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;

import java.util.ArrayList;

public interface GameResultContract {
    interface View extends BaseView<Presenter> {
        void showResults(ArrayList<String> resultStrings);

    }

    interface Presenter extends BasePresenter {
        void informToShowResults(ArrayList<String> resultStrings);

        void deleteFirestoreRoomData();

        void deleteStorageImagesData();

        void doneViewingResult();
    }
}
