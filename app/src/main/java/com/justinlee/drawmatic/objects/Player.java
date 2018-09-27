package com.justinlee.drawmatic.objects;

public class Player {
    private String mPlayerName;
    private String mPlayerId;
    private int mPlayerType;
    private int mPlayerOrder;

    public Player() {
    }

    public Player(String playerName, String playerId, int playerType,  int playerOrder) {
        mPlayerName = playerName;
        mPlayerId = playerId;
        mPlayerType = playerType;
        mPlayerOrder = playerOrder;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    public String getPlayerId() {
        return mPlayerId;
    }

    public int getPlayerType() {
        return mPlayerType;
    }

    public int getPlayerOrder() {
        return mPlayerOrder;
    }
}
