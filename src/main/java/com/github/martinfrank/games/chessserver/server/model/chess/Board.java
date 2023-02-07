package com.github.martinfrank.games.chessserver.server.model.chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    private transient final Fields fields = new Fields();
    public final Map<Field, Figure> lineUp = new HashMap<>();
    public final List<Figure> beatenFigures = new ArrayList<>();

    public Board(){
        lineUpFigureAt(Figurine.ROOK, Color.WHITE, 'A', 1);
        lineUpFigureAt(Figurine.KNIGHT, Color.WHITE, 'B', 1);
        lineUpFigureAt(Figurine.BISHOP, Color.WHITE, 'C', 1);
        lineUpFigureAt(Figurine.QUEEN, Color.WHITE, 'D', 1);
        lineUpFigureAt(Figurine.KING, Color.WHITE, 'E', 1);
        lineUpFigureAt(Figurine.BISHOP, Color.WHITE, 'F', 1);
        lineUpFigureAt(Figurine.KNIGHT, Color.WHITE, 'G', 1);
        lineUpFigureAt(Figurine.ROOK, Color.WHITE, 'H', 1);
        lineUpFigureAt(Figurine.PAWN, Color.WHITE, 'A', 2);
        lineUpFigureAt(Figurine.PAWN, Color.WHITE, 'B', 2);
        lineUpFigureAt(Figurine.PAWN, Color.WHITE, 'C', 2);
        lineUpFigureAt(Figurine.PAWN, Color.WHITE, 'D', 2);
        lineUpFigureAt(Figurine.PAWN, Color.WHITE, 'E', 2);
        lineUpFigureAt(Figurine.PAWN, Color.WHITE, 'F', 2);
        lineUpFigureAt(Figurine.PAWN, Color.WHITE, 'G', 2);
        lineUpFigureAt(Figurine.PAWN, Color.WHITE, 'H', 2);


        lineUpFigureAt(Figurine.ROOK, Color.BLACK, 'A', 8);
        lineUpFigureAt(Figurine.KNIGHT, Color.BLACK, 'B', 8);
        lineUpFigureAt(Figurine.BISHOP, Color.BLACK, 'C', 8);
        lineUpFigureAt(Figurine.QUEEN, Color.BLACK, 'D', 8);
        lineUpFigureAt(Figurine.KING, Color.BLACK, 'E', 8);
        lineUpFigureAt(Figurine.BISHOP, Color.BLACK, 'F', 8);
        lineUpFigureAt(Figurine.KNIGHT, Color.BLACK, 'G', 8);
        lineUpFigureAt(Figurine.ROOK, Color.BLACK, 'H', 8);
        lineUpFigureAt(Figurine.PAWN, Color.BLACK, 'A', 7);
        lineUpFigureAt(Figurine.PAWN, Color.BLACK, 'B', 7);
        lineUpFigureAt(Figurine.PAWN, Color.BLACK, 'C', 7);
        lineUpFigureAt(Figurine.PAWN, Color.BLACK, 'D', 7);
        lineUpFigureAt(Figurine.PAWN, Color.BLACK, 'E', 7);
        lineUpFigureAt(Figurine.PAWN, Color.BLACK, 'F', 7);
        lineUpFigureAt(Figurine.PAWN, Color.BLACK, 'G', 7);
        lineUpFigureAt(Figurine.PAWN, Color.BLACK, 'H', 7);
    }

    private void lineUpFigureAt(Figurine figurine, Color color, char column, int row) {
        Field field = fields.getField(column, row);
        Figure f = Figures.getFigure(figurine, color);
        lineUp.put(field, f);
    }


}
