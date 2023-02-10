package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessmodel.message.FcGetParticipatingGamesMessage;
import com.github.martinfrank.games.chessmodel.message.FsDeclineParticipatingGamesMessage;
import com.github.martinfrank.games.chessmodel.message.FsSubmitParticipatingGamesMessage;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessserver.server.data.ServerAppDataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetParticipatingGamesHandler extends AbstractHandler<FcGetParticipatingGamesMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetParticipatingGamesHandler.class);
    public GetParticipatingGamesHandler(ServerAppDataPool serverAppDataPool, FcGetParticipatingGamesMessage message) {
        super(serverAppDataPool, message);
    }

    public void handle() {
        LOGGER.debug("handle");
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
        List<Game> participatingGames = serverAppDataPool.currentGames.getParticipatingGames(message.player);
        FsSubmitParticipatingGamesMessage submitServerInfoMessage = new FsSubmitParticipatingGamesMessage(participatingGames);
        clientWorker.send(serverAppDataPool.messageParser.toJson(submitServerInfoMessage));
    }

    public String getDeclinedReason(){
        if (!serverAppDataPool.currentPlayers.contains(message.player)){
            return "you are not logged in";
        }
        return null;
    }
}
