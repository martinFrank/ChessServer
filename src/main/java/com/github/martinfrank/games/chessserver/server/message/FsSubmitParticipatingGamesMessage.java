package com.github.martinfrank.games.chessserver.server.message;

import com.github.martinfrank.games.chessserver.server.model.Game;

import java.util.List;

public class FsSubmitParticipatingGamesMessage extends Message{

    public final List<Game> participatingGames;

    public FsSubmitParticipatingGamesMessage(List<Game> participatingGames) {
        super(MessageType.FS_SUBMIT_PARTICIPATING_GAMES);
        this.participatingGames = participatingGames;
    }

    @Override
    public String toString() {
        return "FsSubmitServerInfoMessage{" +
                "participatingGames=" + participatingGames +
                ", msgType=" + msgType +
                ", version=" + version +
                '}';
    }
}
