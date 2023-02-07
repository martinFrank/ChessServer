package com.github.martinfrank.games.chessserver.server.model.chess;

public class Fields {

    private final Field[][] fields;
    public Fields(){
        fields = new Field[8][8];
        for (int column = 0; column < 8; column ++){
            for (int row = 0; row < 8; row ++){
                String columnChar = ""+(char) ('A' + column);
                String rowChar = ""+(char) ('8' - row);
                fields[row][column] = new Field(rowChar, columnChar);
            }
        }
    }

    public Field getField(char column, int row) {
        int c = getColumn(column);
        int r = getRow(row);
        return fields[r][c];
    }

    private int getColumn(char column) {
        switch (column){
            case 'A':
            case 'a':
                return 0;
            case 'B':
            case 'b':
                return 1;
            case 'C':
            case 'c':
                return 2;
            case 'D':
            case 'd':
                return 3;
            case 'E':
            case 'e':
                return 4;
            case 'F':
            case 'f':
                return 5;
            case 'G':
            case 'g':
                return 6;
            case 'H':
            case 'h':
                return 7;
        }
        return -1;
    }

    private int getRow(int row) {
        return 8 - row;
    }
}
