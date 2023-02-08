package com.github.martinfrank.games.chessserver.server.model.chess;

public class Field {

    public final String row;
    public final String column;

    transient int x;
    transient int y;

    public Field(String row, String column) {
        this.row = row;
        this.column = column;
        this.x = x;
        this.y = y;
    }

    Field(int x, int y) {
        this.x = x;
        this.y = y;
        this.row = getRow(x);
        this.column = getColumn((char)y);
    }

    @Override
    public String toString() {
        return "Field{" +
                "row='" + row + '\'' +
                ", column='" + column + '\'' +
                '}';
    }


    private String getColumn(int column) {
        switch (column){
            case 0: return "A";
            case 1: return "B";
            case 2: return "C";
            case 3: return "D";
            case 4: return "E";
            case 5: return "F";
            case 6: return "G";
            case 7: return "H";
        }
        return "?";
    }

    private String getRow(int row) {
        return ""+(8 - row);
    }
}
