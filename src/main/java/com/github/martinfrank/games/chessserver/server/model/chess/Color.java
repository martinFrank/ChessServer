package com.github.martinfrank.games.chessserver.server.model.chess;

public enum Color {

    WHITE ("white"), BLACK("black");

    private final String colorName;

    Color(String colorName){
        this.colorName = colorName;
    }

    public Color getOpposite(){
        switch (this){
            case BLACK: return WHITE;
            case WHITE: return BLACK;
        }
        throw new IllegalStateException("i am neither wite nor black???");
    }
}
