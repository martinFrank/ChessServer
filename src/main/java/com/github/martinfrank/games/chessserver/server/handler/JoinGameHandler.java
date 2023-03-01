package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessmodel.message.FcJoinGameMessage;
import com.github.martinfrank.games.chessmodel.message.FcSelectColorMessage;
import com.github.martinfrank.games.chessmodel.message.FsConfirmJoinGamesMessage;
import com.github.martinfrank.games.chessmodel.message.FsDeclineJoinGameMessage;
import com.github.martinfrank.games.chessmodel.message.FsDeclineSelectColorMessage;
import com.github.martinfrank.games.chessmodel.message.FsSubmitSelectColorMessage;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessserver.server.data.ServerAppDataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JoinGameHandler extends AbstractHandler<FcJoinGameMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoinGameHandler.class);
    public JoinGameHandler(ServerAppDataPool serverAppDataPool) {
        super(serverAppDataPool);
    }

    @Override
    public void handle(ClientWorker clientWorker, FcJoinGameMessage message) {
        if(clientWorker == null){
            LOGGER.warn("could not find matching client worker");
            return;
        }
        Game game = serverAppDataPool.currentGames.findById(message.gameId);
        String reason = getDeclineReason(game, message);
        if (reason != null) {
            LOGGER.warn("declining reason = "+reason);
            FsDeclineJoinGameMessage response = new FsDeclineJoinGameMessage(reason);
            clientWorker.send(serverAppDataPool.messageParser.toJson(response));
            return;
        }

        game.setGuestPlayer(message.player);
        FsConfirmJoinGamesMessage response = new FsConfirmJoinGamesMessage(message.player, game);
        String jsonResponse = serverAppDataPool.messageParser.toJson(response);
        clientWorker.send(jsonResponse);
        sendToOtherParticipant(jsonResponse, game, message.player);
    }

    private String getDeclineReason(Game game, FcJoinGameMessage message) {
        if (!serverAppDataPool.currentPlayers.contains(message.player)){
            return "you are not logged in";
        }
        if (game == null) {
            return "join game declined, no game with id " + message.gameId + " found";
        }
        if (game.gameContent.isStarted()) {
            return "join game declined, game with id " + message.gameId + " is already started";
        }
        return null;
    }
}
