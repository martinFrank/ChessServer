package com.github.martinfrank.games.chessserver.server.message;

import com.github.martinfrank.games.chessserver.server.model.Game;

public class FsSubmitUpdateGameMessage extends Message{

    public final Game game;

    public FsSubmitUpdateGameMessage(Game game) {
        super(MessageType.FS_SUBMIT_UPDATE_GAME);
        this.game = game;
    }
}
