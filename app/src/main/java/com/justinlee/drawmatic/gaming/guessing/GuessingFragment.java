package com.justinlee.drawmatic.gaming.guessing;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.justinlee.drawmatic.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class GuessingFragment extends Fragment implements GuessingContract.View {
    private GuessingContract.Presenter mGuessingPresenter;

    private TextView mTextHintTitle;
    private EditText mEditTextGuessingInput;
    private Button mCurrentStepButton;
    private TextView mTextTimeRemaining;
    private ImageView mImageToGuess;
    private TextView mTextPreviousPlayer;

    public GuessingFragment() {
        // Required empty public constructor
    }

    public static GuessingFragment newInstance() {
        return new GuessingFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_guessing, container, false);

        initButtons(rootView);
        initViews(rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGuessingPresenter.start();
    }

    @Override
    public void setPresenter(GuessingContract.Presenter presenter) {
        mGuessingPresenter = checkNotNull(presenter);
    }


    public void initButtons(View rootView) {
        mCurrentStepButton = rootView.findViewById(R.id.button_steps_remaining_guessing);
        Button quitGameButton = rootView.findViewById(R.id.button_quit_game_guessing);

        mCurrentStepButton.setOnClickListener(guessingOnClickListener);
        quitGameButton.setOnClickListener(guessingOnClickListener);
    }


    private void initViews(View rootView) {
        mTextHintTitle = rootView.findViewById(R.id.text_hint_title_guessing);
        mEditTextGuessingInput = rootView.findViewById(R.id.edittext_topic_guessing);
        mTextTimeRemaining = rootView.findViewById(R.id.text_time_remaining_guessing);
        mImageToGuess = rootView.findViewById(R.id.image_drawing_from_previous);
        mTextPreviousPlayer = rootView.findViewById(R.id.text_hint_previous_player_guessing);
    }


    private View.OnClickListener guessingOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_steps_remaining_guessing:
                    mGuessingPresenter.updateGuessingStepProgressAndUploadGuessing();
                    break;

                case R.id.button_quit_game_guessing:
                    mGuessingPresenter.informActivityToPromptLeaveGameAlert();
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    public void showOnlineDrawing(String imageUrl) {
        Glide.with(getActivity())
                .load(imageUrl)
                .thumbnail(0.5f)
                .into(mImageToGuess);
    }


    @Override
    public void showPreviousPlayer(String previousPlayer) {
        mTextPreviousPlayer.setText(getString(R.string.hint_drawing_from) + previousPlayer);
    }

    @Override
    public void updateTimer(long currentCountDownTime) {
        mTextTimeRemaining.setText(String.valueOf(currentCountDownTime));
    }


    @Override
    public void showCurrentStep(int currentStep, int numPlayers) {
        mCurrentStepButton.setText("Step " + currentStep + "/" + numPlayers);
    }


    @Override
    public void showWordCountHint(int wordCount) {
        mTextHintTitle.setText(getString(R.string.hint_title_guess_1) + wordCount + getString(R.string.hint_title_guess_2));
    }


    @Override
    public String getGuessingInput() {
        return mEditTextGuessingInput.getText().toString();
    }

    /**
     * *********************************************************************************
     * Offline Mode
     * **********************************************************************************
     */
    @Override
    public void hideViews() {
        getActivity().findViewById(R.id.text_time_remaining_guessing).setVisibility(View.GONE);
        getActivity().findViewById(R.id.image_time_remaining_guessing).setVisibility(View.GONE);
        getActivity().findViewById(R.id.text_hint_previous_player_guessing).setVisibility(View.GONE);
    }

    @Override
    public void initiateNextStepButton(int currentStep, int numPlayers) {
        mCurrentStepButton.setText("Step " + currentStep + "/" + numPlayers + getString(R.string.hint_tap_to_next));
        mCurrentStepButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mGuessingPresenter.saveGuessingAndTransToNextPage(mEditTextGuessingInput.getText().toString());
                                }
                            });
    }


    @Override
    public void showOfflineDrawing(Bitmap drawing) {
        Glide.with(getActivity())
                .load(drawing)
                .thumbnail(0.5f)
                .into(mImageToGuess);
    }

    /**
     * *********************************************************************************
     * Fragment Lifecycle
     * **********************************************************************************
     */
    @Override
    public void onStart() {
        super.onStart();
        if (!((GuessingPresenter) mGuessingPresenter).isInOfflineMode()) {
            mGuessingPresenter.restartCountDownTimer();
        }
    }

    @Override
    public void onStop() {
        if (!((GuessingPresenter) mGuessingPresenter).isInOfflineMode()) {
            mGuessingPresenter.stopCountDownTimer();
            mGuessingPresenter.removeRoomListenerRegistration();
            mGuessingPresenter.saveUnproperlyProcessedData();
        }
        super.onStop();
    }
}
