package com.justinlee.drawmatic.in_game_set_topic;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.justinlee.drawmatic.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetTopicFragment extends Fragment implements SetTopicContract.View {
    private SetTopicContract.Presenter mSetTopicPresenter;

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
        Button currentStepButton = rootView.findViewById(R.id.button_steps_remaining_set_topic);
        Button quitGameButton = rootView.findViewById(R.id.button_quit_game_set_topic);

        currentStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO cancel this function in production
                mSetTopicPresenter.transToDrawingPageOnline(SetTopicFragment.this);
            }
        });

    }
}
