package com.justinlee.drawmatic.objects;

public class Player {
    private String mPlayerName;
    private String mPlayerId;
    private int mPlayerType;
//    private int mPlayerOrder;

    public Player() {
    }

    public Player(String playerName, String playerId, int playerType,  int playerOrder) {
        mPlayerName = playerName;
        mPlayerId = playerId;
        mPlayerType = playerType;
//        mPlayerOrder = playerOrder;
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

//    public int getPlayerOrder() {
//        return mPlayerOrder;
//    }

    public void setPlayerName(String playerName) {
        mPlayerName = playerName;
    }

    public void setPlayerId(String playerId) {
        mPlayerId = playerId;
    }

    public void setPlayerType(int playerType) {
        mPlayerType = playerType;
    }

//    public void setPlayerOrder(int playerOrder) {
//        mPlayerOrder = playerOrder;
//    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Player)) return false;

        Player anotherPlayer = (Player) obj;
        return this.getPlayerId().equals(anotherPlayer.getPlayerId());

    }
}
