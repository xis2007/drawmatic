package com.justinlee.drawmatic.util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.R;

public class BackLeaveAppBottomSheetDialog extends BottomSheetDialogFragment {
    private static MainActivity mMainActivity;

    public BackLeaveAppBottomSheetDialog() {
    }

    public static BackLeaveAppBottomSheetDialog newInstance(MainActivity mainActivity) {
        mMainActivity = mainActivity;
        return new BackLeaveAppBottomSheetDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bottom_sheet_dialog_leave_app_back, container, false);

        initButtons(rootView);

        return rootView;
    }

    private void initButtons(View rootView) {
        Button buttonStayInGame = rootView.findViewById(R.id.button_stay_in_app_bottom_dialog);
        Button buttonLeaveGame = rootView.findViewById(R.id.button_leave_app_bottom_dialog);

        buttonStayInGame.setOnClickListener(leaveAppAlertOnClickListener);
        buttonLeaveGame.setOnClickListener(leaveAppAlertOnClickListener);

    }

    private View.OnClickListener leaveAppAlertOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_stay_in_app_bottom_dialog:
                    dismiss();
                    break;

                case R.id.button_leave_app_bottom_dialog:
                    // TODO inform the server and other players that the game ends
                    mMainActivity.finish();
                    dismiss();
                    break;

                default:
                    break;

            }
        }
    };
}
