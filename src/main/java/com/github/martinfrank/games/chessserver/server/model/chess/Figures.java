package com.github.martinfrank.games.chessserver.server.model.chess;

import java.util.ArrayList;
import java.util.List;

public class Figures {

    public static final List<Figure> WHITE = createWhite();
    public static final List<Figure> BLACK = createBlack();


    private static List<Figure> createBlack() {
        return createFigures(Color.BLACK);
    }

    private static List<Figure> createWhite() {
        return createFigures(Color.WHITE);
    }

    private static List<Figure> createFigures(Color color) {
        List<Figure> figures = new ArrayList<>();
        for (int i = 0; i < 8; i ++){
            String symbol = color == Color.WHITE ? "\u2659":"\u265F";
            Figure pawn = new Figure(symbol, color, Figurine.PAWN);
            figures.add(pawn);
        }
        for (int i = 0; i < 2; i ++){
            String symbol = color == Color.WHITE ? "\u2658":"\u265E";
            Figure knight = new Figure(symbol, color, Figurine.KNIGHT);
            figures.add(knight);
        }
        for (int i = 0; i < 2; i ++){
            String symbol = color == Color.WHITE ? "\u2657":"\u265D";
            Figure bishop = new Figure(symbol, color, Figurine.BISHOP);
            figures.add(bishop);
        }
        for (int i = 0; i < 2; i ++){
            String symbol = color == Color.WHITE ? "\u2656":"\u265C";
            Figure rook = new Figure(symbol, color, Figurine.ROOK);
            figures.add(rook);
        }
        String queenSymbol = color == Color.WHITE ? "\u2655":"\u265B";
        Figure queen = new Figure(queenSymbol, color, Figurine.QUEEN);
        figures.add(queen);

        String kingSymbol = color == Color.WHITE ? "\u2654":"\u265A";
        Figure king = new Figure(kingSymbol, color, Figurine.KING);
        figures.add(king);
        return figures;
    }

}
