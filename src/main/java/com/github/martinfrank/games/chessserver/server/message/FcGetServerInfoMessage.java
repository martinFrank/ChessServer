package com.github.martinfrank.games.chessserver.server.message;

import com.github.martinfrank.games.chessserver.server.model.Player;

public class FcGetServerInfoMessage extends Message{

    private final Player player;

    public FcGetServerInfoMessage(Player player) {
        super(MessageType.FC_GET_SERVER_INFO);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "FcGetServerInfoMessage{" +
                "player=" + player +
                ", msgType=" + msgType +
                ", version=" + version +
                '}';
    }
}
