package com.justinlee.drawmatic.play;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.adapters.SearchedRoomsAdapter;
import com.justinlee.drawmatic.constants.Constants;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayFragment extends Fragment implements PlayContract.View, View.OnClickListener {
    private PlayContract.Presenter mOnlinePresenter;

    private FrameLayout mToolBarHint;
    private CardView mButtonCreateNewGame;
    private CardView mButtonSearchGames;

    private FrameLayout mToolBarSearch;
    private ConstraintLayout mSearchedResultContainer;
    private RecyclerView mSearchResultRecyclerView;
    private SearchedRoomsAdapter mSearchedRoomsAdapter;
    private ImageView mButtonCancelSearchForRooms;
    private EditText mEdittextSearchForRooms;



    public PlayFragment() {
        // Required empty public constructor
    }


    public static PlayFragment newInstance() {
        return new PlayFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_play, container, false);

        initViews(rootView);
        initRecyclerView(rootView);

        return rootView;
    }

    private void initViews(View rootView) {

        // views for game selection
        mToolBarHint = rootView.findViewById(R.id.frame_hint_play);
        mToolBarHint.setVisibility(View.VISIBLE);

        mButtonCreateNewGame = rootView.findViewById(R.id.button_new_game_play);
        mButtonSearchGames = rootView.findViewById(R.id.button_search_game_play);
        mButtonCreateNewGame.setOnClickListener(this);
        mButtonSearchGames.setOnClickListener(this);

        // view for game searching
        mToolBarSearch = rootView.findViewById(R.id.frame_search_bar_play);
        mToolBarSearch.setVisibility(View.INVISIBLE);

        mEdittextSearchForRooms = rootView.findViewById(R.id.edittext_searchbox_play);
        mButtonCancelSearchForRooms = rootView.findViewById(R.id.button_cancel_search_play);
        mEdittextSearchForRooms.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (Constants.NO_STRING.equals(v.getText().toString()) || v.getText().toString().isEmpty()) {
                    exitSearch();
                    Snackbar.make(getActivity().findViewById(R.id.fragment_container_main), R.string.hint_room_name_input, Snackbar.LENGTH_SHORT).show();
                } else {
                    exitSearch();
                    mOnlinePresenter.searchForRooms(PlayFragment.this, v.getText().toString());
                }
                return true;
            }
        });

        mEdittextSearchForRooms.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    if (hasFocus) {
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    } else {
                        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                    }
                }
            }
        });
    }

    private void initRecyclerView(View rootView) {
        mSearchResultRecyclerView = rootView.findViewById(R.id.room_list_recyclerview_online);
        mSearchedResultContainer = rootView.findViewById(R.id.layout_room_list_online);

        mSearchResultRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSearchedRoomsAdapter = new SearchedRoomsAdapter(PlayFragment.this, null);
        mSearchResultRecyclerView.setAdapter(mSearchedRoomsAdapter);
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
    public void showGameSelectionPageUi() {
        mToolBarSearch.setVisibility(View.INVISIBLE);
        mEdittextSearchForRooms.clearFocus();

        mToolBarHint.setVisibility(View.VISIBLE);
        mSearchedResultContainer.setVisibility(View.GONE);
    }


    @Override
    public void showSearchGamesPageUi() {
        mToolBarSearch.setVisibility(View.VISIBLE);
        mEdittextSearchForRooms.requestFocus();

        mToolBarHint.setVisibility(View.INVISIBLE);
        mSearchedResultContainer.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideOnlineSearchPageUi() {
        mSearchedResultContainer.setVisibility(View.GONE);
    }


    @Override
    public void showOnlineRoomCreationPageUi(int roomType) {

    }


    @Override
    public void showOnlineGameSettingsPageUi() {

    }


    @Override
    public void showRoomIsInGameMessage() {
        Snackbar.make(getActivity().findViewById(R.id.fragment_container_main), R.string.hint_selected_room_is_in_game, Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public void setPresenter(PlayContract.Presenter presenter) {
        mOnlinePresenter = checkNotNull(presenter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_new_game_play:
                mOnlinePresenter.createRoomForOnlineNormalMode();
                break;

            case R.id.button_search_game_play:
                mOnlinePresenter.informToTransToSearchRoomsPage();
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
    public PlayContract.Presenter getOnlinePresenter() {
        return mOnlinePresenter;
    }

    public RecyclerView getSearchResultRecyclerView() {
        return mSearchResultRecyclerView;
    }

    public SearchedRoomsAdapter getSearchedRoomsAdapter() {
        return mSearchedRoomsAdapter;
    }

    public ConstraintLayout getSearchedResultContainer() {
        return mSearchedResultContainer;
    }
}
