package com.github.martinfrank.games.chessserver.server.data;

import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessmodel.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Games {

    private static final Logger LOGGER = LoggerFactory.getLogger(Games.class);
    private static final int DEFAULT_LIMIT = 5;

    private final List<Game> games = new ArrayList<>();


    public List<Game> getParticipatingGames(Player player) {
        return getParticipatingGames(player, DEFAULT_LIMIT);
    }

    public List<Game> getParticipatingGames(Player player, int maxAmountOfGames ) {
        return games.stream()
                .filter(game -> game.isParticipant(player))
                .limit(maxAmountOfGames)
                .collect(Collectors.toList());
    }

    public Game createGame(Player player) {
        Game game = new Game(UUID.randomUUID(), player);
        games.add(game);
        LOGGER.debug("games.size() = "+games.size());
        return game;
    }

    public Game findById(UUID gameId) {
        return games.stream()
                .filter(g->g.gameId.equals(gameId))
                .findAny()
                .orElse(null);
    }

    public List<Game> findOpenGames() {
        return findOpenGames(DEFAULT_LIMIT);
    }

    public List<Game> findOpenGames(int maxAmountOfGames) {
        return games.stream()
//                .filter(g -> g.getGuestPlayer() == null)
                .limit(maxAmountOfGames)
                .collect(Collectors.toList());

    }

    @Override
    public String toString() {
        return "Games{" +
                "games.size=" + games.size() +
                '}';
    }

    private void updatePlayers(Game game, Players players){

    }
}
