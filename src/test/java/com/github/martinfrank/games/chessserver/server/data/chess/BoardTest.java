package com.github.martinfrank.games.chessserver.server.data.chess;

import com.github.martinfrank.games.chessmodel.model.chess.Board;
import com.github.martinfrank.games.chessmodel.model.chess.Field;
import com.github.martinfrank.games.chessmodel.model.chess.Figure;
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