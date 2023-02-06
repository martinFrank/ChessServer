package com.github.martinfrank.games.chessserver.server.model.chess;

public class Figure {

    public final String symbol;
    public final Color color;
    public final Figurine type;

    public Figure(String symbol, Color color, Figurine type) {
        this.symbol = symbol;
        this.color = color;
        this.type = type;
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
