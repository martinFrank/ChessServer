package com.github.martinfrank.games.chessserver.server.message;

public class FcCreateGameMessage extends Message{

    public FcCreateGameMessage() {
        super(MessageType.FC_CREATE_GAME);
    }
}
