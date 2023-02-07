package com.github.martinfrank.games.chessserver.server.message;

import com.github.martinfrank.games.chessserver.server.model.Game;
import com.github.martinfrank.games.chessserver.server.model.chess.Field;
import com.github.martinfrank.games.chessserver.server.model.chess.Figure;

import java.util.List;

public class FsSubmitSelectFigureMessage extends Message{

    public final Game game;
    public final Figure figure;
    public final Field field;

    public final List<Field> path;
    public FsSubmitSelectFigureMessage(Game game, Figure figure, Field field, List<Field> path) {
        super(MessageType.FS_SUBMIT_SELECT_FIGURE);
        this.game = game;
        this.figure = figure;
        this.field = field;
        this.path = path;
    }

    @Override
    public String toString() {
        return "FsSubmitSelectFigureMessage{" +
                "game=" + game +
                ", figure=" + figure +
                ", field=" + field +
                ", path=" + path +
                ", msgType=" + msgType +
                ", version=" + version +
                '}';
    }
}
