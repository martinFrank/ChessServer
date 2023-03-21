package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessmodel.message.deletegame.FcDeleteGameMessage;
import com.github.martinfrank.games.chessserver.server.data.DataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteGameHandler extends AbstractHandler<FcDeleteGameMessage>{

    private static final Logger LOGGER = LoggerFactory.getLogger(GameContentHandler.class);

    public DeleteGameHandler(DataPool dataPool) {
        super(dataPool);
    }

    @Override
    public void handle(ClientWorker clientWorker, FcDeleteGameMessage message) {
        LOGGER.debug("someone should implement this method");
    }
}
