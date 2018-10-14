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
        InnerInstructionsData list1Data1 = new InnerInstructionsData("What is this game?", "This is a party game for everyone!\n\nIn this party game, players will draw and guess respectively, from their previous players.\n\nAfter every one finishes, check out who's drawing and guessing is the most ridiculous and hilarious!!\n\nNo need to spend a lot of time learning, anyone can start this game and enjoy in 30 seconds.\n\nSo what is the goal of the game? Well, it is not to win, but to have fun!!!", "");
        InnerInstructionsData list1Data2 = new InnerInstructionsData("Game Requirements?", "Minimum Player: 4\n\nMaximum Players: 12\n\nTools: Your Phone", "");

        ArrayList<InnerInstructionsData> instructionsDataList1 = new ArrayList<>();
        instructionsDataList1.add(list1Data0);
        instructionsDataList1.add(list1Data1);
        instructionsDataList1.add(list1Data2);


        // 2nd datalist
        InnerInstructionsData list2Data0 = new InnerInstructionsData("How to play?", null, String.valueOf(R.drawable.bg_instructions2));
        InnerInstructionsData list2Data1 = new InnerInstructionsData("Step 1", "You can start the games in 2 ways. In the main menu, either create a game for others to join, or search and join an existing game your friend created.", "");
        InnerInstructionsData list2Data2 = new InnerInstructionsData("Step 2", "After every player has joined the game, the creator can start the game", "");
        InnerInstructionsData list2Data3 = new InnerInstructionsData("Step 3", "In the 1st round, every one should think about a topic and type it into the input box and wait until the time at the top finishes (time amount is one of the room settings of room master)", "");
        InnerInstructionsData list2Data4 = new InnerInstructionsData("Step 4", "Then, in the 2nd round, the topic will be delivered to the next player automatically, where players will see the topic from some one, and will need to turn the words into a hand-drawing for the next player to guess what it is" +
                "\n\nNote:" +
                "\n\nodd-numbered players and even-numbered players will have slightly different rules" +
                "\n\nOdd-Numbred Players: All players will set a topic and the topic will be delivered to the next player automatically in this 2nd round" +
                "\n\nEven-Numbered pLayers: All players will set a topic, but the players will have to turn the topic they set in the 1st round into a drawing in this 2nd roundd" +
                "\n\nThe rest of the game is the same for both odd-numbered players and even-numbered players", "");
        InnerInstructionsData list2Data5 = new InnerInstructionsData("Step 5", "In the 3rd round, all players will receive a drawing from some one in the game, and your goal is to guess what the drawing is about" +
                "\n\nHint: word counts is shown in the input section", "");
        InnerInstructionsData list2Data6 = new InnerInstructionsData("Step 6", "The game will continue with steps 4 and 5 one after the other until each topic goes through all players in the game" +
                "\n\nAfter every one finishes, each player will have all the drawings and guessings related to the topic they set in the 1st round." +
                "\n\nPlease swipe left to see how to use the results to enjoy the best part of this game!!", "");

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
        InnerInstructionsData list3Data1 = new InnerInstructionsData("Step 1", "After the game finishes, every player will have the results (drawings and guessings) related to the topic they set in the 1st round" +
                "\n\nGo to the first drawing, let every one guess what the drawing is without knowing the real answer(topic)", "");
        InnerInstructionsData list3Data2 = new InnerInstructionsData("Step 2", "Continue along with the results one by one to see who is the rotten apple that spoils the barrel!!!", "");
        InnerInstructionsData list3Data3 = new InnerInstructionsData("Step 3", "Finally, show the final answer to every player", "");
        InnerInstructionsData list3Data4 = new InnerInstructionsData("Step 4", "Well the above is the recommended way to present the results" +
                "\n\nBut you can present the results however you like!" +
                "\n\nThe point is...... Have Fun!", "");

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
