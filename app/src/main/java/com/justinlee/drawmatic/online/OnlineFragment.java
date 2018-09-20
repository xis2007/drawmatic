package com.justinlee.drawmatic.online;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justinlee.drawmatic.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineFragment extends Fragment implements OnlineContract.View {
    private OnlineContract.Presenter mOnlinePresenter;

    public OnlineFragment() {
        // Required empty public constructor
    }

    public static OnlineFragment newInstance() {
        return new OnlineFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_online, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOnlinePresenter.start();
    }



    @Override
    public void showOnlinePageUi() {

    }

    @Override
    public void showOnlineSearchPageUi() {

    }

    @Override
    public void showOnlineRoomCreationPageUi() {

    }

    @Override
    public void showOnlineGameSettingsPageUi() {

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setPresenter(@NonNull OnlineContract.Presenter presenter) {
        mOnlinePresenter = presenter;
    }
}
