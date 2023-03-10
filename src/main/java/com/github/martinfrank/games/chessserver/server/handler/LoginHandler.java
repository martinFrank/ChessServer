package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessmodel.message.login.FcLoginMessage;
import com.github.martinfrank.games.chessmodel.message.login.FsSubmitLoginMessage;
import com.github.martinfrank.games.chessserver.server.data.DataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginHandler extends AbstractHandler<FcLoginMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginHandler.class);

    public LoginHandler(DataPool dataPool) {
        super(dataPool);
    }

    public void handle(ClientWorker clientWorker, FcLoginMessage message) {
        dataPool.clientMapping.put(message.player.playerId, clientWorker);
        dataPool.currentPlayers.add(message.player);

        FsSubmitLoginMessage response = new FsSubmitLoginMessage();
        LOGGER.debug("sending confirm login: "+response);
        clientWorker.send(dataPool.messageParser.toJson(response));
    }
}
