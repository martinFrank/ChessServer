package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessmodel.message.FcGetOpenGamesMessage;
import com.github.martinfrank.games.chessmodel.message.FsDeclineParticipatingGamesMessage;
import com.github.martinfrank.games.chessmodel.message.FsSubmitOpenGamesMessage;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessmodel.model.Player;
import com.github.martinfrank.games.chessserver.server.data.ServerAppDataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetOpenGameHandler extends AbstractHandler<FcGetOpenGamesMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetOpenGameHandler.class);
    public GetOpenGameHandler(ServerAppDataPool serverAppDataPool) {
        super(serverAppDataPool);
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
            FsDeclineParticipatingGamesMessage decline = new FsDeclineParticipatingGamesMessage(declineReason);
            clientWorker.send(serverAppDataPool.messageParser.toJson(decline));
            return;
        }
        List<Game> games = serverAppDataPool.currentGames.findOpenGames();
        FsSubmitOpenGamesMessage response = new FsSubmitOpenGamesMessage(games);
        clientWorker.send(serverAppDataPool.messageParser.toJson(response));
    }

    private String getDeclinedReason(Player player) {
        if (!serverAppDataPool.currentPlayers.contains(player)){
            return "you are not logged in";
        }
        return null;
    }
}
