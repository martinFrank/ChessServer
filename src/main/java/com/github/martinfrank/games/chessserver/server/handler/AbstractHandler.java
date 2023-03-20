package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessmodel.message.Message;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessmodel.model.Player;
import com.github.martinfrank.games.chessserver.server.data.DataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHandler<T extends Message> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHandler.class);
    final DataPool dataPool;

    public AbstractHandler(DataPool dataPool) {
        this.dataPool = dataPool;
    }

    public abstract void handle(ClientWorker clientWorker,  T message);

    public void sendToOtherParticipant(String jsonResponse, Game game, Player player) {
        LOGGER.debug("sending to other participant: "+jsonResponse);
        Player other = game.getOther(player);
        ClientWorker otherWorker = dataPool.clientMapping.getClientWorker(other);
        if (otherWorker == null){
            LOGGER.warn("could not find other participant from game");
            return;
        }
        otherWorker.send(jsonResponse);
    }

}
