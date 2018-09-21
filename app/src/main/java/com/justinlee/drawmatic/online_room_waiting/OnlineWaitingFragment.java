package com.justinlee.drawmatic.online_room_waiting;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justinlee.drawmatic.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineWaitingFragment extends Fragment implements OnlineWaitingContract.View {
    private OnlineWaitingContract.Presenter mOnlineWaitingresenter;

    public OnlineWaitingFragment() {
        // Required empty public constructor
    }

    public static OnlineWaitingFragment newInstance() {
        return new OnlineWaitingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_online_waiting, container, false);
    }

    @Override
    public void showTopicPageUi() {

    }

    @Override
    public void setPresenter(@NonNull OnlineWaitingContract.Presenter presenter) {
        mOnlineWaitingresenter = presenter;
    }
}
