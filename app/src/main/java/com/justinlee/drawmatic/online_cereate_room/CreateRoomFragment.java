package com.justinlee.drawmatic.online_cereate_room;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Wave;
import com.justinlee.drawmatic.R;
import com.xw.repo.BubbleSeekBar;

import static com.google.common.base.Preconditions.checkNotNull;

public class CreateRoomFragment extends Fragment implements CreateRoomContract.View {
    private static final String TAG = "justin";

    private CreateRoomContract.Presenter mCreateRoomPresenter;

    private EditText mEditTextRoomName;
    private BubbleSeekBar mSeekbarMaxPlayers;
    private BubbleSeekBar mSeekbarAttemptTime;

    private int mMaxPlayerProgress = 4;
    private float mAttemptTimeProgress = 0.5f;

    private ConstraintLayout mLoadingLayout;
    private SpinKitView mLoadingKit;
    private Wave mLoadingKitEffect;

    public CreateRoomFragment() {
        // Required empty public constructor
    }


    public static CreateRoomFragment newInstance() {
        return new CreateRoomFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_room, container, false);

        setupEditText(rootView);
        setupSeekbar(rootView);
        setupButtonsAndViews(rootView);

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
    public void showLoadingSign() {
        mLoadingLayout.setVisibility(View.VISIBLE);
//        mLoadingKit.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingSign() {
        mLoadingLayout.setVisibility(View.GONE);
//        mLoadingKit.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(@NonNull CreateRoomContract.Presenter presenter) {
        mCreateRoomPresenter = checkNotNull(presenter);
    }

    private void setupEditText(View rootView) {
        mEditTextRoomName = rootView.findViewById(R.id.edittext_room_name_create_room);
    }


    public void setupSeekbar(View rootView) {
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
                mAttemptTimeProgress = progressFloat;
            }
        });
    }


    public void setupButtonsAndViews(View rootView) {
        mLoadingLayout = rootView.findViewById(R.id.layout_loading_create_room);
        mLoadingLayout.setVisibility(View.GONE);

        mLoadingKit = rootView.findViewById(R.id.loading_kit);
        mLoadingKitEffect = new Wave();
        mLoadingKit.setIndeterminateDrawable(mLoadingKitEffect);
//        mLoadingKit.setVisibility(View.GONE);

        Button cancelButton = rootView.findViewById(R.id.button_cancel_create_room);
        Button nextButton = rootView.findViewById(R.id.button_next_create_room);

        cancelButton.setOnClickListener(buttonOnclickListener);
        nextButton.setOnClickListener(buttonOnclickListener);

    }


    private View.OnClickListener buttonOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_cancel_create_room:
                    hideLoadingSign();
                    mCreateRoomPresenter.cancelRoomCreation(CreateRoomFragment.this);
                    break;

                case R.id.button_next_create_room:
                    //TODO not complete yet
                    showLoadingSign();
                    mCreateRoomPresenter.createRoom(CreateRoomFragment.this, mEditTextRoomName.getText().toString(), mMaxPlayerProgress, mAttemptTimeProgress);
//                    mCreateRoomPresenter.transToRoomWaitingPage(CreateRoomFragment.this, mEditTextRoomName.getText().toString(), mMaxPlayerProgress, mAttemptTimeProgress);
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
    public SpinKitView getLoadingKit() {
        return mLoadingKit;
    }

    public Wave getLoadingKitEffect() {
        return mLoadingKitEffect;
    }
}
