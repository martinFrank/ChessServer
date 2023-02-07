package com.github.martinfrank.games.chessserver.server.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public Game findById(UUID gameId) {
        for(Game game: games){
            if(game.gameId.equals(gameId)){
                return game;
            }
        }
        return null;
    }

    public List<Game> findOpenGames(int maxAmountOfGames) {
        List<Game> allOpenGames = games.stream().filter(g -> g.getGuestPlayer() == null).collect(Collectors.toList());
        if(allOpenGames.size() < maxAmountOfGames){
            return allOpenGames;
        }
        List<Game> result = new ArrayList<>();
        for(int i = 0; i < maxAmountOfGames; i ++){
            result.add(allOpenGames.get(i));
        }
        return result;

    }
}
