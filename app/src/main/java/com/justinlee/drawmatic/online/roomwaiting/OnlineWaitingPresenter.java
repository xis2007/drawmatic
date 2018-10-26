package com.justinlee.drawmatic.online.roomwaiting;

import com.google.firebase.firestore.ListenerRegistration;
import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.firabase.OnlineRoomManager;
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

    private ListenerRegistration mListenerRegistration;

    public OnlineWaitingPresenter(OnlineWaitingContract.View onlineWaitingView, Game game) {
        mOnlineWaitingView = onlineWaitingView;
        mOnlineWaitingView.setPresenter(this);

        if (game instanceof OnlineGame) {
            mOnlineGame = (OnlineGame) game;
            mOfflineGame = null;
        } else {
            mOnlineGame = null;
            mOfflineGame = ((OfflineGame) game);
        }
    }

    @Override
    public void leaveRoom(final OnlineWaitingFragment fragment) {
        mMainPresenter.isLoading(((MainActivity) mMainView).getResources().getString(R.string.hint_loading_leaving_room));
        Player userAsPlayer = ((MainPresenter) ((MainActivity) mMainView).getMainPresenter()).getCurrentPlayer();;
        new OnlineRoomManager((MainActivity) mMainView).leaveRoom(fragment, mOnlineGame, userAsPlayer);
    }


    @Override
    public void informToTransToOnlinePage() {
        mMainPresenter.transToPlayPage();
        mMainPresenter.isNotLoading();
    }

    @Override
    public void startPlayingOnline() {
        mMainPresenter.isLoading(((MainActivity) mMainView).getResources().getString(R.string.hint_loading_starting_game));
        mMainPresenter.transToSetTopicPage(mOnlineGame);

        mMainPresenter.isNotLoading();
    }

    @Override
    public void updateOnlineRoomStatus(ArrayList<OnlineGame> newOnlineGameList) {
        mOnlineGame = newOnlineGameList.get(0);
        if (mOnlineGame != null) {
            ((OnlineWaitingFragment) mOnlineWaitingView).getAdapter().swapList(mOnlineGame.getOnlineSettings().getPlayers());
            mOnlineWaitingView.showRoomStatusUi(mOnlineGame);
        }
        mMainPresenter.isNotLoading();
    }

    @Override
    public void setGameStatusToInGame() {
        if (mOnlineGame.getOnlineSettings().getPlayers().size() < 4) {
            mOnlineWaitingView.showNotEnoughPlayersMessage((MainActivity) mMainView);
        } else {
            new OnlineRoomManager((MainActivity) mMainView).setGameStatusToInGame(this, mOnlineGame);
        }
    }

    @Override
    public void informToShowRoomClosedMessage() {
        mOnlineWaitingView.showRoomClosedMessage((MainActivity) mMainView);
    }

    @Override
    public void start() {
        // TODO search and check for the room created on server
        mOnlineWaitingView.showRoomStatusUi(mOnlineGame);
        mListenerRegistration = new OnlineRoomManager((MainActivity) mMainView).syncRoomStatus(mOnlineWaitingView, this, mOnlineGame);
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

    public ListenerRegistration getListenerRegistration() {
        return mListenerRegistration;
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
