package com.github.martinfrank.games.chessserver.server.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Games {

    private static final Logger LOGGER = LoggerFactory.getLogger(Games.class);

    private final List<Game> games = new ArrayList<>();

    public List<Game> getParticipatingGames(Player player) {
        List<Game> participatingGames = new ArrayList<>();
        for(Game game: games){
            if(player.equals(game.hostPlayer) || player.equals(game.getGuestPlayer())){
                participatingGames.add(game);
            }
        }
        return participatingGames;
    }

    public Game createNew(Player player) {
        Game game = new Game(player);
        games.add(game);
        LOGGER.debug("games.size() = "+games.size());
        return game;
    }
}
