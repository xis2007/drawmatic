package com.justinlee.drawmatic.instructions;

import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.MainPresenter;

public class InstructionsPresenter implements InstructionsContract.Presenter {
    private MainContract.View mMainView;
    private MainContract.Presenter mMainPresenter;
    private InstructionsContract.View mInstructionsView;

    public InstructionsPresenter(InstructionsContract.View instructionsView) {
        mInstructionsView = instructionsView;
        mInstructionsView.setPresenter(this);
    }


    @Override
    public void start() {

    }



    /**
     * ***********************************************************************************
     * Set MainView and MainPresenters to get reference to them
     * ***********************************************************************************
     */
    public void setMainView(MainContract.View mainView) {
        mMainView = mainView;
    }


    public void setMainPresenter(MainPresenter mainPresenter) {
        mMainPresenter = mainPresenter;
    }

    /**
     * ***********************************************************************************
     * Getters and Setters
     * ***********************************************************************************
     */
    public MainContract.View getMainView() {
        return mMainView;
    }

    public MainContract.Presenter getMainPresenter() {
        return mMainPresenter;
    }
}
