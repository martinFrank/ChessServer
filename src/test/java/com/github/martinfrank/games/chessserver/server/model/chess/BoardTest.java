package com.github.martinfrank.games.chessserver.server.model.chess;

import org.junit.Test;

import java.util.Map;

public class BoardTest {

    @Test
    public void testBoard(){
        Board board = new Board();
        for(Map.Entry<Field, Figure> entry: board.lineUp.entrySet()){
            System.out.println(entry.getKey()+" -> "+entry.getValue());
        }

    }

}