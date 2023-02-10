package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessmodel.message.FcSelectColorMessage;
import com.github.martinfrank.games.chessmodel.message.FsDeclineSelectColorMessage;
import com.github.martinfrank.games.chessmodel.message.FsSubmitSelectColorMessage;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessserver.server.data.ServerAppDataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectColorHandler extends AbstractHandler<FcSelectColorMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelectColorHandler.class);
    public SelectColorHandler(ServerAppDataPool serverAppDataPool, FcSelectColorMessage message) {
        super(serverAppDataPool, message);
    }

    public void handle() {
        ClientWorker clientWorker = serverAppDataPool.clientMapping.getClientWorker(message.player);
        if(clientWorker == null){
            LOGGER.warn("could not find matching client worker");
            return;
        }
        Game game = serverAppDataPool.currentGames.findById(message.gameId);
        String reason = getDeclineReason(game, message);
        if (reason != null) {
            LOGGER.warn("declining reason = "+reason);
            FsDeclineSelectColorMessage response = new FsDeclineSelectColorMessage(reason);
            clientWorker.send(serverAppDataPool.messageParser.toJson(response));
            return;
        }
        game.setHostColor(message.desiredColor);
        String change = "Player " + message.player.playerName + " changed his/her color to " + message.desiredColor;
        FsSubmitSelectColorMessage response = new FsSubmitSelectColorMessage(game, change);
        String jsonResponse = serverAppDataPool.messageParser.toJson(response);
        clientWorker.send(jsonResponse);
        sendToGuest(game, jsonResponse);
    }

    private String getDeclineReason(Game game, FcSelectColorMessage message) {
        if (!serverAppDataPool.currentPlayers.contains(message.player)){
            return "you are not logged in";
        }
        if (game == null) {
            return "change color declined, no game with id " + message.gameId + " found";
        }
        if (game.isStarted()) {
            return "change color declined, game with id " + message.gameId + " is already started";
        }
        if (!game.hostPlayer.equals(message.player)) {
            return "change color declined, you are not host of game with id " + message.gameId;
        }
        return null;
    }
}
