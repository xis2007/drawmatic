package com.justinlee.drawmatic.in_game_drawing;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.divyanshu.draw.widget.DrawView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.util.StringUtil;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrawingFragment extends Fragment implements DrawingContract.View {
    private static final String TAG = "justinx";

    private DrawingContract.Presenter mDrawingPresenter;
    private BottomNavigationViewEx mInGameNavigation;

    private DrawView mDrawView;
    private Button mCurrentStepButton;
    private TextView mTextTimeRemaining;
    private TextView mTextTopicDrawing;
    private TextView mTextPreviousPlayer;

    public DrawingFragment() {
        // Required empty public constructor
    }

    public static DrawingFragment newInstance() {
        return new DrawingFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_drawing, container, false);

        initButtons(rootView);
        initDrawingView(rootView);
        initBottomNav(rootView);
        initViews(rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDrawingPresenter.start();
    }

    @Override
    public void setPresenter(DrawingContract.Presenter presenter) {
        mDrawingPresenter = checkNotNull(presenter);
    }

    private void initButtons(View rootView) {
        mCurrentStepButton = rootView.findViewById(R.id.button_steps_remaining_drawing);
        Button quitGameButton = rootView.findViewById(R.id.button_quit_game_drawing);

        mCurrentStepButton.setOnClickListener(drawingOnClickListener);
        quitGameButton.setOnClickListener(drawingOnClickListener);
    }

    private void initViews(View rootView) {
        mTextTimeRemaining = rootView.findViewById(R.id.text_time_remaining_drawing);
        mTextTopicDrawing = rootView.findViewById(R.id.textView_topic_drawing);
        mTextPreviousPlayer = rootView.findViewById(R.id.text_hint_previous_player_drawing);
    }

    private View.OnClickListener drawingOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_steps_remaining_drawing:
                    // TODO cancel this function in production
                    mDrawingPresenter.transToGuessingPage();
                    break;

                case R.id.button_quit_game_drawing:
                    mDrawingPresenter.informActivityToPromptLeaveGameAlert();
                    break;

                default:
                    break;
            }

        }
    };

    private void initDrawingView(View rootView) {
        mDrawView = rootView.findViewById(R.id.drawView);
        mDrawView.setStrokeWidth(10);
        mDrawView.setColor(R.color.colorGreyDark);
    }

    private void initBottomNav(View rootView) {
        mInGameNavigation = rootView.findViewById(R.id.bottom_nav_in_game);
        mInGameNavigation.enableAnimation(false);
        mInGameNavigation.enableShiftingMode(false);

        mInGameNavigation.setOnNavigationItemSelectedListener(inGameNavListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener inGameNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_clear:
                    mDrawingPresenter.clearDrawing(mDrawView);
                    return true;
                case R.id.navigation_undo:
                    mDrawingPresenter.undoDrawing(mDrawView);
                    return true;
                case R.id.navigation_redo:
                    mDrawingPresenter.redoDrawing(mDrawView);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void showTopic(String topic) {
        if(StringUtil.isEmptyString(topic)) {
            mTextTopicDrawing.setText("Oops, the previous player has nothing for you");
        } else {
            mTextTopicDrawing.setText("Your topic is:   " + topic);
        }

    }

    @Override
    public void showPreviousPlayer(String previousPlayer) {
        mTextPreviousPlayer.setText("Topic from: " + previousPlayer);
    }

    @Override
    public void updateTimer(long currentCountDoenTime) {
        mTextTimeRemaining.setText(String.valueOf(currentCountDoenTime));
    }

    @Override
    public void showCurrentStep(int currentStep, int numPlayers) {
        mCurrentStepButton.setText("Step " + currentStep + "/" + numPlayers);
    }


    /**
     * *********************************************************************************
     * Fragment Lifecycle
     * **********************************************************************************
     */

    @Override
    public void onStart() {
        super.onStart();
        mDrawingPresenter.restartCountDownTimer();
    }

    @Override
    public void onStop() {
        mDrawingPresenter.stopCountDownTimer();
        ((DrawingPresenter) mDrawingPresenter).getRoomListenerRegistration().remove();
        super.onStop();
    }
}
