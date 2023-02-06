package com.github.martinfrank.games.chessserver.server.model;

import java.util.UUID;

public class Player {

    public final UUID playerId;
    public final String playerName;

    private transient boolean isOnline ;

    public Player(UUID playerId, String playerName ){
        this.playerId = playerId;
        this.playerName = playerName;
    }


}
