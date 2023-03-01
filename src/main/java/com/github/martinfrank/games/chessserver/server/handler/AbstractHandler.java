package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessmodel.message.Message;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessmodel.model.Player;
import com.github.martinfrank.games.chessserver.server.data.ServerAppDataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHandler<T extends Message> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHandler.class);
    final ServerAppDataPool serverAppDataPool;

    public AbstractHandler(ServerAppDataPool serverAppDataPool) {
        this.serverAppDataPool = serverAppDataPool;
    }

    public abstract void handle(ClientWorker clientWorker,  T message);

    public void sendToGuest(Game game, String jsonResponse) {
        ClientWorker guestWorker = serverAppDataPool.clientMapping.getClientWorker(game.getGuestPlayer());
        if (guestWorker != null){
            guestWorker.send(jsonResponse);
        }
    }

    public void sendToOtherParticipant(String jsonResponse, Game game, Player player) {
        Player other = null;
        if (game.hostPlayer.equals(player)){
            other = game.getGuestPlayer();
        }
        if(game.getGuestPlayer().equals(player)){
            other = game.hostPlayer;
        }
        ClientWorker otherWorker = serverAppDataPool.clientMapping.getClientWorker(other);
        if (otherWorker == null){
            LOGGER.warn("could not find other participant from game");
            return;
        }
        otherWorker.send(jsonResponse);
    }

}
