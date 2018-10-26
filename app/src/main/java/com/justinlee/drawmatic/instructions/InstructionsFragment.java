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


    private ArrayList<ArrayList<InnerInstructionsData>> createInstructionsLists() {
        // 1st datalist
        InnerInstructionsData list1Data0 = new InnerInstructionsData(getString(R.string.title_header_1of1), null, String.valueOf(R.drawable.bg_instructions1));
        InnerInstructionsData list1Data1 = new InnerInstructionsData(getString(R.string.title_item_1of1), getString(R.string.des_item_1of1), "");
        InnerInstructionsData list1Data2 = new InnerInstructionsData(getString(R.string.title_item_2of1), getString(R.string.des_item_2of1), "");

        ArrayList<InnerInstructionsData> instructionsDataList1 = new ArrayList<>();
        instructionsDataList1.add(list1Data0);
        instructionsDataList1.add(list1Data1);
        instructionsDataList1.add(list1Data2);


        // 2nd datalist
        InnerInstructionsData list2Data0 = new InnerInstructionsData(getString(R.string.title_header_1of2), null, String.valueOf(R.drawable.bg_instructions2));
        InnerInstructionsData list2Data1 = new InnerInstructionsData(getString(R.string.title_item_1of2), getString(R.string.des_item_1of2), "");
        InnerInstructionsData list2Data2 = new InnerInstructionsData(getString(R.string.title_item_2of2), getString(R.string.des_item_2of2), "");
        InnerInstructionsData list2Data3 = new InnerInstructionsData(getString(R.string.title_item_3of2), getString(R.string.des_item_3of2), "");
        InnerInstructionsData list2Data4 = new InnerInstructionsData(getString(R.string.title_item_4of2), getString(R.string.des_item_4of2), "");
        InnerInstructionsData list2Data5 = new InnerInstructionsData(getString(R.string.title_item_5of2), getString(R.string.des_item_5of2), "");
        InnerInstructionsData list2Data6 = new InnerInstructionsData(getString(R.string.title_item_6of2), getString(R.string.des_item_6of2), "");

        ArrayList<InnerInstructionsData> instructionsDataList2 = new ArrayList<>();
        instructionsDataList2.add(list2Data0);
        instructionsDataList2.add(list2Data1);
        instructionsDataList2.add(list2Data2);
        instructionsDataList2.add(list2Data3);
        instructionsDataList2.add(list2Data4);
        instructionsDataList2.add(list2Data5);
        instructionsDataList2.add(list2Data6);

        // 3rd datalist
        InnerInstructionsData list3Data0 = new InnerInstructionsData(getString(R.string.title_header_1of3), null, String.valueOf(R.drawable.bg_instructions3));
        InnerInstructionsData list3Data1 = new InnerInstructionsData(getString(R.string.title_item_1of3), getString(R.string.des_item_1of3), "");
        InnerInstructionsData list3Data2 = new InnerInstructionsData(getString(R.string.title_item_2of3), getString(R.string.des_item_2of3), "");
        InnerInstructionsData list3Data3 = new InnerInstructionsData(getString(R.string.title_item_3of3), getString(R.string.des_item_3of3), "");
        InnerInstructionsData list3Data4 = new InnerInstructionsData(getString(R.string.title_item_4of3), getString(R.string.des_item_4of3), "");

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
