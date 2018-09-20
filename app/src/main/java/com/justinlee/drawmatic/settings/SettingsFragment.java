package com.justinlee.drawmatic.settings;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justinlee.drawmatic.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements SettingsContract.View {
    private SettingsContract.Presenter mSettingsPresenter;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSettingsPresenter.start();
    }

    @Override
    public void updateProfile() {

    }

    @Override
    public void setPresenter(@NonNull SettingsContract.Presenter presenter) {
        mSettingsPresenter = presenter;
    }
}
