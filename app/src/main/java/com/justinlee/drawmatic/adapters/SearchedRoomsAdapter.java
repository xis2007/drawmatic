package com.justinlee.drawmatic.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justinlee.drawmatic.MainContract;
import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.objects.OnlineSettings;
import com.justinlee.drawmatic.online.OnlineFragment;

import java.util.ArrayList;

public class SearchedRoomsAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private OnlineFragment mOnlineFragment;
    private ArrayList<OnlineSettings> mOnlineGameSettings;

    public SearchedRoomsAdapter(OnlineFragment onlineFragment, ArrayList<OnlineSettings> onlineGameSettings) {
        mContext = onlineFragment.getActivity();
        mOnlineFragment = onlineFragment;
        mOnlineGameSettings = onlineGameSettings;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final OnlineSettings currentOnlineGameSettings = mOnlineGameSettings.get(position);

        // TODO ordering may cause problem to positions of each player in the Arraylist
        ((RoomViewHolder) holder).getTextRoomName().setText(currentOnlineGameSettings.getRoomName());
        ((RoomViewHolder) holder).getTextRoomCreater().setText(currentOnlineGameSettings.getPlayers().get(0).getPlayerName());
        ((RoomViewHolder) holder).getTextNumPlayersInRoom().setText(currentOnlineGameSettings.getCurrentNumPlayers() + " / " + currentOnlineGameSettings.getMaxPlayers());

        ((RoomViewHolder) holder).getRoomItemLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnlineFragment.getOnlinePresenter().joinSelectedRoom(currentOnlineGameSettings);
                ((MainContract.View) mOnlineFragment.getActivity()).showLoadingUi();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOnlineGameSettings.size();
    }

    public void swapList(ArrayList<OnlineSettings> gameSettings) {
        if (mOnlineGameSettings != gameSettings) {
            mOnlineGameSettings = gameSettings;
            notifyDataSetChanged();
        }
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextRoomName;
        private TextView mTextRoomCreater;
        private TextView mTextNumPlayersInRoom;

        private ConstraintLayout roomItemLayout;


        public RoomViewHolder(View itemView) {
            super(itemView);

            mTextRoomName = itemView.findViewById(R.id.text_room_name_searched);
            mTextRoomCreater = itemView.findViewById(R.id.text_creator);
            mTextNumPlayersInRoom = itemView.findViewById(R.id.text_max_player);

            roomItemLayout = itemView.findViewById(R.id.layout_item_room);
        }

        public TextView getTextRoomName() {
            return mTextRoomName;
        }

        public TextView getTextRoomCreater() {
            return mTextRoomCreater;
        }

        public TextView getTextNumPlayersInRoom() {
            return mTextNumPlayersInRoom;
        }

        public ConstraintLayout getRoomItemLayout() {
            return roomItemLayout;
        }
    }
}
