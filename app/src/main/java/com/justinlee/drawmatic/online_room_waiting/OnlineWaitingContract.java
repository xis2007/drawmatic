package com.justinlee.drawmatic.online_room_waiting;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;
import com.justinlee.drawmatic.objects.OnlineGame;

import java.util.ArrayList;

public interface OnlineWaitingContract {
    interface View extends BaseView<Presenter> {
        void showRoomNameUi(String roomName);

        void showRoomClosedMessage();
    }

    interface Presenter extends BasePresenter {
        void leaveRoom(OnlineWaitingFragment fragment);

//        void deleteRoom();

        void informToTransToOnlinePage();

        void startPlayingOnline();

        void updateOnlineRoomStatus(ArrayList<OnlineGame> newOnlineGame);

        void setGameStatusToInGame();

        void informToShowRoomClosedMessage();
    }

}
