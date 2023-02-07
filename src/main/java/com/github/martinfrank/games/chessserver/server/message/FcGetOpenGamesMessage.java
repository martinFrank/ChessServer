package com.github.martinfrank.games.chessserver.server.message;

import com.github.martinfrank.games.chessserver.server.model.Player;

public class FcGetOpenGamesMessage extends Message{

    public FcGetOpenGamesMessage() {
        super(MessageType.FC_GET_OPEN_GAMES);
    }

    @Override
    public String toString() {
        return "FcGetOpenGamesMessage{" +
                ", msgType=" + msgType +
                ", version=" + version +
                '}';
    }
}
