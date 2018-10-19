package com.justinlee.drawmatic.objects;

import java.util.ArrayList;

public class OfflineSettings extends GameSettings {
    private static final String TAG = "playerssssss";

    private int mGameMode;
    private int mNumPlayers;

    private ArrayList<Object> mGuessingAndDrawingsList;


    public OfflineSettings() {
    }

    public OfflineSettings(int gameMode, int numPlayers) {
        mGameMode = gameMode;
        mNumPlayers = numPlayers;

        mGuessingAndDrawingsList = new ArrayList<>();
    }

    public int getGameMode() {
        return mGameMode;
    }

    public void setGameModfe(int gameMode) {
        mGameMode = gameMode;
    }

    public int getNumPlayers() {
        return mNumPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        mNumPlayers = numPlayers;
    }

    public ArrayList<Object> getGuessingAndDrawingsList() {
        return mGuessingAndDrawingsList;
    }

    public void addItemToResultList(Object guessingOrDrawing) {
        mGuessingAndDrawingsList.add(guessingOrDrawing);
    }
}
