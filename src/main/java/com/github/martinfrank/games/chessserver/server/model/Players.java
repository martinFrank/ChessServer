package com.github.martinfrank.games.chessserver.server.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Players {

    private static final Logger LOGGER = LoggerFactory.getLogger(Players.class);

    private final List<Player> players = new ArrayList<>();

    public void add(Player player) {
        players.add(player);
        LOGGER.debug("players.size(): " + players.size());
    }

    public void remove(UUID playerId) {
        for(Player player: players){
            if(player.playerId.equals(playerId)){
                players.remove(player);
                break;
            }
        }
        LOGGER.debug("players.size(): " + players.size());
    }
}
