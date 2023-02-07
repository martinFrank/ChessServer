package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessserver.server.message.FcCreateGameMessage;
import com.github.martinfrank.games.chessserver.server.message.FsDeclineCreateGameMessage;
import com.github.martinfrank.games.chessserver.server.message.FsDeclineSelectColorMessage;
import com.github.martinfrank.games.chessserver.server.message.FsSubmitCreatedGameMessage;
import com.github.martinfrank.games.chessserver.server.model.Game;
import com.github.martinfrank.games.chessserver.server.model.ServerAppDataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateGameHandler extends AbstractHandler<FcCreateGameMessage>{

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateGameHandler.class);

    public CreateGameHandler(ServerAppDataPool serverAppDataPool, FcCreateGameMessage message) {
        super(serverAppDataPool, message);
    }

    public void handle() {
        ClientWorker clientWorker = serverAppDataPool.clientMapping.getClientWorker(message.player);
        if(clientWorker == null){
            LOGGER.warn("could not find matching client worker");
            return;
        }

        String reason = getDeclineReason();
        if (reason != null) {
            FsDeclineCreateGameMessage response = new FsDeclineCreateGameMessage(reason);
            clientWorker.send(serverAppDataPool.messageParser.toJson(response));
            return;
        }
        Game game = serverAppDataPool.currentGames.createNew(message.player);
        FsSubmitCreatedGameMessage response = new FsSubmitCreatedGameMessage(game);
        clientWorker.send(serverAppDataPool.messageParser.toJson(response));
    }

    private String getDeclineReason() {
        if (!serverAppDataPool.currentPlayers.contains(message.player)){
            return "you are not logged in";
        }
        return null;
    }
}