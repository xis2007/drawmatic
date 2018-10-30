package com.justinlee.drawmatic.settings;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.justinlee.drawmatic.MainPresenter;
import com.justinlee.drawmatic.R;

import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;

public class SettingsFragment extends Fragment implements SettingsContract.View {
    private SettingsContract.Presenter mSettingsPresenter;

    private EditText mEdittextUserName;
    private ExpandableLayout mExpandableLayout;
    private ImageButton mButtonEditName;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        initViews(rootView);
        initFoldingViews(rootView);

        return rootView;
    }


    private void initViews(View rootView) {
        mEdittextUserName = rootView.findViewById(R.id.editText_edit_name);
        mEdittextUserName.setText(((MainPresenter) ((SettingsPresenter) mSettingsPresenter).getMainPresenter()).getCurrentPlayer().getPlayerName());


        mButtonEditName = rootView.findViewById(R.id.button_edit_name_settings);
        mButtonEditName.setTag(R.drawable.ic_edit);
        mButtonEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Integer) v.getTag()).equals(((Integer) R.drawable.ic_edit))) {
                    mButtonEditName.setImageDrawable(getResources().getDrawable(R.drawable.ic_confirm));
                    mButtonEditName.setTag(R.drawable.ic_confirm);
                    mEdittextUserName.setBackground(getResources().getDrawable(R.drawable.box_edit_name_settings));
                    mEdittextUserName.setEnabled(true);
                    mEdittextUserName.requestFocus();

                } else {
                    mButtonEditName.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
                    mButtonEditName.setTag(R.drawable.ic_edit);
                    mEdittextUserName.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    mEdittextUserName.setEnabled(false);

                    mSettingsPresenter.updatePlayerName(mEdittextUserName.getText().toString());
                }
            }
        });
    }

    private void initFoldingViews(View rootView) {
        mExpandableLayout = rootView.findViewById(R.id.expandable_layout);
        mExpandableLayout.setRenderer(new ExpandableLayout.Renderer<String, String>() {
            @Override
            public void renderParent(View view, String s, boolean b, int parentPosition) {
                switch (parentPosition) {
                    case 0:
                        ((TextView) view.findViewById(R.id.text_title_parent_about_settings)).setText(R.string.title_about);
                        break;
                    case 1:
                        ((TextView) view.findViewById(R.id.text_title_parent_about_settings)).setText(R.string.title_privacy_policy);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void renderChild(View view, String s, int parentPosition, int childPosition) {
                switch (parentPosition) {
                    case 0:
                        ((TextView) view.findViewById(R.id.text_title_child_about_settings)).setText(R.string.des_this_app);
                        break;
                    case 1:
                        ((TextView) view.findViewById(R.id.text_title_child_about_settings)).setText(R.string.des_privacy_policy);
                        ((TextView) view.findViewById(R.id.text_title_child_about_settings)).setMovementMethod(LinkMovementMethod.getInstance());
                        break;
                    default:
                        break;
                }
            }
        });

        Section<String, String> section1 = new Section<>();
        section1.parent = "Parent1";
        section1.children.add("child1");

        Section<String, String> section2 = new Section<>();
        section2.parent = "Parent2";
        section2.children.add("child1");

        mExpandableLayout.addSection(section1);
        mExpandableLayout.addSection(section2);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSettingsPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull SettingsContract.Presenter presenter) {
        mSettingsPresenter = presenter;
    }
}
