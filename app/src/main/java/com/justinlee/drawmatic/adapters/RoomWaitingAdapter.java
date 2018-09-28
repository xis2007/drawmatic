package com.justinlee.drawmatic.adapters;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.constants.Constants;
import com.justinlee.drawmatic.objects.Player;
import com.justinlee.drawmatic.online_room_waiting.OnlineWaitingPresenter;

import java.util.ArrayList;

public class RoomWaitingAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private OnlineWaitingPresenter mOnlineWaitingPresenter;
    private ArrayList<Player> mPlayers;

    public RoomWaitingAdapter(OnlineWaitingPresenter onlineWaitingPresenter, ArrayList<Player> players) {
        mContext = ((Fragment) onlineWaitingPresenter.getOnlineWaitingView()).getActivity();
        mOnlineWaitingPresenter = onlineWaitingPresenter;
        mPlayers = players;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Player currentPlayer = mPlayers.get(position);

        if (currentPlayer.getPlayerType() == Constants.PlayerType.ROOM_MASTER) {
//            ((PlayerViewHolder) holder).getProfileBackground().setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            ((PlayerViewHolder) holder).getPlayerType().setText(R.string.text_room_master);
        } else {
//            ((PlayerViewHolder) holder).getProfileBackground().setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
            ((PlayerViewHolder) holder).getPlayerType().setText(R.string.type_participant);
        }

        ((PlayerViewHolder) holder).getPlayerName().setText(currentPlayer.getPlayerName());
    }

    public void swapList(ArrayList<Player> players) {
        if (mPlayers != players) {
            mPlayers = players;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mPlayers.size();
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        private ImageView mProfileBackground;
        private TextView mPlayerName;
        private TextView mPlayerType;
        private int mPlayerOrder;


        public PlayerViewHolder(View itemView) {
            super(itemView);

            mProfileBackground = itemView.findViewById(R.id.image_player_profile_background);
            mPlayerName = itemView.findViewById(R.id.text_player_name_waiting);
            mPlayerType = itemView.findViewById(R.id.text_player_type);
        }

        public ImageView getProfileBackground() {
            return mProfileBackground;
        }

        public TextView getPlayerName() {
            return mPlayerName;
        }

        public TextView getPlayerType() {
            return mPlayerType;
        }
    }
}
