package com.github.martinfrank.games.chessserver;

import com.github.martinfrank.games.chessserver.server.ChessServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        new ChessServer().start();
    }
}
