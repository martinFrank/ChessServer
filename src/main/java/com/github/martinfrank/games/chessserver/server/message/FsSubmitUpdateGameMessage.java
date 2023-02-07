package com.github.martinfrank.games.chessserver.server.message;

import com.github.martinfrank.games.chessserver.server.model.Game;

public class FsSubmitUpdateGameMessage extends Message{

    public final Game game;
    public String changes;

    public FsSubmitUpdateGameMessage(Game game, String changes) {
        super(MessageType.FS_SUBMIT_UPDATE_GAME);
        this.game = game;
        this.changes = changes;
    }

    @Override
    public String toString() {
        return "FsSubmitUpdateGameMessage{" +
                "game=" + game +
                ", changes='" + changes + '\'' +
                ", msgType=" + msgType +
                ", version=" + version +
                '}';
    }
}
