package com.justinlee.drawmatic.cereate_room;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justinlee.drawmatic.R;
import com.xw.repo.BubbleSeekBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateRoomFragment extends Fragment implements CreateRoomContract.View {
    private static final String TAG = "justin";

    private CreateRoomContract.Presenter mCreateRoomPresenter;
    private BubbleSeekBar mSeekbarMaxPlayers;
    private BubbleSeekBar mSeekbarAttemptTime;

    private int mMaxPlayerProgress;
    private int mAttemptTimeProgress;

    public CreateRoomFragment() {
        // Required empty public constructor
    }

    public static CreateRoomFragment newInstance() {
        return new CreateRoomFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_room, container, false);

        mSeekbarMaxPlayers = rootView.findViewById(R.id.seekbar_payer_number_create_room);
        mSeekbarAttemptTime = rootView.findViewById(R.id.seekbar_attempt_time_create_room);

        mSeekbarMaxPlayers.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                super.getProgressOnFinally(bubbleSeekBar, progress, progressFloat, fromUser);
                mMaxPlayerProgress = progress;
            }
        });

        mSeekbarAttemptTime.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                super.getProgressOnFinally(bubbleSeekBar, progress, progressFloat, fromUser);
                mAttemptTimeProgress = progress;
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCreateRoomPresenter.start();
    }

    @Override
    public void showCreatedRoomUi() {

    }

    @Override
    public void setPresenter(@NonNull CreateRoomContract.Presenter presenter) {
        mCreateRoomPresenter = presenter;
    }
}
