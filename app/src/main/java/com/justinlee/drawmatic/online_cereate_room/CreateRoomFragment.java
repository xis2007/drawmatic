package com.justinlee.drawmatic.online_cereate_room;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.justinlee.drawmatic.R;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;
import com.suke.widget.SwitchButton;
import com.xw.repo.BubbleSeekBar;

import static com.google.common.base.Preconditions.checkNotNull;

public class CreateRoomFragment extends Fragment implements CreateRoomContract.View {
    private static final String TAG = "justin";

    private CreateRoomContract.Presenter mCreateRoomPresenter;

    private SwitchButton mGameModeswitchButton;

    private EditText mEditTextRoomName;
    private BubbleSeekBar mSeekbarMaxPlayers;
    private BubbleSeekBar mSeekbarAttemptTime;
    private SingleSelectToggleGroup mAttemptTimeToggleGroup;
    private SingleSelectToggleGroup mNumPlayerToggleGroup;

    private int mNumPlayerProgress = 4;
    private float mAttemptTimeProgress = 0.5f;


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
    public void promptNameInputAlert() {
        Snackbar.make(getActivity().findViewById(R.id.fragment_container_main), "Room Name is Empty", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void promptRoomExistingAlert() {
        Snackbar.make(getActivity().findViewById(R.id.fragment_container_main), "Room Already Exists", Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public void setPresenter(@NonNull CreateRoomContract.Presenter presenter) {
        mCreateRoomPresenter = checkNotNull(presenter);
    }


    private void setupEditText(View rootView) {
        mEditTextRoomName = rootView.findViewById(R.id.edittext_room_name_create_room);
    }


    public void setupSeekbar(View rootView) {
//        mSeekbarMaxPlayers = rootView.findViewById(R.id.seekbar_payer_number_create_room);
//        mSeekbarAttemptTime = rootView.findViewById(R.id.seekbar_attempt_time_create_room);

//        mSeekbarMaxPlayers.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
//            @Override
//            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
//                super.getProgressOnFinally(bubbleSeekBar, progress, progressFloat, fromUser);
//                mNumPlayerProgress = progress;
//            }
//        });

//        mSeekbarAttemptTime.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
//            @Override
//            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
//                super.getProgressOnFinally(bubbleSeekBar, progress, progressFloat, fromUser);
//                mAttemptTimeProgress = progressFloat;
//            }
//        });

        mNumPlayerToggleGroup = rootView.findViewById(R.id.toggleGroup_numPlayer);
        mNumPlayerToggleGroup.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.numPlayer_4:
                        mNumPlayerProgress = 4;
                        break;
                    case R.id.numPlayer_5:
                        mNumPlayerProgress = 5;
                        break;
                    case R.id.numPlayer_6:
                        mNumPlayerProgress = 6;
                        break;
                    case R.id.numPlayer_7:
                        mNumPlayerProgress = 7;
                        break;
                    case R.id.numPlayer_8:
                        mNumPlayerProgress = 8;
                        break;
                    case R.id.numPlayer_9:
                        mNumPlayerProgress = 9;
                        break;
                    case R.id.numPlayer_10:
                        mNumPlayerProgress = 10;
                        break;
                    case R.id.numPlayer_11:
                        mNumPlayerProgress = 11;
                        break;
                    case R.id.numPlayer_12:
                        mNumPlayerProgress = 12;
                        break;
                    default:
                        break;
                }
                Log.d(TAG, "onCheckedChanged: num player is: " + mNumPlayerProgress);
            }
        });

        mAttemptTimeToggleGroup = rootView.findViewById(R.id.toggleGroup_attemptTime);
        mAttemptTimeToggleGroup.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.min_05:
                        mAttemptTimeProgress = 0.5f;
                        break;
                    case R.id.min_10:
                        mAttemptTimeProgress = 1.0f;
                        break;
                    case R.id.min_15:
                        mAttemptTimeProgress = 1.5f;
                        break;
                    case R.id.min_20:
                        mAttemptTimeProgress = 2.0f;
                        break;
                    default:
                        break;
                }
                Log.d(TAG, "onCheckedChanged: attempt time is: " + mAttemptTimeProgress);
            }
        });
    }


    public void setupButtonsAndViews(final View rootView) {
        mGameModeswitchButton = rootView.findViewById(R.id.switch_button);
        mGameModeswitchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked) {
                    switchToOfflineUi(rootView);
                } else {
                    switchToOnlineUi(rootView);
                }
            }
        });


        // buttons for confirmation or cancellation of room creation
        Button cancelButton = rootView.findViewById(R.id.button_cancel_create_room);
        Button nextButton = rootView.findViewById(R.id.button_next_create_room);

        cancelButton.setOnClickListener(buttonOnclickListener);
        nextButton.setOnClickListener(buttonOnclickListener);
    }


    private void switchToOfflineUi(View rootView) {
        LinearLayout roomNameLayout = rootView.findViewById(R.id.layout_room_name_create_room);
        LinearLayout timeAttemptLayout = rootView.findViewById(R.id.layout_time_attempt_create_room);
        roomNameLayout.setVisibility(View.GONE);
        timeAttemptLayout.setVisibility(View.GONE);

        TextView attemptTimeNote = rootView.findViewById(R.id.textView7);
        attemptTimeNote.setVisibility(View.GONE);

        TextView tagMaxPlayers = rootView.findViewById(R.id.text_max_players_create_room);
        tagMaxPlayers.setText("Number of Players");
    }

    private void switchToOnlineUi(View rootView) {
        LinearLayout roomNameLayout = rootView.findViewById(R.id.layout_room_name_create_room);
        LinearLayout timeAttemptLayout = rootView.findViewById(R.id.layout_time_attempt_create_room);
        roomNameLayout.setVisibility(View.VISIBLE);
        timeAttemptLayout.setVisibility(View.VISIBLE);

        TextView attemptTimeNote = rootView.findViewById(R.id.textView7);
        attemptTimeNote.setVisibility(View.VISIBLE);

        TextView tagMaxPlayers = rootView.findViewById(R.id.text_max_players_create_room);
        tagMaxPlayers.setText("Maximum Players");
    }

    private View.OnClickListener buttonOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_cancel_create_room:
                    mCreateRoomPresenter.cancelRoomCreation();
                    break;

                case R.id.button_next_create_room:
                    if(mGameModeswitchButton.isChecked()) {
                        mCreateRoomPresenter.startOfflineGame(mNumPlayerProgress);
                    } else {
                        mCreateRoomPresenter.createRoom(mEditTextRoomName.getText().toString(), mNumPlayerProgress, mAttemptTimeProgress);
                    }
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

}
