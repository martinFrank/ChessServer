package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessmodel.message.getopengames.FcGetOpenGamesMessage;
import com.github.martinfrank.games.chessmodel.message.getopengames.FsDeclineOpenGamesMessage;
import com.github.martinfrank.games.chessmodel.message.getopengames.FsSubmitOpenGamesMessage;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessmodel.model.Player;
import com.github.martinfrank.games.chessserver.server.data.DataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetOpenGameHandler extends AbstractHandler<FcGetOpenGamesMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetOpenGameHandler.class);
    public GetOpenGameHandler(DataPool dataPool) {
        super(dataPool);
    }

    @Override
    public void handle(ClientWorker clientWorker, FcGetOpenGamesMessage message) {
        if(clientWorker == null){
            LOGGER.warn("could not find matching client worker");
            return;
        }
        String declineReason = getDeclinedReason(message.player);
        if(declineReason != null){
            LOGGER.warn("declining reason = "+declineReason);
            FsDeclineOpenGamesMessage decline = new FsDeclineOpenGamesMessage(declineReason);
            clientWorker.send(dataPool.messageParser.toJson(decline));
            return;
        }
        List<Game> games = dataPool.currentGames.findOpenGames();
        FsSubmitOpenGamesMessage response = new FsSubmitOpenGamesMessage(games);
        clientWorker.send(dataPool.messageParser.toJson(response));
    }

    private String getDeclinedReason(Player player) {
        if (!dataPool.currentPlayers.contains(player)){
            return "you are not logged in";
        }
        return null;
    }
}
