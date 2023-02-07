package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessserver.server.message.FcLoginMessage;
import com.github.martinfrank.games.chessserver.server.message.FsConfirmLoginMessage;
import com.github.martinfrank.games.chessserver.server.model.ServerAppDataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;

public class LoginHandler extends AbstractHandler<FcLoginMessage>{

    private final ClientWorker clientWorker;
    public LoginHandler(ServerAppDataPool serverAppDataPool, FcLoginMessage loginMessage, ClientWorker clientWorker) {
        super(serverAppDataPool, loginMessage);
        this.clientWorker = clientWorker;
    }

    public void handle() {
        serverAppDataPool.clientMapping.put(message.player.playerId, clientWorker);
        serverAppDataPool.currentPlayers.add(message.player);
        clientWorker.send(serverAppDataPool.messageParser.toJson(new FsConfirmLoginMessage()));
    }
}
