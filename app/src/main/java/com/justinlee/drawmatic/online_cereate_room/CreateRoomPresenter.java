package com.justinlee.drawmatic.online_cereate_room;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.constants.Constants;
import com.justinlee.drawmatic.firabase_operation.FirestoreManager;
import com.justinlee.drawmatic.objects.OnlineSettings;
import com.justinlee.drawmatic.objects.Player;

import java.util.ArrayList;

public class CreateRoomPresenter implements CreateRoomContract.Presenter {
    private static final String TAG = "justinx";

    private MainContract.View mMainView;
    private MainContract.Presenter mMainPresenter;

    private CreateRoomContract.View mCreateRoomView;

    private int mRoomType;

    public CreateRoomPresenter(CreateRoomContract.View createRoomView, int roomType) {
        mCreateRoomView = createRoomView;
        mCreateRoomView.setPresenter(this);

        mRoomType = roomType;
    }

    @Override
    public void createRoom(final String roomName, final int numPlayers, final float attemptTime) {
        Player roomMaster = ((MainPresenter) ((MainActivity) mMainView).getMainPresenter()).getCurrentPlayer();
        roomMaster.setPlayerType(Constants.PlayerType.ROOM_MASTER);
        ArrayList<Player> playersList =  new ArrayList<>();
        playersList.add(roomMaster);

        final OnlineSettings onlineSettings = new OnlineSettings(mRoomType, roomName, numPlayers, attemptTime, playersList);

        new FirestoreManager((MainActivity) mMainView).createOnlineRoom(this, onlineSettings, mCreateRoomView);
    }

    @Override
    public void cancelRoomCreation() {
        ((MainActivity) mMainView).getMainPresenter().transToOnlinePage();
    }

    @Override
    public void transToRoomWaitingPage(OnlineSettings onlineSettings) {
        ((MainActivity) mMainView).getMainPresenter().transToOnlineWaitingPage(onlineSettings);
    }

    @Override
    public void informToHideLoadingUi() {
        mMainView.hideLoadingUi();
    }

    @Override
    public void informToShowLoadingUi() {
        mMainView.showLoadingUi();
    }

    @Override
    public void start() {

    }

    /**
     * ***********************************************************************************
     * Set MainView and MainPresenters to get reference to them
     * ***********************************************************************************
     */
    public void setMainView(MainContract.View mainView) {
        mMainView = mainView;
    }


    public void setMainPresenter(MainPresenter mainPresenter) {
        mMainPresenter = mainPresenter;
    }
}
