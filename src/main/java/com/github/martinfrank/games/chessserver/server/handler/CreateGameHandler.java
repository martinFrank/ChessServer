package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessmodel.message.FcCreateGameMessage;
import com.github.martinfrank.games.chessmodel.message.FsDeclineCreateGameMessage;
import com.github.martinfrank.games.chessmodel.message.FsSubmitCreatedGameMessage;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessmodel.model.Player;
import com.github.martinfrank.games.chessserver.server.data.ServerAppDataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateGameHandler extends AbstractHandler<FcCreateGameMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateGameHandler.class);

    public CreateGameHandler(ServerAppDataPool serverAppDataPool) {
        super(serverAppDataPool);
    }

    @Override
    public void handle(ClientWorker clientWorker, FcCreateGameMessage message) {
//        ClientWorker clientWorker = serverAppDataPool.clientMapping.getClientWorker(message.player);
        if(clientWorker == null){
            LOGGER.warn("could not find matching client worker");
            return;
        }

        String reason = getDeclineReason(message.player);
        if (reason != null) {
            LOGGER.warn("declining reason = "+reason);
            FsDeclineCreateGameMessage response = new FsDeclineCreateGameMessage(reason);
            LOGGER.debug("handle Fc Create Game, sending decline response "+response);
            clientWorker.send(serverAppDataPool.messageParser.toJson(response));
            return;
        }
        Game game = serverAppDataPool.currentGames.createGame(message.player);
        FsSubmitCreatedGameMessage response = new FsSubmitCreatedGameMessage(game);
        LOGGER.debug("handle Fc Create Game, sending confirm response "+response);
        clientWorker.send(serverAppDataPool.messageParser.toJson(response));
    }

    private String getDeclineReason(Player player) {
        if (!serverAppDataPool.currentPlayers.contains(player)){
            return "you are not logged in";
        }
        return null;
    }
}
