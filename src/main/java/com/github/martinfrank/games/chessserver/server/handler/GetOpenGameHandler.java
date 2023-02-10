package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessmodel.message.FcGetOpenGamesMessage;
import com.github.martinfrank.games.chessmodel.message.FsDeclineParticipatingGamesMessage;
import com.github.martinfrank.games.chessmodel.message.FsSubmitOpenGamesMessage;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessserver.server.data.ServerAppDataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetOpenGameHandler extends AbstractHandler<FcGetOpenGamesMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetOpenGameHandler.class);
    public GetOpenGameHandler(ServerAppDataPool serverAppDataPool, FcGetOpenGamesMessage message) {
        super(serverAppDataPool, message);
    }

    public void handle() {
        ClientWorker clientWorker = serverAppDataPool.clientMapping.getClientWorker(message.player);
        if(clientWorker == null){
            LOGGER.warn("could not find matching client worker");
            return;
        }
        String declineReason = getDeclinedReason();
        if(declineReason != null){
            LOGGER.warn("declining reason = "+declineReason);
            FsDeclineParticipatingGamesMessage decline = new FsDeclineParticipatingGamesMessage(declineReason);
            clientWorker.send(serverAppDataPool.messageParser.toJson(decline));
            return;
        }
        List<Game> games = serverAppDataPool.currentGames.findOpenGames(10);
        FsSubmitOpenGamesMessage response = new FsSubmitOpenGamesMessage(games);
        clientWorker.send(serverAppDataPool.messageParser.toJson(response));
    }

    private String getDeclinedReason() {
        if (!serverAppDataPool.currentPlayers.contains(message.player)){
            return "you are not logged in";
        }
        return null;
    }
}
