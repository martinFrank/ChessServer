package com.github.martinfrank.games.chessserver.server.model.chess;

public class Field {

    public final String row;
    public final String column;

    public Field(String row, String column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public String toString() {
        return "Field{" +
                "row='" + row + '\'' +
                ", column='" + column + '\'' +
                '}';
    }
}
