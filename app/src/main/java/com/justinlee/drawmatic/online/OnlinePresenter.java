package com.justinlee.drawmatic.online;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.constants.Constants;

public class OnlinePresenter implements OnlineContract.Presenter {
    private static final String TAG = "justin";

    OnlineContract.View mOnlineView;

    public OnlinePresenter(OnlineContract.View onlineView) {
        mOnlineView = onlineView;
        mOnlineView.setPresenter(this);
    }

    @Override
    public void goToOnlineNormalMode(OnlineFragment onlineFragment) {
        ((MainActivity) onlineFragment.getActivity()).getMainPresenter().transToOnlineRoomCreationPage(Constants.OnlineGameMode.ONLINE_NORMAL);
//        mOnlineView.showOnlineRoomCreationPageUi(Constants.OnlineGameMode.ONLINE_NORMAL);
    }

    @Override
    public void startPlayingOnline() {

    }

    @Override
    public void start() {

    }
}
