package com.justinlee.drawmatic.online;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.adapters.SearchedRoomsAdapter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineFragment extends Fragment implements OnlineContract.View, View.OnClickListener {
    private OnlineContract.Presenter mOnlinePresenter;

    private EditText mEdittextSearchForRooms;
    private Button mButtonCreateOnlineNormalRoom;
    private Button mButtonCreateOnlineTeamRoom;

    private RecyclerView mSearchResultRecyclerView;
    private SearchedRoomsAdapter mSearchedRoomsAdapter;


    public OnlineFragment() {
        // Required empty public constructor
    }


    public static OnlineFragment newInstance() {
        return new OnlineFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_online, container, false);

        mButtonCreateOnlineNormalRoom = rootView.findViewById(R.id.button_online_normal_mode);
        mEdittextSearchForRooms = rootView.findViewById(R.id.edittext_searchbox_online);
        mSearchResultRecyclerView = rootView.findViewById(R.id.room_list_recyclerview_online);

        mButtonCreateOnlineNormalRoom.setOnClickListener(this);
        mEdittextSearchForRooms.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if("".equals(v.getText().toString()) || v.getText().toString().isEmpty()) {
                    exitSearch();
                    Snackbar.make(getActivity().findViewById(R.id.boxes_scrollview_online), "Need to input something", Snackbar.LENGTH_SHORT).show();
                } else {
                    exitSearch();
                    mOnlinePresenter.searchForRooms(OnlineFragment.this, v.getText().toString());
                }
                return true;
            }
        });

        mSearchResultRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSearchedRoomsAdapter = new SearchedRoomsAdapter(OnlineFragment.this, null);
        mSearchResultRecyclerView.setAdapter(mSearchedRoomsAdapter);

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOnlinePresenter.start();
    }



    @Override
    public void showOnlinePageUi() {

    }


    @Override
    public void showOnlineSearchPageUi() {
        mSearchResultRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideOnlineSearchPageUi() {
        mSearchResultRecyclerView.setVisibility(View.GONE);
    }


    @Override
    public void showOnlineRoomCreationPageUi(int roomType) {

    }


    @Override
    public void showOnlineGameSettingsPageUi() {

    }


    @SuppressLint("RestrictedApi")
    @Override
    public void setPresenter(OnlineContract.Presenter presenter) {
        mOnlinePresenter = checkNotNull(presenter);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_online_normal_mode:
                mOnlinePresenter.createRoomForOnlineNormalMode(this);
                break;

            case R.id.button_online_team_mode:
                break;

            default:
                break;
        }
    }


    private void exitSearch() {
        mEdittextSearchForRooms.clearFocus();
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mEdittextSearchForRooms.getWindowToken(), 0);
    }



    /** **********************************************************************************
     *  Getters and Setters
     *  **********************************************************************************
     */
    public OnlineContract.Presenter getOnlinePresenter() {
        return mOnlinePresenter;
    }

    public RecyclerView getSearchResultRecyclerView() {
        return mSearchResultRecyclerView;
    }

    public SearchedRoomsAdapter getSearchedRoomsAdapter() {
        return mSearchedRoomsAdapter;
    }
}
