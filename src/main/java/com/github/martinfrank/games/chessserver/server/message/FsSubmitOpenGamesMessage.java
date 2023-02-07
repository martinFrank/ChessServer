package com.github.martinfrank.games.chessserver.server.message;

import com.github.martinfrank.games.chessserver.server.model.Game;

import java.util.List;

public class FsSubmitOpenGamesMessage extends Message{

    public final List<Game> games;

    public FsSubmitOpenGamesMessage(List<Game> games) {
        super(MessageType.FS_SUBMIT_OPEN_GAMES);
        this.games = games;
    }

    @Override
    public String toString() {
        return "FsSubmitUpdateGameMessage{" +
                "game=" + games +
                ", msgType=" + msgType +
                ", version=" + version +
                '}';
    }
}
