package com.github.martinfrank.games.chessserver.server.message;

import com.github.martinfrank.games.chessserver.server.model.Game;

public class FsSubmitCreatedGameMessage extends Message{

    public final Game game;

    public FsSubmitCreatedGameMessage(Game game) {
        super(MessageType.FS_SUBMIT_CREATED_GAME);
        this.game = game;
    }
}
