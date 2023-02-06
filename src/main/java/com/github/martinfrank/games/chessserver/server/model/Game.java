package com.github.martinfrank.games.chessserver.server.model;


import java.util.UUID;

//laufendes Spiel
public class Game {

    public final UUID gameId = UUID.randomUUID();
    public final Player hostPlayer;
    private Player guestPlayer;
    private Player currentPlayerId;
    private Player white;
    private Player black;
    private boolean isStarted;
    private boolean isHostOnline;
    private boolean isGuestOnline;

    //private List<Figure> figures
    //private List<Move> moveHistory;

    public Game(Player hostPlayer) {
        this.hostPlayer = hostPlayer;
        white = hostPlayer;
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
    }

    public Player getWhite() {
        return white;
    }

    public void setWhite(Player white) {
        this.white = white;
    }

    public Player getBlack() {
        return black;
    }

    public void setBlack(Player black) {
        this.black = black;
    }
}
