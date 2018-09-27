package com.justinlee.drawmatic.online;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.justinlee.drawmatic.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineFragment extends Fragment implements OnlineContract.View, View.OnClickListener {
    private OnlineContract.Presenter mOnlinePresenter;

    private EditText mEdittextSearchForRooms;
    private Button mButtonCreateOnlineNormalRoom;
    private Button mButtonCreateOnlineTeamRoom;

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
        View rootView = inflater.inflate(R.layout.fragment_online, container, false);

        mEdittextSearchForRooms = rootView.findViewById(R.id.edittext_searchbox_online);
        mButtonCreateOnlineNormalRoom = rootView.findViewById(R.id.button_online_normal_mode);

        // TODO
//        mEdittextSearchForRooms.();
        mButtonCreateOnlineNormalRoom.setOnClickListener(this);

        return rootView;
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
    public void showOnlineRoomCreationPageUi(int roomType) {

    }

    @Override
    public void showOnlineGameSettingsPageUi() {

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setPresenter(OnlineContract.Presenter presenter) {
        mOnlinePresenter = checkNotNull(presenter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_online_normal_mode:
                mOnlinePresenter.goToOnlineNormalMode(this);
                break;

            case R.id.button_online_team_mode:
                break;

            default:
                break;
        }
    }
}
