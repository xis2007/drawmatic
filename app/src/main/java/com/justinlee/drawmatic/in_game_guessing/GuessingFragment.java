package com.justinlee.drawmatic.in_game_guessing;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.justinlee.drawmatic.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuessingFragment extends Fragment implements GuessingContract.View {
    private GuessingContract.Presenter mGuessingPresenter;


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

        return rootView;
    }

    @Override
    public void setPresenter(GuessingContract.Presenter presenter) {
        mGuessingPresenter = checkNotNull(presenter);
    }

    public void initButtons(View rootView) {
        Button currentStepButton = rootView.findViewById(R.id.button_steps_remaining_guessing);
        Button quitGameButton = rootView.findViewById(R.id.button_quit_game_guessing);

        currentStepButton.setOnClickListener(guessingOnClickListener);
        quitGameButton.setOnClickListener(guessingOnClickListener);

    }

    private View.OnClickListener guessingOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_steps_remaining_guessing:
                    // TODO cancel this function in production
                    mGuessingPresenter.transToDrawingPage(GuessingFragment.this);
                    break;

                case R.id.button_quit_game_guessing:
                    mGuessingPresenter.promptLeaveRoomAlert(GuessingFragment.this);
                    break;

                default:
                    break;
            }

        }
    };
}
