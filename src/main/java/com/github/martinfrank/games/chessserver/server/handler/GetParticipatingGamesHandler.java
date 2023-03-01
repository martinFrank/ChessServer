package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessmodel.message.FcGetParticipatingGamesMessage;
import com.github.martinfrank.games.chessmodel.message.FsDeclineParticipatingGamesMessage;
import com.github.martinfrank.games.chessmodel.message.FsSubmitParticipatingGamesMessage;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessmodel.model.Player;
import com.github.martinfrank.games.chessserver.server.data.ServerAppDataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetParticipatingGamesHandler extends AbstractHandler<FcGetParticipatingGamesMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetParticipatingGamesHandler.class);
    public GetParticipatingGamesHandler(ServerAppDataPool serverAppDataPool) {
        super(serverAppDataPool);
    }

    @Override
    public void handle(ClientWorker clientWorker, FcGetParticipatingGamesMessage message) {
        LOGGER.debug("handle");
        if(clientWorker == null){
            LOGGER.warn("could not find matching client worker");
            return;
        }
        String declineReason = getDeclinedReason(message.player);
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

    public String getDeclinedReason(Player player){
        if (!serverAppDataPool.currentPlayers.contains(player)){
            return "you are not logged in";
        }
        return null;
    }
}
