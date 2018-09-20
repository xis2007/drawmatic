package com.justinlee.drawmatic.cereate_room;

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
    public void start() {

    }
}
