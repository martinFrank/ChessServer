package com.github.martinfrank.games.chessserver.server.model.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Figure {

    public final String symbol;
    public final Color color;
    public final Figurine type;

    public Figure(String symbol, Color color, Figurine type) {
        this.symbol = symbol;
        this.color = color;
        this.type = type;
    }

    public List<Field> getSelectionPath(Field from){
        switch (type){
            case PAWN: return getSelectionPathPawn(from);
            case ROOK: return getSelectionPathRook(from);
            default: return Collections.emptyList();
        }
    }

    private List<Field> getSelectionPathRook(Field from) {
        return Collections.emptyList();
    }

    private List<Field> getSelectionPathPawn(Field from) {
        if(color == Color.WHITE){
            if (from.x == 6){
                Field f1 = new Field(from.x, 5);
                Field f2 = new Field(from.x, 4);
                return Arrays.asList(f1, f2);
            }
            if(from.x <= 5 && from.x >=1){
                return Collections.singletonList(new Field(from.x, from.y-1));
            }
        }

        if(color == Color.BLACK){
            if (from.x == 6){
                Field f1 = new Field(from.x, 5);
                Field f2 = new Field(from.x, 4);
                return Arrays.asList(f1, f2);
            }
            if(from.x <= 5 && from.x >=1){
                return Collections.singletonList(new Field(from.x, from.y-1));
            }
        }
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return "Figure{" +
                "symbol='" + symbol + '\'' +
                ", color=" + color +
                ", type=" + type +
                '}';
    }
}
