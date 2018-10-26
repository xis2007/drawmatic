package com.justinlee.drawmatic.online.createroom;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.constants.Constants;
import com.justinlee.drawmatic.firabase.OnlineRoomManager;
import com.justinlee.drawmatic.objects.OfflineGame;
import com.justinlee.drawmatic.objects.OfflineSettings;
import com.justinlee.drawmatic.objects.OnlineGame;
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
    public void createRoom(String roomName, int numPlayers, float attemptTime) {
        if (Constants.NO_STRING.equals(roomName) || roomName.isEmpty()) {
            mCreateRoomView.promptNameInputAlert();

        } else {
            mMainPresenter.isLoading(((MainActivity) mMainView).getResources().getString(R.string.hint_loading_creating_room));
            Player roomMaster = ((MainPresenter) ((MainActivity) mMainView).getMainPresenter()).getCurrentPlayer();
            roomMaster.setPlayerType(Constants.PlayerType.ROOM_MASTER);
            ArrayList<Player> playersList =  new ArrayList<>();
            playersList.add(roomMaster);

            final OnlineSettings onlineSettings = new OnlineSettings(mRoomType, roomName, roomMaster.getPlayerName(), numPlayers, attemptTime, playersList);

            new OnlineRoomManager((MainActivity) mMainView).createOnlineRoom(this, onlineSettings, mCreateRoomView);
        }
    }


    @Override
    public void cancelRoomCreation() {
        mMainPresenter.isLoading(((MainActivity) mMainView).getResources().getString(R.string.hint_loading_cancel_room_creation));
        ((MainActivity) mMainView).getMainPresenter().transToPlayPage();
        mMainPresenter.isNotLoading();
    }

    @Override
    public void transToRoomWaitingPage(String roomId, OnlineSettings onlineSettings) {
        mMainPresenter.isLoading(((MainActivity) mMainView).getResources().getString(R.string.hint_loading_prepare_room));
        OnlineGame onlineGame = new OnlineGame(roomId, onlineSettings);
        ((MainActivity) mMainView).getMainPresenter().transToOnlineWaitingPage(onlineGame);
    }

    @Override
    public void start() {

    }


    /**
     * ***********************************************************************************
     * Offline Methods
     * ***********************************************************************************
     */
    @Override
    public void startOfflineGame(int numPlayers) {
        OfflineSettings offlineSettings = new OfflineSettings(Constants.GameMode.OFFLINE_NORMAL, numPlayers);
        OfflineGame offlineGame = new OfflineGame(offlineSettings);
        mMainPresenter.transToSetTopicPage(offlineGame);
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
