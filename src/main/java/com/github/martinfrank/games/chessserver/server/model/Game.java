package com.github.martinfrank.games.chessserver.server.model;


import com.github.martinfrank.games.chessserver.server.model.chess.Board;
import com.github.martinfrank.games.chessserver.server.model.chess.Color;

import java.util.UUID;

//laufendes Spiel
public class Game {

    public final UUID gameId = UUID.randomUUID();
    public final Player hostPlayer;
    private Player guestPlayer;
    private Player currentPlayer;

    private Color hostColor = Color.WHITE;
    private Color guestColor = Color.BLACK;
    private boolean isStarted;
    private boolean isHostOnline;
    private boolean isGuestOnline;

    private long startTime = -1;

    private Board board = new Board();
    //private List<Move> moveHistory;

    public Game(Player hostPlayer) {
        this.hostPlayer = hostPlayer;
        isHostOnline = true;
    }

    public Player getGuestPlayer(){
        return guestPlayer;
    }

    public void setGuestPlayer(Player guestPlayer) {
        this.guestPlayer = guestPlayer;
    }

    public boolean isHostOnline() {
        return isHostOnline;
    }

    public void setHostOnline(boolean hostOnline) {
        isHostOnline = hostOnline;
    }

    public boolean isGuestOnline() {
        return isGuestOnline;
    }

    public void setGuestOnline(boolean guestOnline) {
        isGuestOnline = guestOnline;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
        currentPlayer = hostPlayer;
        startTime = System.currentTimeMillis();
    }


    public void setHostColor(Color desiredColor) {
        hostColor = desiredColor;
        guestColor = desiredColor.getOpposite();
    }

}
