package com.justinlee.drawmatic.in_game_drawing;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.divyanshu.draw.widget.DrawView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.justinlee.drawmatic.R;

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
        Log.d(TAG, "setPresenter: presenter set " + presenter.toString());
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
    }

    private View.OnClickListener drawingOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_steps_remaining_drawing:
                    // TODO cancel this function in production
                    mDrawingPresenter.transToGuessingPage(DrawingFragment.this);
                    break;

                case R.id.button_quit_game_drawing:
                    mDrawingPresenter.promptLeaveRoomAlert(DrawingFragment.this);
                    break;

                default:
                    break;
            }

        }
    };

    private void initDrawingView(View rootView) {
        mDrawView = rootView.findViewById(R.id.drawView);
        mDrawView.setStrokeWidth(5);
        mDrawView.setColor(R.color.colorAccent);
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
        mTextTopicDrawing.setText(topic);
    }

    @Override
    public void updateTimer(long currentCountDoenTime) {
        mTextTimeRemaining.setText(String.valueOf(currentCountDoenTime));
    }

    @Override
    public void showCurrentStep(int currentStep, int maxPlayers) {
        mCurrentStepButton.setText(currentStep + " / " + maxPlayers);
    }
}
