package com.justinlee.drawmatic.in_game_result;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.adapters.GameResultPagerAdapter;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameResultFragment extends Fragment implements GameResultContract.View {
    private static final String TAG = "justinxxxxx";

    private GameResultContract.Presenter mGameResultPresenter;

    private ViewPager mGameResultViewPager;
    private GameResultPagerAdapter mPagerAdapter;
    private Button mDoneButton;

    public GameResultFragment() {
        // Required empty public constructor
    }

    public static GameResultFragment newInstance() {
        return new GameResultFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_result, container, false);

        initViewPager(rootView);
        initViews(rootView);

        return rootView;
    }


    private void initViewPager(View rootView) {
        mGameResultViewPager = rootView.findViewById(R.id.gameResultViewPager);
    }


    private void initViews(View rootView) {
        mDoneButton = rootView.findViewById(R.id.button_quit_game_game_result);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameResultPresenter.doneViewingResult();
            }
        });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGameResultPresenter.start();
    }


    @Override
    public void showOnlineGameResults(ArrayList<String> resultStrings, ArrayList<String> authorStrings) {
        mPagerAdapter = new GameResultPagerAdapter(getActivity(), resultStrings, authorStrings);
        mGameResultViewPager.removeAllViewsInLayout();
        mGameResultViewPager.setAdapter(mPagerAdapter);
        ExtensiblePageIndicator extensiblePageIndicator = getActivity().findViewById(R.id.flexibleIndicator);
        extensiblePageIndicator.initViewPager(mGameResultViewPager);
    }

    @Override
    public void setPresenter(GameResultContract.Presenter presenter) {
        mGameResultPresenter = presenter;
    }

    /**
     * ***********************************************************************************
     * Offline Mode
     * ***********************************************************************************
     */
    @Override
    public void showOfflineGameResults(ArrayList<Object> resultObjects) {
        mPagerAdapter = new GameResultPagerAdapter(getActivity(), resultObjects);
        mGameResultViewPager.removeAllViewsInLayout();
        mGameResultViewPager.setAdapter(mPagerAdapter);
        mGameResultViewPager.setOffscreenPageLimit(5);
        ExtensiblePageIndicator extensiblePageIndicator = getActivity().findViewById(R.id.flexibleIndicator);
        extensiblePageIndicator.initViewPager(mGameResultViewPager);
    }
}
