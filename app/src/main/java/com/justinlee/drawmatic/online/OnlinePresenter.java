package com.justinlee.drawmatic.online;

import android.app.Fragment;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.User.UserManager;
import com.justinlee.drawmatic.constants.Constants;
import com.justinlee.drawmatic.firabase_operation.FirestoreManager;
import com.justinlee.drawmatic.objects.OnlineSettings;
import com.justinlee.drawmatic.objects.Player;

import java.util.ArrayList;

public class OnlinePresenter implements OnlineContract.Presenter {
    private static final String TAG = "justin";

    OnlineContract.View mOnlineView;

    public OnlinePresenter(OnlineContract.View onlineView) {
        mOnlineView = onlineView;
        mOnlineView.setPresenter(this);
    }

    @Override
    public void createRoomForOnlineNormalMode(OnlineFragment onlineFragment) {
        ((MainActivity) onlineFragment.getActivity()).getMainPresenter().transToOnlineRoomCreationPage(Constants.OnlineGameMode.ONLINE_NORMAL);
    }

    @Override
    public void startPlayingOnline() {

    }

    @Override
    public void searchForRooms(OnlineFragment onlineFragment, String inputString) {
        // TODO query
        new FirestoreManager(((Fragment) mOnlineView).getActivity()).searchForRoom(onlineFragment, this, inputString);
    }

    @Override
    public void informToShowResultRooms(ArrayList<OnlineSettings> onlineRoomSettings) {
        ((OnlineFragment) mOnlineView).getSearchedRoomsAdapter().swapList(onlineRoomSettings);
        mOnlineView.showOnlineSearchPageUi();
    }

    @Override
    public void joinSelectedRoom(OnlineSettings onlineSettings) {
        Player userAsPlayer = new Player(UserManager.getInstance().getUserName(), UserManager.getInstance().getUserId(), Constants.PlayerType.PARTICIPANT, onlineSettings.getCurrentNumPlayers() + 1);
        new FirestoreManager(((Fragment) mOnlineView).getActivity()).joinRoom((OnlineFragment) mOnlineView, onlineSettings, this, userAsPlayer);
    }

    @Override
    public void informToTransToOnlineWaitingPage(OnlineSettings onlineSettings) {
        ((MainActivity) ((OnlineFragment) mOnlineView).getActivity()).getMainPresenter().transToOnlineWaitingPage(onlineSettings);
    }

    @Override
    public void start() {

    }
}
