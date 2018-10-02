package com.justinlee.drawmatic.util;

import android.content.Context;
import android.util.Log;

import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.objects.Player;

public class TopicDrawingRetrievingUtil {
    private Context mContext;
    private OnlineGame mOnlineGame;
    private Player mCuurentPlayer;

    public TopicDrawingRetrievingUtil(Context context, OnlineGame onlineGame, Player cuurentPlayer) {
        mContext = context;
        mOnlineGame = onlineGame;
        mCuurentPlayer = cuurentPlayer;
    }

    public String calcPlayerIdToRetrieveTopicOrDrawing() {
        if(mOnlineGame.isPlayersOddumbered()) {
            int positionOfPlayerInsortedList = getOddNumberedPlayerPosition();
            String playerId = mOnlineGame.getOnlineSettings().generateSortedPlayersListById().get(positionOfPlayerInsortedList).getPlayerId();
            Log.d("sortedList", "calcPlayerIdToRetrieveTopicOrDrawing: " + mOnlineGame.getOnlineSettings().generateSortedPlayersListById().get(0).getPlayerName());
            return playerId;

        } else {
            int positionOfPlayerInsortedList = getEvenNumberedPlayerPosition();
            String playerId = mOnlineGame.getOnlineSettings().generateSortedPlayersListById().get(positionOfPlayerInsortedList).getPlayerId();
            Log.d("sortedList", "calcPlayerIdToRetrieveTopicOrDrawing: " + mOnlineGame.getOnlineSettings().generateSortedPlayersListById().get(0).getPlayerName());
            return playerId;
        }
    }

    private int getOddNumberedPlayerPosition() {
        int totalSteps = mOnlineGame.getTotalSteps();
        int currentStep = mOnlineGame.getCurrentStep();
        int currentPlayerPositionInList = mOnlineGame.getOnlineSettings().generateSortedPlayersListById().indexOf(mCuurentPlayer);

        int positionOfPlayerToGetData = (currentPlayerPositionInList - (currentStep - 1)) >= 0 ? (currentPlayerPositionInList - (currentStep - 1)) : totalSteps - ((currentStep - 1) - currentPlayerPositionInList);

        return positionOfPlayerToGetData;
    }


    private int getEvenNumberedPlayerPosition() {
        int totalSteps = mOnlineGame.getTotalSteps();
        int currentStep = mOnlineGame.getCurrentStep();
        int currentPlayerPositionInList = mOnlineGame.getOnlineSettings().generateSortedPlayersListById().indexOf(mCuurentPlayer);

        int positionOfPlayerToGetData;
        if(currentStep <= 2) {
            positionOfPlayerToGetData = currentPlayerPositionInList;
        } else {
            positionOfPlayerToGetData = (currentPlayerPositionInList - (currentStep - 2)) >= 0 ? (currentPlayerPositionInList - (currentStep - 2)) : (totalSteps - 1) - ((currentStep - 2) - currentPlayerPositionInList);
        }

        return positionOfPlayerToGetData;
    }


    public int calcItemNumberToRetrieveTopicOrDrawing() {
        if(mOnlineGame.isPlayersOddumbered()) {
            return getOddNumberedPlayerTopicOrDrawing();
        } else {
            return getEvenNumberedPlayerTopicOrDrawing();
        }
    }

    private int getOddNumberedPlayerTopicOrDrawing() {
        return mOnlineGame.getCurrentStep() - 1;
    }


    private int getEvenNumberedPlayerTopicOrDrawing() {
        return mOnlineGame.getCurrentStep() - 1;
    }


    public boolean shouldGetTopic() {
        return calcItemNumberToRetrieveTopicOrDrawing() % 2 == 1;
    }
}
