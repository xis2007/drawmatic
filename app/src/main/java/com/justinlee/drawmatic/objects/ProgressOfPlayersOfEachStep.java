package com.justinlee.drawmatic.objects;

import java.util.ArrayList;
import java.util.HashMap;

public class ProgressOfPlayersOfEachStep {
    HashMap<String, Integer> mProgressOfEachPlayer;

    public ProgressOfPlayersOfEachStep(ArrayList<Player> playersList) {
        this.mProgressOfEachPlayer = new HashMap<>();

        for(Player player : playersList) {
            mProgressOfEachPlayer.put(player.getPlayerId(), 0);
        }
    }

    public HashMap<String, Integer> getProgressOfEachPlayer() {
        return mProgressOfEachPlayer;
    }
}
