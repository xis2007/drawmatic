package com.justinlee.drawmatic.online;

import android.app.Fragment;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.User.UserManager;
import com.justinlee.drawmatic.constants.Constants;
import com.justinlee.drawmatic.firabase_operation.OnlineRoomManager;
import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.objects.Player;

import java.util.ArrayList;

public class OnlinePresenter implements OnlineContract.Presenter {
    private static final String TAG = "justin";

    private OnlineContract.View mOnlineView;

    private MainContract.View mMainView;
    private MainPresenter mMainPresenter;

    public OnlinePresenter(OnlineContract.View onlineView) {
        mOnlineView = onlineView;
        mOnlineView.setPresenter(this);
    }

    @Override
    public void createRoomForOnlineNormalMode() {
        mMainPresenter.transToOnlineRoomCreationPage(Constants.OnlineGameMode.ONLINE_NORMAL);
    }

    @Override
    public void informToTransToSearchRoomsPage() {
        mOnlineView.showSearchGamesPageUi();
    }


    @Override
    public void searchForRooms(OnlineFragment onlineFragment, String inputString) {
        mMainPresenter.isLoading(((MainActivity) mMainView).getResources().getString(R.string.hint_loading_search_rooms));
        new OnlineRoomManager(((Fragment) mOnlineView).getActivity()).searchForRoom(onlineFragment, this, inputString);
    }

    @Override
    public void informToShowResultRooms(ArrayList<OnlineGame> onlineGamesList) {
        ((OnlineFragment) mOnlineView).getSearchedRoomsAdapter().swapList(onlineGamesList);
//        mOnlineView.showSearchGamesPageUi();
        mMainPresenter.isNotLoading();
    }

    @Override
    public void joinSelectedRoom(OnlineGame onlineGame) {
        Player userAsPlayer = new Player(UserManager.getInstance().getUserName(), UserManager.getInstance().getUserId(), Constants.PlayerType.PARTICIPANT, onlineGame.getOnlineSettings().getCurrentNumPlayers() + 1);
        new OnlineRoomManager(((Fragment) mOnlineView).getActivity()).joinRoom((OnlineFragment) mOnlineView, onlineGame, this, userAsPlayer);

    }

    @Override
    public void informToTransToOnlineWaitingPage(OnlineGame onlineGame) {
        mMainPresenter.transToOnlineWaitingPage(onlineGame);
    }

    @Override
    public void informToShowNoRoomsResultFoundMessage() {
        mMainPresenter.isNotLoading();
        mOnlineView.showNoRoomsResultFoundMessage();
    }

    @Override
    public void informToShowRoomIsInGameMessage() {
        mOnlineView.showRoomIsInGameMessage();
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
