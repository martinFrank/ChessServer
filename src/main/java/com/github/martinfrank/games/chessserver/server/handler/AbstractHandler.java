package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessserver.server.message.Message;
import com.github.martinfrank.games.chessserver.server.model.Game;
import com.github.martinfrank.games.chessserver.server.model.ServerAppDataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;

public class AbstractHandler <T extends Message> {

    final ServerAppDataPool serverAppDataPool;
    final T message;

    public AbstractHandler(ServerAppDataPool serverAppDataPool, T message) {
        this.serverAppDataPool = serverAppDataPool;
        this.message = message;
    }

    public void sendToGuest(Game game, String jsonResponse) {
        ClientWorker guestWorker = serverAppDataPool.clientMapping.getClientWorker(game.getGuestPlayer());
        if (guestWorker != null){
            guestWorker.send(jsonResponse);
        }
    }
}
