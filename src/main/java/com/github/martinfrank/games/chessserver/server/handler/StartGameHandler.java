package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessmodel.message.FcStartGameMessage;
import com.github.martinfrank.games.chessmodel.message.FsDeclineStartGameMessage;
import com.github.martinfrank.games.chessmodel.message.FsSubmitCreatedGameMessage;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessserver.server.data.ServerAppDataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartGameHandler extends AbstractHandler<FcStartGameMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartGameHandler.class);

    public StartGameHandler(ServerAppDataPool serverAppDataPool) {
        super(serverAppDataPool);
    }

    public void handle(ClientWorker clientWorker, FcStartGameMessage message) {
        if(clientWorker == null){
            LOGGER.warn("could not find matching client worker");
            return;
        }
        Game game = serverAppDataPool.currentGames.findById(message.gameId);
        String reason = getDeclineReasonForStartGame(game, message);
        if (reason != null) {
            LOGGER.warn("declining reason = "+reason);
            FsDeclineStartGameMessage response = new FsDeclineStartGameMessage(reason);
            clientWorker.send(serverAppDataPool.messageParser.toJson(response));
            return;
        }
        game.gameContent.setStarted(true);
        FsSubmitCreatedGameMessage response = new FsSubmitCreatedGameMessage(game);
        String jsonResponse = serverAppDataPool.messageParser.toJson(response);
        clientWorker.send(jsonResponse);
        sendToGuest(game, jsonResponse);
    }

    private String getDeclineReasonForStartGame(Game game, FcStartGameMessage message) {
        if (!serverAppDataPool.currentPlayers.contains(message.player)){
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
