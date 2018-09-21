package com.justinlee.drawmatic.online_cereate_room;

import com.justinlee.drawmatic.MainActivity;

public class CreateRoomPresenter implements CreateRoomContract.Presenter {
    private CreateRoomContract.View mCreateRoomView;

    public CreateRoomPresenter(CreateRoomContract.View createRoomView) {
        mCreateRoomView = createRoomView;
        mCreateRoomView.setPresenter(this);
    }

    @Override
    public void sendRoomCreationRequest() {

    }

    @Override
    public void cancelRoomCreation(CreateRoomFragment createRoomFragment) {
        ((MainActivity) createRoomFragment.getActivity()).getMainPresenter().transToOnlinePage();
    }

    @Override
    public void transToRoomWaitingPage(CreateRoomFragment createRoomFragment, String roomName, int numPlayers, float attemptTime) {
        ((MainActivity) createRoomFragment.getActivity()).getMainPresenter().transToOnlineWaitingPage(roomName, numPlayers, attemptTime);
    }

    @Override
    public void start() {

    }
}
