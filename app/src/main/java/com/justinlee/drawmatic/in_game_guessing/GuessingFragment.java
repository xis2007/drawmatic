package com.justinlee.drawmatic.in_game_guessing;


import android.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class GuessingFragment extends Fragment implements GuessingContract.View {
    private GuessingContract.Presenter mGuessingPresenter;

    private EditText mEditTextGuessingInput;
    private Button mCurrentStepButton;
    private TextView mTextTimeRemaining;
    private ImageView mImageToGuess;

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
        mEditTextGuessingInput = rootView.findViewById(R.id.edittext_topic_guessing);
        mTextTimeRemaining = rootView.findViewById(R.id.text_time_remaining_guessing);
        mImageToGuess = rootView.findViewById(R.id.image_drawing_from_previous);
    }

    private View.OnClickListener guessingOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_steps_remaining_guessing:
                    // TODO cancel this function in production
                    mGuessingPresenter.transToDrawingPage();
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
    public void showDrawing(String imageUrl) {
        Glide.with(getActivity())
                .load(imageUrl)
                .thumbnail(0.5f)
                .into(mImageToGuess);
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
        mEditTextGuessingInput.setHint("The drawing has " + wordCount + " words");
        mEditTextGuessingInput.setHintTextColor(getActivity().getResources().getColor(R.color.colorGrey));
    }

    @Override
    public String getGuessingInput() {
        return mEditTextGuessingInput.getText().toString();
    }

    @Override
    public void onStop() {
        ((GuessingPresenter) mGuessingPresenter).getCountDownTimer().cancel();
        ((GuessingPresenter) mGuessingPresenter).getRoomListenerRegistration().remove();
        super.onStop();
    }
}
