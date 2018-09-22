package com.justinlee.drawmatic.online_cereate_room;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.constants.Constants;
import com.justinlee.drawmatic.objects.OnlineRoom;
import com.justinlee.drawmatic.objects.Player;

import java.util.ArrayList;

public class CreateRoomPresenter implements CreateRoomContract.Presenter {
    private CreateRoomContract.View mCreateRoomView;
    private int mRoomType;

    public CreateRoomPresenter(CreateRoomContract.View createRoomView, int roomType) {
        mCreateRoomView = createRoomView;
        mCreateRoomView.setPresenter(this);

        mRoomType = roomType;
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
        // TODO from database
        Player roomMaster = new Player(Constants.PlayerType.ROOM_MASTER, "Justin", 1);
        ArrayList<Player> initialPlayerInRoom = new ArrayList<>();
        initialPlayerInRoom.add(roomMaster);

        OnlineRoom onlineRoom = new OnlineRoom(mRoomType, roomName, numPlayers, attemptTime, initialPlayerInRoom);

        ((MainActivity) createRoomFragment.getActivity()).getMainPresenter().transToOnlineWaitingPage(onlineRoom);

    }

    @Override
    public void start() {

    }
}
