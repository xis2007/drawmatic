package com.justinlee.drawmatic.online_room_waiting;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.firabase_operation.OnlineRoomManager;
import com.justinlee.drawmatic.objects.Game;
import com.justinlee.drawmatic.objects.OfflineGame;
import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.objects.Player;

import java.util.ArrayList;

public class OnlineWaitingPresenter implements OnlineWaitingContract.Presenter {
    private MainContract.View mMainView;
    private MainContract.Presenter mMainPresenter;

    private OnlineWaitingContract.View mOnlineWaitingView;

    private OnlineGame mOnlineGame;
    private OfflineGame mOfflineGame;

    public OnlineWaitingPresenter(OnlineWaitingContract.View onlineWaitingView, Game game) {
        mOnlineWaitingView = onlineWaitingView;
        mOnlineWaitingView.setPresenter(this);

        if (game instanceof OnlineGame) {
            mOnlineGame = (OnlineGame) game;
            mOfflineGame = null;
        }
//        else {
//            mOnlineSettings = null;
//            mOfflineSettings = ((OfflineGame) game);
//        }
    }

    @Override
    public void leaveRoom(final OnlineWaitingFragment fragment) {
        mMainPresenter.isLoading();
        Player userAsPlayer = ((MainPresenter) ((MainActivity) mMainView).getMainPresenter()).getCurrentPlayer();;
//        new FirestoreManager((MainActivity) mMainView).leaveRoom(fragment, mOnlineSettings, userAsPlayer);
        new OnlineRoomManager((MainActivity) mMainView).leaveRoom(fragment, mOnlineGame, userAsPlayer);

    }

//    @Override
//    public void deleteRoom() {
////        new FirestoreManager((MainActivity) mMainView).deleteRoom(this, mOnlineSettings, mOnlineWaitingView);
//
//        new OnlineRoomManager((MainActivity) mMainView).deleteRoom(this, mRoomId, mOnlineSettings, mOnlineWaitingView);
//    }

    @Override
    public void informToTransToOnlinePage() {
        mMainPresenter.transToOnlinePage();
        mMainPresenter.isNotLoading();
    }

    @Override
    public void startPlayingOnline() {
        mMainPresenter.isLoading();
        mMainPresenter.transToSetTopicPage(mOnlineGame);

        mMainPresenter.isNotLoading();
    }

    @Override
    public void updateOnlineRoomStatus(ArrayList<OnlineGame> newOnlineGameList) {

        mOnlineGame = newOnlineGameList.get(0);
        if (mOnlineGame != null) {
            ((OnlineWaitingFragment) mOnlineWaitingView).getAdapter().swapList(mOnlineGame.getOnlineSettings().getPlayers());
        }
        mMainPresenter.isNotLoading();
    }

    @Override
    public void setGameStatusToInGame() {
//        new FirestoreManager((MainActivity) mMainView).setGameStatusToInGame((OnlineWaitingFragment) mOnlineWaitingView, mOnlineGame);
        new OnlineRoomManager((MainActivity) mMainView).setGameStatusToInGame(this, mOnlineGame);
    }

    @Override
    public void informToShowRoomClosedMessage() {
        mOnlineWaitingView.showRoomClosedMessage();
    }

    @Override
    public void start() {
        // TODO search and check for the room created on server
        mOnlineWaitingView.showRoomNameUi(mOnlineGame.getOnlineSettings().getRoomName());
//        new FirestoreManager((MainActivity) mMainView).syncRoomStatus(mOnlineWaitingView, this, mOnlineSettings);
        new OnlineRoomManager((MainActivity) mMainView).syncRoomStatus(mOnlineWaitingView, this, mOnlineGame);
    }


    /**
     * ***********************************************************************************
     * Getters and Setters
     * ***********************************************************************************
     */
    public OnlineWaitingContract.View getOnlineWaitingView() {
        return mOnlineWaitingView;
    }

    public OnlineGame getOnlineGame() {
        return mOnlineGame;
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
