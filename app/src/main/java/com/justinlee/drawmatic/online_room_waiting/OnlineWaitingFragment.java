package com.justinlee.drawmatic.online_room_waiting;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.adapters.RoomWaitingAdapter;
import com.justinlee.drawmatic.constants.Constants;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineWaitingFragment extends Fragment implements OnlineWaitingContract.View {
    private OnlineWaitingContract.Presenter mOnlineWaitingPresenter;
    private RecyclerView mPlayersRecyclerView;
    private RoomWaitingAdapter mAdapter;

    TextView mTextHintWaiting;

    public OnlineWaitingFragment() {
        // Required empty public constructor
    }

    public static OnlineWaitingFragment newInstance() {
        return new OnlineWaitingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new RoomWaitingAdapter((OnlineWaitingPresenter) mOnlineWaitingPresenter, ((OnlineWaitingPresenter) mOnlineWaitingPresenter).getOnlineSettings().getPlayers());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_online_waiting, container, false);

        mTextHintWaiting = rootView.findViewById(R.id.text_hint_waiting);
        initRecyclerView(rootView);
        setupButtonsAndViews(rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainContract.View) getActivity()).showLoadingUi();
        mOnlineWaitingPresenter.start();
    }

    private void initRecyclerView(View rootView) {
        mPlayersRecyclerView = rootView.findViewById(R.id.recyclerview_room_waiting);
        mPlayersRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mPlayersRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showRoomNameUi(String roomName) {
        mTextHintWaiting.setText(roomName);
    }

//    @Override
//    public void showLoadingSign() {
//        mLoadingLayout.setVisibility(View.VISIBLE);
//    }
//
//
//    @Override
//    public void hideLoadingSign() {
//        mLoadingLayout.setVisibility(View.GONE);
//    }

    @Override
    public void setPresenter(@NonNull OnlineWaitingContract.Presenter presenter) {
        mOnlineWaitingPresenter = checkNotNull(presenter);
    }


    public void setupButtonsAndViews(View rootView) {
        Button cancelButton = rootView.findViewById(R.id.button_leave_room_waiting);
        Button startGameButton = rootView.findViewById(R.id.button_start_game_waiting);

        cancelButton.setOnClickListener(buttonOnclickListener);
        startGameButton.setOnClickListener(buttonOnclickListener);

        if (((MainPresenter) ((MainActivity) getActivity()).getMainPresenter()).getCurrentPlayer().getPlayerType() == Constants.PlayerType.PARTICIPANT) {
            startGameButton.setVisibility(View.INVISIBLE);
            startGameButton.setClickable(false);
        } else {
            startGameButton.setVisibility(View.VISIBLE);
            startGameButton.setClickable(true);
        }
    }


    private View.OnClickListener buttonOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_leave_room_waiting:
                    mOnlineWaitingPresenter.informToShowLoadingUi();
                    mOnlineWaitingPresenter.leaveRoom(OnlineWaitingFragment.this);
                    break;

                case R.id.button_start_game_waiting:
                    //TODO not complete yet
//                    mOnlineWaitingPresenter.informToShowLoadingUi(OnlineWaitingFragment.this);
                    mOnlineWaitingPresenter.startPlayingOnline(OnlineWaitingFragment.this);
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * ***********************************************************************************
     * Getters and Setters
     * ***********************************************************************************
     */
    public RecyclerView getPlayersRecyclerView() {
        return mPlayersRecyclerView;
    }

    public RoomWaitingAdapter getAdapter() {
        return mAdapter;
    }
}
