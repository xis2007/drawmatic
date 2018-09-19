package com.justinlee.drawmatic;

import com.justinlee.drawmatic.bases.BasePresenter;
import com.justinlee.drawmatic.bases.BaseView;

public interface MainContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }


}
