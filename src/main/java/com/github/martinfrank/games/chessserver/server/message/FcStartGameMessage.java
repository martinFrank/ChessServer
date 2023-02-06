package com.github.martinfrank.games.chessserver.server.message;

import com.github.martinfrank.games.chessserver.server.model.Player;
import com.github.martinfrank.games.chessserver.server.model.chess.Color;

import java.util.UUID;

public class FcStartGameMessage extends Message{

    public final Player player;
    public final UUID gameId;

    public FcStartGameMessage(Player player, UUID gameId) {
        super(MessageType.FC_START_GAME);
        this.player = player;
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return "FcSelectColorMessage{" +
                "player=" + player +
                ", gameId=" + gameId +
                ", msgType=" + msgType +
                ", version=" + version +
                '}';
    }
}
