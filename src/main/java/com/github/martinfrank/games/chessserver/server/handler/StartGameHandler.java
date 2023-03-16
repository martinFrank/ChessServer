package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessmodel.message.startgame.FcStartGameMessage;
import com.github.martinfrank.games.chessmodel.message.startgame.FsDeclineStartGameMessage;
import com.github.martinfrank.games.chessmodel.message.startgame.FsSubmitStartGameMessage;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessserver.server.data.DataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartGameHandler extends AbstractHandler<FcStartGameMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartGameHandler.class);

    public StartGameHandler(DataPool dataPool) {
        super(dataPool);
    }

    public void handle(ClientWorker clientWorker, FcStartGameMessage message) {
        if(clientWorker == null){
            LOGGER.warn("could not find matching client worker");
            return;
        }
        Game game = dataPool.currentGames.findById(message.gameId);
        String reason = getDeclineReasonForStartGame(game, message);
        if (reason != null) {
            LOGGER.warn("declining reason = "+reason);
            FsDeclineStartGameMessage response = new FsDeclineStartGameMessage(reason);
            clientWorker.send(dataPool.messageParser.toJson(response));
            return;
        }

        game.gameContent.startGame();

        FsSubmitStartGameMessage response = new FsSubmitStartGameMessage(game, game.gameContent);
        String jsonResponse = dataPool.messageParser.toJson(response);
        clientWorker.send(jsonResponse);
        sendToGuest(game, jsonResponse);
    }

    private String getDeclineReasonForStartGame(Game game, FcStartGameMessage message) {
        if (!dataPool.currentPlayers.contains(message.player)){
            return "you are not logged in";
        }
        if (game == null) {
            return "start game declined, no game with id " + message.gameId + " found";
        }
        if (game.gameContent.isStarted()) {
            return "start game declined, game with id " + message.gameId + " is already started";
        }
        if (!game.hostPlayer.equals(message.player)) {
            return "start game declined, you are not host of game with id " + message.gameId;
        }
        if(game.getGuestPlayer() == null){
            return "start game declined, there is no guest in the game with id " + message.gameId;
        }
        return null;
    }
}
