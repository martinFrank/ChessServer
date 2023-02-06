package com.github.martinfrank.games.chessserver.server.model;


import java.util.UUID;

//laufendes Spiel
public class Game {

    public final UUID gameId = UUID.randomUUID();
    private UUID hostId;
    private UUID guestId;
    private UUID currentPlayerId;
    private UUID white;
    private UUID black;
    //private List<Figure> figures

    public Game() {

    }
    public Game(UUID hostId) {
        this.hostId = hostId;
    }
}
