package com.github.martinfrank.games.chessserver.server.model;

import java.util.Objects;
import java.util.UUID;

public class Player {

    public final UUID playerId;
    public final String playerName;

    private transient boolean isOnline ;

    public Player(UUID playerId, String playerName ){
        this.playerId = playerId;
        this.playerName = playerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return playerId.equals(player.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId);
    }
}
