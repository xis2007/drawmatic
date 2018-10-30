package com.justinlee.drawmatic.util;

import android.content.Context;

import com.justinlee.drawmatic.objects.OnlineGame;
import com.justinlee.drawmatic.objects.OnlineSettings;
import com.justinlee.drawmatic.objects.Player;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

public class TopicDrawingRetrievingUtilTest {
    @Mock
    Context mContext;

    OnlineGame mOnlineGame;
    Player mCurrentPlayer;

    @Before
    public void setup() {
        setupOnlineGame();
    }

    private void setupOnlineGame() {
        Player player1 = new Player("Apple", "a", 1100);
        Player player2 = new Player("Bagel", "b", 1000);
        Player player3 = new Player("Crayon", "c", 1000);
        Player player4 = new Player("Doodle", "d", 1000);

        mCurrentPlayer = player1;

        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        OnlineSettings onlineSettings = new OnlineSettings(100, "DandyRoom", "Apple", 9, 1.5f, players);
        mOnlineGame = new OnlineGame("ddr", onlineSettings);
        mOnlineGame.increamentCurrentStep();
        mOnlineGame.increamentCurrentStep();
    }

    @Test
    public void calcPlayerIdToRetrieveTopicOrDrawing() {
        TopicDrawingRetrievingUtil util = new TopicDrawingRetrievingUtil(mContext, mOnlineGame, mCurrentPlayer);
        String id = util.calcPlayerIdToRetrieveTopicOrDrawing();

        Assert.assertEquals("d", id);
    }
}