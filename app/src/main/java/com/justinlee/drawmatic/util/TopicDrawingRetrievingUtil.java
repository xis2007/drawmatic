package com.justinlee.drawmatic.util;

import android.content.Context;
import android.util.Log;

import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.objects.Player;

import java.util.ArrayList;

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
        if (mOnlineGame.isPlayersOddumbered()) {
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

    public String calcPlayerNameWhereTopicOrDrawingIsRetrieved() {
        if (mOnlineGame.isPlayersOddumbered()) {
            int positionOfPlayerInsortedList = getOddNumberedPlayerPosition();
            String playerName = mOnlineGame.getOnlineSettings().generateSortedPlayersListById().get(positionOfPlayerInsortedList).getPlayerName();
            return playerName;

        } else {
            int positionOfPlayerInsortedList = getEvenNumberedPlayerPosition();
            String playerName = mOnlineGame.getOnlineSettings().generateSortedPlayersListById().get(positionOfPlayerInsortedList).getPlayerName();
            return playerName;
        }
    }

    public ArrayList<String> calcOrderedPlayersForResults() {
        int totalPlayers = mOnlineGame.getOnlineSettings().getPlayers().size();
        int currentPlayerPositionInList = mOnlineGame.getOnlineSettings().generateSortedPlayersListById().indexOf(mCuurentPlayer);
        ArrayList<Player> playersList = mOnlineGame.getOnlineSettings().getPlayers();

        ArrayList<String> orderedPlayersForResult = new ArrayList<>();


        if (mOnlineGame.isPlayersOddumbered()) {
            for (int i = 1; i <= totalPlayers; i++) {
                if (currentPlayerPositionInList <= (playersList.size() - 1)) {
                    orderedPlayersForResult.add(playersList.get(currentPlayerPositionInList).getPlayerName());
                } else {
                    orderedPlayersForResult.add(playersList.get(currentPlayerPositionInList - totalPlayers).getPlayerName());
                }
                currentPlayerPositionInList++;
            }
        } else {
            orderedPlayersForResult.add(playersList.get(currentPlayerPositionInList).getPlayerName());
            for (int i = 1; i <= totalPlayers; i++) {
                if (currentPlayerPositionInList <= (playersList.size() - 1)) {
                    orderedPlayersForResult.add(playersList.get(currentPlayerPositionInList).getPlayerName());
                } else {
                    orderedPlayersForResult.add(playersList.get(currentPlayerPositionInList - totalPlayers).getPlayerName());
                }
                currentPlayerPositionInList++;
            }
        }

        return orderedPlayersForResult;
    }

    private int getOddNumberedPlayerPosition() {
        int totalPlayers = mOnlineGame.getOnlineSettings().getPlayers().size();
        int currentStep = mOnlineGame.getCurrentStep();
        int currentPlayerPositionInList = mOnlineGame.getOnlineSettings().generateSortedPlayersListById().indexOf(mCuurentPlayer);

        // logic 1
//        int positionOfPlayerToGetData = (currentPlayerPositionInList - (currentStep - 1)) >= 0 ? (currentPlayerPositionInList - (currentStep - 1)) : totalSteps - ((currentStep - 1) - currentPlayerPositionInList);

        // logic 2
        int positionOfPlayerToGetData = (currentPlayerPositionInList - (currentStep - 1)) >= 0 ? (currentPlayerPositionInList - (currentStep - 1)) : (currentPlayerPositionInList - (currentStep - 1)) + totalPlayers;

        return positionOfPlayerToGetData;
    }


    private int getEvenNumberedPlayerPosition() {
        int totalPlayers = mOnlineGame.getOnlineSettings().getPlayers().size();
        int currentStep = mOnlineGame.getCurrentStep();
        int currentPlayerPositionInList = mOnlineGame.getOnlineSettings().generateSortedPlayersListById().indexOf(mCuurentPlayer);

        int positionOfPlayerToGetData;
        if (currentStep <= 2) {
            positionOfPlayerToGetData = currentPlayerPositionInList;
        } else {
            // logic 1
//            positionOfPlayerToGetData = (currentPlayerPositionInList - (currentStep - 2)) >= 0 ? (currentPlayerPositionInList - (currentStep - 2)) : (totalPlayers - 1) - ((currentStep - 2) - currentPlayerPositionInList);
            // logic 2
            positionOfPlayerToGetData = (currentPlayerPositionInList - (currentStep - 2)) >= 0 ? (currentPlayerPositionInList - (currentStep - 2)) : (currentPlayerPositionInList - (currentStep - 2)) + totalPlayers;

        }

        return positionOfPlayerToGetData;
    }


    public int calcItemNumberToRetrieveTopicOrDrawing() {
        if (mOnlineGame.isPlayersOddumbered()) {
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
