package com.justinlee.drawmatic.objects;

public class Player {
    private int mPlayerType;
    private String mPlayerName;
    private int mPlayerOrder;

    public Player(int playerType, String playerName, int playerOrder) {
        mPlayerType = playerType;
        mPlayerName = playerName;
        mPlayerOrder = playerOrder;
    }

    public int getPlayerType() {
        return mPlayerType;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    public int getPlayerOrder() {
        return mPlayerOrder;
    }
}
