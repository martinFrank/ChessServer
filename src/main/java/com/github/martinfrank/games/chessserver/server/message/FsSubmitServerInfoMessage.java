package com.github.martinfrank.games.chessserver.server.message;

import com.github.martinfrank.games.chessserver.server.model.Game;

import java.util.List;

public class FsSubmitServerInfoMessage extends Message{

    private final List<Game> games;
    public FsSubmitServerInfoMessage(List<Game> games) {
        super(MessageType.FS_SUBMIT_SERVER_INFO);
        this.games = games;
    }
}
