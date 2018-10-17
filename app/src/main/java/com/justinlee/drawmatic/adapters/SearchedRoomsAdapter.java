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
import com.justinlee.drawmatic.constants.Constants;
import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.play.PlayFragment;

import java.util.ArrayList;

public class SearchedRoomsAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private PlayFragment mPlayFragment;
    private ArrayList<OnlineGame> mOnlineGamesList;

    public SearchedRoomsAdapter(PlayFragment playFragment, ArrayList<OnlineGame> onlineGamesList) {
        mContext = playFragment.getActivity();
        mPlayFragment = playFragment;
        mOnlineGamesList = onlineGamesList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case Constants.RoomViewType.INITIAL_SEARCH:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_no_result, parent, false);
                view.setVisibility(View.INVISIBLE);
                return new NoRoomViewHolder(view);

            case Constants.RoomViewType.NO_RESULTS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_no_result, parent, false);
                view.setVisibility(View.VISIBLE);
                return new NoRoomViewHolder(view);

            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
                view.setVisibility(View.VISIBLE);
                return new RoomViewHolder(view);
        }

//        if(viewType == Constants.RoomViewType.NO_RESULTS) {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_no_result, parent, false);
//            return new NoRoomViewHolder(view);
//        } else {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
//            return new RoomViewHolder(view);
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RoomViewHolder) {
            final OnlineGame currentOnlineGame = mOnlineGamesList.get(position);

            // TODO ordering may cause problem to positions of each player in the Arraylist
            ((RoomViewHolder) holder).getTextRoomName().setText(currentOnlineGame.getOnlineSettings().getRoomName());
            ((RoomViewHolder) holder).getTextRoomCreater().setText(currentOnlineGame.getOnlineSettings().getPlayers().get(0).getPlayerName());
            if(currentOnlineGame.getOnlineSettings().isInGame()) {
                ((RoomViewHolder) holder).getTextNumPlayersInRoom().setText("Playing");
                ((RoomViewHolder) holder).getTextNumPlayersInRoom().setTextColor(mContext.getResources().getColor(R.color.colorAlertRed));
            } else {
                ((RoomViewHolder) holder).getTextNumPlayersInRoom().setText(currentOnlineGame.getOnlineSettings().getPlayers().size() + " / " + currentOnlineGame.getOnlineSettings().getMaxPlayers());
                ((RoomViewHolder) holder).getTextNumPlayersInRoom().setTextColor(mContext.getResources().getColor(R.color.colorWhite));
            }

            ((RoomViewHolder) holder).getRoomItemLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentOnlineGame.getOnlineSettings().isInGame()) {
                        mPlayFragment.getOnlinePresenter().informToShowRoomIsInGameMessage();
                    } else {
                        mPlayFragment.getOnlinePresenter().joinSelectedRoom(currentOnlineGame);
                        ((MainContract.View) mPlayFragment.getActivity()).showLoadingUi(mContext.getString(R.string.hint_loading_joining_room));
                    }

                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mOnlineGamesList == null) return Constants.RoomViewType.INITIAL_SEARCH;
        if(mOnlineGamesList.isEmpty()) return  Constants.RoomViewType.NO_RESULTS;
        return Constants.RoomViewType.ROOM_RESULTS;
//        return mOnlineGamesList == null ? Constants.RoomViewType.NO_RESULTS : Constants.RoomViewType.ROOM_RESULTS;
    }

    @Override
    public int getItemCount() {
        return mOnlineGamesList == null ? 1 : mOnlineGamesList.size();
    }

    public void swapList(ArrayList<OnlineGame> onlineGamesList) {
        if (mOnlineGamesList != onlineGamesList) {
            mOnlineGamesList = onlineGamesList;
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

    public class NoRoomViewHolder extends RecyclerView.ViewHolder {
        public NoRoomViewHolder(View itemView) {
            super(itemView);
        }
    }
}
