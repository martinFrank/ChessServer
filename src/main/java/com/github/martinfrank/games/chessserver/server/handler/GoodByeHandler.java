package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessmodel.message.disconnect.FsSubmitDisconnectMessage;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessmodel.model.Player;
import com.github.martinfrank.games.chessserver.server.data.DataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class GoodByeHandler { //does not extend Abstract Handler since this is a special case Handler

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodByeHandler.class);
    private final DataPool dataPool;

    public GoodByeHandler(DataPool dataPool) {
        this.dataPool = dataPool;
    }

    public void handleDisconnect(ClientWorker clientWorker) {
        LOGGER.debug("client has disconnected: '" + clientWorker + "'");
        UUID playerId = dataPool.clientMapping.getPlayerId(clientWorker);
        Player player = dataPool.currentPlayers.get(playerId);
        sendGoodbyeToOpenGames(player);
        dataPool.currentPlayers.remove(playerId);
        dataPool.clientMapping.remove(clientWorker);
    }

    private void sendGoodbyeToOpenGames(Player player) {
        List<Game> games = dataPool.currentGames.getParticipatingGames(player);
        for (Game game : games) {
            LOGGER.debug("sending goodbye");
            ClientWorker worker = dataPool.clientMapping.getClientWorker(game.getOther(player));
            if (worker != null) {
                FsSubmitDisconnectMessage message = new FsSubmitDisconnectMessage(player, game);
                LOGGER.debug("message = "+message);
                String json = dataPool.messageParser.toJson(message);
                worker.send(json);
            }
        }
    }

}
