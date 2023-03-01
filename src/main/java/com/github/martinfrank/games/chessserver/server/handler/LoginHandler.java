package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessmodel.message.FcLoginMessage;
import com.github.martinfrank.games.chessmodel.message.FsConfirmLoginMessage;
import com.github.martinfrank.games.chessserver.server.data.ServerAppDataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginHandler extends AbstractHandler<FcLoginMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginHandler.class);

    public LoginHandler(ServerAppDataPool serverAppDataPool) {
        super(serverAppDataPool);
    }

    public void handle(ClientWorker clientWorker, FcLoginMessage message) {
        serverAppDataPool.clientMapping.put(message.player.playerId, clientWorker);
        serverAppDataPool.currentPlayers.add(message.player);

        FsConfirmLoginMessage response = new FsConfirmLoginMessage();
        LOGGER.debug("sending confirm login: "+response);
        clientWorker.send(serverAppDataPool.messageParser.toJson(response));
    }
}
