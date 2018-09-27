package com.justinlee.drawmatic.online_room_waiting;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;

public interface OnlineWaitingContract {
    interface View extends BaseView<Presenter> {
        void showRoomNameUi(String roomName);
    }

    interface Presenter extends BasePresenter {
        void leaveRoom(OnlineWaitingFragment fragment);

        void startPlayingOnline(OnlineWaitingFragment fragment);

        void informToHideLoadingUi(OnlineWaitingFragment fragment);

        void informToShowLoadingUi(OnlineWaitingFragment fragment);
    }

}
