package com.github.martinfrank.games.chessserver.server.message;

public class FsDeclineStartGameMessage extends Message{

    public final String reason;

    public FsDeclineStartGameMessage(String reason) {
        super(MessageType.FS_DECLINE_CREATE_GAME);
        this.reason = reason;
    }
}
