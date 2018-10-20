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
import com.justinlee.drawmatic.firabase_operation.OnlineInGameManager;
import com.justinlee.drawmatic.objects.OnlineGame;

public class LeaveGameBottomSheetDialog extends BottomSheetDialogFragment {
    private static MainActivity mMainActivity;
    private OnlineGame mOnlineGame;

    public LeaveGameBottomSheetDialog() {
    }

    public static LeaveGameBottomSheetDialog newInstance(MainActivity mainActivity) {
        mMainActivity = mainActivity;
        return new LeaveGameBottomSheetDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bottom_sheet_dialog_leave_game, container, false);

        initButtons(rootView);

        return rootView;
    }

    private void initButtons(View rootView) {
        Button buttonStayInGame = rootView.findViewById(R.id.button_stay_in_game_bottom_dialog);
        Button buttonLeaveGame = rootView.findViewById(R.id.button_leave_game_bottom_dialog);

        buttonStayInGame.setOnClickListener(leaveGameAlertOnClickListener);
        buttonLeaveGame.setOnClickListener(leaveGameAlertOnClickListener);
    }

    private View.OnClickListener leaveGameAlertOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_stay_in_game_bottom_dialog:
                    dismiss();
                    break;

                case R.id.button_leave_game_bottom_dialog:
                    // TODO inform the server and other players that the game ends
                    if(mOnlineGame != null) {
                        new OnlineInGameManager(mMainActivity).leaveRoomAndDeleteDataWhileInGame(mOnlineGame);
                    } else {
                        mMainActivity.getMainPresenter().transToPlayPage();
                    }

                    dismiss();
                    break;

                default:
                    break;

            }
        }
    };

    /**
     * *********************************************************************************
     * Getters and Setters
     * **********************************************************************************
     */
    public LeaveGameBottomSheetDialog setOnlineGame(OnlineGame onlineGame) {
        mOnlineGame = onlineGame;
        return this;
    }
}
