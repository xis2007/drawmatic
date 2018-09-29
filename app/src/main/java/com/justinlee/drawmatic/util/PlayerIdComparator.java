package com.justinlee.drawmatic.util;

import com.justinlee.drawmatic.objects.Player;

import java.util.Comparator;

public class PlayerIdComparator implements Comparator<Player> {
    @Override
    public int compare(Player o1, Player o2) {
        return o1.getPlayerId().compareTo(o2.getPlayerId());
    }
}
