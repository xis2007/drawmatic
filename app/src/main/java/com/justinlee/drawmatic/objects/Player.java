package com.justinlee.drawmatic.objects;

public class Player {
    private String mPlayerName;
    private String mPlayerId;
    private int mPlayerType;

    public Player() {
    }

    public Player(String playerName, String playerId, int playerType) {
        mPlayerName = playerName;
        mPlayerId = playerId;
        mPlayerType = playerType;
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

    public void setPlayerName(String playerName) {
        mPlayerName = playerName;
    }

    public void setPlayerId(String playerId) {
        mPlayerId = playerId;
    }

    public void setPlayerType(int playerType) {
        mPlayerType = playerType;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Player)) return false;

        Player anotherPlayer = (Player) obj;
        return this.getPlayerId().equals(anotherPlayer.getPlayerId());
    }
}
