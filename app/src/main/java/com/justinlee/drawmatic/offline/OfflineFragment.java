package com.justinlee.drawmatic.offline;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justinlee.drawmatic.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfflineFragment extends Fragment implements OfflineContract.View {
    private OfflineContract.Presenter mOfflinePresenter;

    public OfflineFragment() {
        // Required empty public constructor
    }

    public static OfflineFragment newInstance() {
        return new OfflineFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOfflinePresenter.start();
    }

    @Override
    public void showOfflinePageUi() {

    }

    @Override
    public void showOfflineSearchPageUi() {

    }

    @Override
    public void showOfflineGameSettingsPageUi() {

    }

    @Override
    public void setPresenter(@NonNull OfflineContract.Presenter presenter) {
        mOfflinePresenter = checkNotNull(presenter);
    }
}
