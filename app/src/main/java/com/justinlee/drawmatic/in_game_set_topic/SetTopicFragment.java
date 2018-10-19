package com.justinlee.drawmatic.in_game_set_topic;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.justinlee.drawmatic.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class SetTopicFragment extends Fragment implements SetTopicContract.View {
    private SetTopicContract.Presenter mSetTopicPresenter;

    Button mCurrentStepButton;
    Button mQuitGameButton;

    private TextView mTextTimeRemaining;
    private EditText mEditTextTopicInput;

    public SetTopicFragment() {
        // Required empty public constructor
    }

    public static SetTopicFragment newInstance() {
        return new SetTopicFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_set_topic, container, false);

        initButtons(rootView);
        initViews(rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSetTopicPresenter.start();
    }

    @Override
    public void setPresenter(SetTopicContract.Presenter presenter) {
        mSetTopicPresenter = checkNotNull(presenter);
    }

    public void initButtons(View rootView) {
        mCurrentStepButton = rootView.findViewById(R.id.button_steps_remaining_set_topic);
        mQuitGameButton = rootView.findViewById(R.id.button_quit_game_set_topic);

        mCurrentStepButton.setOnClickListener(setTopicOnClickListener);
        mQuitGameButton.setOnClickListener(setTopicOnClickListener);
    }

    private View.OnClickListener setTopicOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_steps_remaining_set_topic:
                    mSetTopicPresenter.transToDrawingPage();
                    break;

                case R.id.button_quit_game_set_topic:
                    mSetTopicPresenter.informActivityToPromptLeaveGameAlert();
                    break;

                default:
                    break;
            }
        }
    };

    private void initViews(View rootView) {
        mTextTimeRemaining = rootView.findViewById(R.id.text_time_remaining_set_topic);
        mEditTextTopicInput = rootView.findViewById(R.id.edittext_topic_input);
    }

    @Override
    public void updateTimer(long currentCountDoenTime) {
        mTextTimeRemaining.setText(String.valueOf(currentCountDoenTime));
    }

    @Override
    public void showCurrentStep(int currentStep, int numPlayers) {
        mCurrentStepButton.setText("Step " + currentStep + "/" + numPlayers);
    }

    public String getEditTextTopicInput() {
        return mEditTextTopicInput.getText().toString();
    }



    /**
     * *********************************************************************************
     * Offline Mode
     * **********************************************************************************
     */
    @Override
    public void hideTimer() {
        getActivity().findViewById(R.id.text_time_remaining_set_topic).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.image_time_remaining_set_topic).setVisibility(View.INVISIBLE);
    }

    @Override
    public void initiateNextStepButton() {
        getActivity()
                .findViewById(R.id.button_steps_remaining_set_topic)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSetTopicPresenter.saveGuessingAndTransToDrawingPage(mEditTextTopicInput.getText().toString());
                    }
                });
    }

    /**
     * *********************************************************************************
     * Fragment Lifecycle
     * **********************************************************************************
     */
    @Override
    public void onStart() {
        super.onStart();
        if (!((SetTopicPresenter) mSetTopicPresenter).isInOfflineMode()) {
            mSetTopicPresenter.restartCountDownTimer();
        }

    }

    @Override
    public void onStop() {
        if (!((SetTopicPresenter) mSetTopicPresenter).isInOfflineMode()) {
            mSetTopicPresenter.stopCountDownTimer();
            ((SetTopicPresenter) mSetTopicPresenter).getRoomListenerRegistration().remove();
        }
        super.onStop();
    }

    /**
     * *********************************************************************************
     * Getters and Setters
     * **********************************************************************************
     */
    public TextView getTextTimeRemaining() {
        return mTextTimeRemaining;
    }


}
