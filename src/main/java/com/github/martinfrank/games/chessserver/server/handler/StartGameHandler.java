package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessserver.server.message.FcStartGameMessage;
import com.github.martinfrank.games.chessserver.server.message.FsDeclineSelectColorMessage;
import com.github.martinfrank.games.chessserver.server.message.FsDeclineStartGameMessage;
import com.github.martinfrank.games.chessserver.server.message.FsSubmitCreatedGameMessage;
import com.github.martinfrank.games.chessserver.server.model.Game;
import com.github.martinfrank.games.chessserver.server.model.ServerAppDataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartGameHandler extends AbstractHandler<FcStartGameMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartGameHandler.class);

    public StartGameHandler(ServerAppDataPool serverAppDataPool, FcStartGameMessage message) {
        super(serverAppDataPool, message);
    }

    public void handle() {
        ClientWorker clientWorker = serverAppDataPool.clientMapping.getClientWorker(message.player);
        if(clientWorker == null){
            LOGGER.warn("could not find matching client worker");
            return;
        }
        Game game = serverAppDataPool.currentGames.findById(message.gameId);
        String reason = getDeclineReasonForStartGame(game, message);
        if (reason != null) {
            FsDeclineStartGameMessage response = new FsDeclineStartGameMessage(reason);
            clientWorker.send(serverAppDataPool.messageParser.toJson(response));
            return;
        }
        game.setStarted(true);
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
        if (game.isStarted()) {
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
