package com.justinlee.drawmatic.online_room_waiting;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;
import com.justinlee.drawmatic.objects.OnlineSettings;

import java.util.ArrayList;

public interface OnlineWaitingContract {
    interface View extends BaseView<Presenter> {
        void showRoomNameUi(String roomName);
    }

    interface Presenter extends BasePresenter {
        void leaveRoom(OnlineWaitingFragment fragment);

        void deleteRoom(OnlineWaitingFragment fragment);

        void startPlayingOnline(OnlineWaitingFragment fragment);

        void informToHideLoadingUi(OnlineWaitingFragment fragment);

        void informToShowLoadingUi(OnlineWaitingFragment fragment);

        void syncOnlineNewRoomStatus(ArrayList<OnlineSettings> newOnlineSettings);
    }

}
