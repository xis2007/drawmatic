package com.justinlee.drawmatic.instructions;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.adapters.InstructionsOuterAdapter;
import com.justinlee.drawmatic.objects.InnerInstructionsData;
import com.ramotion.garlandview.TailLayoutManager;
import com.ramotion.garlandview.TailRecyclerView;
import com.ramotion.garlandview.TailSnapHelper;
import com.ramotion.garlandview.header.HeaderTransformer;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstructionsFragment extends Fragment implements InstructionsContract.View {
    private InstructionsContract.Presenter mInstructionsPresenter;

    TailRecyclerView mTailRecyclerView;

    public InstructionsFragment() {
        // Required empty public constructor
    }

    public static InstructionsFragment newInstance() {
        return new InstructionsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_instructions, container, false);

        initViews(rootView);

        return rootView;
    }

    private void initViews(View rootView) {
        mTailRecyclerView = rootView.findViewById(R.id.tail_recycler_view_instructions);
        ((TailLayoutManager) mTailRecyclerView.getLayoutManager()).setPageTransformer(new HeaderTransformer());
        mTailRecyclerView.setAdapter(new InstructionsOuterAdapter(createInstructionsLists()));

        new TailSnapHelper().attachToRecyclerView(mTailRecyclerView);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInstructionsPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull InstructionsContract.Presenter presenter) {
        mInstructionsPresenter = checkNotNull(presenter);
    }


    /**
     * hardcoded data at initial use
     */
    private ArrayList<ArrayList<InnerInstructionsData>> createInstructionsLists() {
        // 1st datalist
        InnerInstructionsData list1Data0 = new InnerInstructionsData("Introduction", null, String.valueOf(R.drawable.bg_instructions1));
        InnerInstructionsData list1Data1 = new InnerInstructionsData("What is this game?", "This game is a drawing and guessing game. Players will set topic first, the next will draw the topic, and the next will guess the drawing from the previous person.", "https://firebasestorage.googleapis.com/v0/b/drawmatic-jl888.appspot.com/o/drawmatic%2Finstructions%2Fbg_game_selection_2.png?alt=media&token=7881d44c-f542-4a65-9424-60ebb3c44650");
        InnerInstructionsData list1Data2 = new InnerInstructionsData("Game Requirements?", "Minimum Player: 4\nMaximum Players: 12\nTools: Your Phone~", "");

        ArrayList<InnerInstructionsData> instructionsDataList1 = new ArrayList<>();
        instructionsDataList1.add(list1Data0);
        instructionsDataList1.add(list1Data1);
        instructionsDataList1.add(list1Data2);


        // 2nd datalist
        InnerInstructionsData list2Data0 = new InnerInstructionsData("How to play?", null, String.valueOf(R.drawable.bg_instructions2));
        InnerInstructionsData list2Data1 = new InnerInstructionsData("Step 1", "Start the games in 2 ways....", "");
        InnerInstructionsData list2Data2 = new InnerInstructionsData("Step 2", "Join or wait until all players to join the room", "");
        InnerInstructionsData list2Data3 = new InnerInstructionsData("Step 3", "Room master starts the game...", "");
        InnerInstructionsData list2Data4 = new InnerInstructionsData("Step 4", "Set the topic...", "");
        InnerInstructionsData list2Data5 = new InnerInstructionsData("Step 5", "After time ends, you will receive a topic from one of the players, and you will need to translate the topic into a drawing for the next player to guess what your drawing is", "");
        InnerInstructionsData list2Data6 = new InnerInstructionsData("Step 6", "After time ends, you will receive a drawing from one of the players, and you will need to guess what the drawing is", "");

        ArrayList<InnerInstructionsData> instructionsDataList2 = new ArrayList<>();
        instructionsDataList2.add(list2Data0);
        instructionsDataList2.add(list2Data1);
        instructionsDataList2.add(list2Data2);
        instructionsDataList2.add(list2Data3);
        instructionsDataList2.add(list2Data4);
        instructionsDataList2.add(list2Data5);
        instructionsDataList2.add(list2Data6);

        // 3rd datalist
        InnerInstructionsData list3Data0 = new InnerInstructionsData("Show the Results", null, String.valueOf(R.drawable.bg_instructions3));
        InnerInstructionsData list3Data1 = new InnerInstructionsData("Step 1", "Recommended to show the results from the first drawing, let every one guess what it is without knowing the answer", "");
        InnerInstructionsData list3Data2 = new InnerInstructionsData("Step 2", "show the guessing and drawings in order after the first drawing, so every one knows who made hilarious guessing and drawing", "");
        InnerInstructionsData list3Data3 = new InnerInstructionsData("Step 3", "Show the final answer in the end", "");
        InnerInstructionsData list3Data4 = new InnerInstructionsData("Step 4", "And...... Hav Fun!", "");

        ArrayList<InnerInstructionsData> instructionsDataList3 = new ArrayList<>();
        instructionsDataList3.add(list3Data0);
        instructionsDataList3.add(list3Data1);
        instructionsDataList3.add(list3Data2);
        instructionsDataList3.add(list3Data3);
        instructionsDataList3.add(list3Data4);

        // add the lists into the arraylist
        ArrayList<ArrayList<InnerInstructionsData>> instructionsList = new ArrayList<>();
        instructionsList.add(instructionsDataList1);
        instructionsList.add(instructionsDataList2);
        instructionsList.add(instructionsDataList3);

        return instructionsList;
    }


}
