package com.github.martinfrank.games.chessserver.server.model.chess;

import java.util.ArrayList;
import java.util.List;

public class Figures {

    private static final List<Figure> WHITE_FIGURES = createWhite();
    private static final List<Figure> BLACK_FIGURES = createBlack();


    private static List<Figure> createBlack() {
        return createFigures(Color.BLACK);
    }

    private static List<Figure> createWhite() {
        return createFigures(Color.WHITE);
    }

    private static List<Figure> createFigures(Color color) {
        List<Figure> figures = new ArrayList<>();
//        for (int i = 0; i < 8; i ++){
        String pawnSymbol = color == Color.WHITE ? "\u2659" : "\u265F";
        Figure pawn = new Figure(pawnSymbol, color, Figurine.PAWN);
        figures.add(pawn);
//        }
//        for (int i = 0; i < 2; i ++){
        String knightSymbol = color == Color.WHITE ? "\u2658" : "\u265E";
        Figure knight = new Figure(knightSymbol, color, Figurine.KNIGHT);
        figures.add(knight);
//        }
//        for (int i = 0; i < 2; i ++){
        String bishopSymbol = color == Color.WHITE ? "\u2657" : "\u265D";
        Figure bishop = new Figure(bishopSymbol, color, Figurine.BISHOP);
        figures.add(bishop);
//        }
//        for (int i = 0; i < 2; i ++){
        String rookSymbol = color == Color.WHITE ? "\u2656" : "\u265C";
        Figure rook = new Figure(rookSymbol, color, Figurine.ROOK);
        figures.add(rook);
//        }
        String queenSymbol = color == Color.WHITE ? "\u2655" : "\u265B";
        Figure queen = new Figure(queenSymbol, color, Figurine.QUEEN);
        figures.add(queen);

        String kingSymbol = color == Color.WHITE ? "\u2654" : "\u265A";
        Figure king = new Figure(kingSymbol, color, Figurine.KING);
        figures.add(king);
        return figures;
    }

    public static Figure getFigure(Figurine figurine, Color color) {
        if(color == Color.WHITE){
            return findFigurine(figurine, WHITE_FIGURES);

        }
        if(color == Color.BLACK){
            return findFigurine(figurine, BLACK_FIGURES);
        }
        throw new IllegalArgumentException();
    }

    private static Figure findFigurine(Figurine rook, List<Figure> figures) {
        return figures.stream().filter(f -> f.type == rook).findAny().orElseThrow(IllegalArgumentException::new);
    }
}
