package com.justinlee.drawmatic.cereate_room;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;

public interface CreateRoomContract {
    interface View extends BaseView<Presenter> {
        void showCreatedRoomUi();
    }

    interface Presenter extends BasePresenter {
        void sendRoomCreationRequest();
    }
}
