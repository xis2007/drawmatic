package com.justinlee.drawmatic.online_cereate_room;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.constants.Constants;
import com.justinlee.drawmatic.firabase_operation.FirestoreManager;
import com.justinlee.drawmatic.objects.OnlineSettings;
import com.justinlee.drawmatic.objects.Player;

import java.util.ArrayList;

public class CreateRoomPresenter implements CreateRoomContract.Presenter {
    private static final String TAG = "justinx";

    private CreateRoomContract.View mCreateRoomView;
    private int mRoomType;

    public CreateRoomPresenter(CreateRoomContract.View createRoomView, int roomType) {
        mCreateRoomView = createRoomView;
        mCreateRoomView.setPresenter(this);

        mRoomType = roomType;
    }

    @Override
    public void createRoom(final CreateRoomFragment createRoomFragment, final String roomName, final int numPlayers, final float attemptTime) {
        Player roomMaster = ((MainPresenter) ((MainActivity) createRoomFragment.getActivity()).getMainPresenter()).getCurrentPlayer();
        roomMaster.setPlayerType(Constants.PlayerType.ROOM_MASTER);
        ArrayList<Player> playersList =  new ArrayList<>();
        playersList.add(roomMaster);

        final OnlineSettings onlineSettings = new OnlineSettings(mRoomType, roomName, numPlayers, attemptTime, playersList);

        new FirestoreManager(createRoomFragment.getContext()).createOnlineRoom(this, onlineSettings, mCreateRoomView);
    }

    @Override
    public void cancelRoomCreation(CreateRoomFragment createRoomFragment) {
        ((MainActivity) createRoomFragment.getActivity()).getMainPresenter().transToOnlinePage();
    }

    @Override
    public void transToRoomWaitingPage(CreateRoomFragment createRoomFragment, OnlineSettings onlineSettings) {
        ((MainActivity) createRoomFragment.getActivity()).getMainPresenter().transToOnlineWaitingPage(onlineSettings);
    }

    @Override
    public void informToHideLoadingUi(CreateRoomFragment createRoomFragment) {
        ((MainActivity) createRoomFragment.getActivity()).hideLoadingUi();
    }

    @Override
    public void informToShowLoadingUi(CreateRoomFragment createRoomFragment) {
        ((MainActivity) createRoomFragment.getActivity()).showLoadingUi();
    }

    @Override
    public void start() {

    }
}
