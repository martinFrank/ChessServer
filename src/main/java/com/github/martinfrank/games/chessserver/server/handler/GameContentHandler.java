package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessmodel.message.getgamecontent.FcGetGameContentMessage;
import com.github.martinfrank.games.chessmodel.message.getgamecontent.FsDeclineGameContentMessage;
import com.github.martinfrank.games.chessmodel.message.getgamecontent.FsSubmitGameContentMessage;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessserver.server.data.DataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameContentHandler extends AbstractHandler<FcGetGameContentMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameContentHandler.class);

    public GameContentHandler(DataPool dataPool) {
        super(dataPool);
    }


    @Override
    public void handle(ClientWorker clientWorker, FcGetGameContentMessage message) {
        if(clientWorker == null){
            LOGGER.warn("could not find matching client worker");
            return;
        }

        Game game = dataPool.currentGames.findById(message.game.gameId);
        String declineReason = getDeclinedReason(game, message);
        if(declineReason != null){
            LOGGER.warn("declining reason = "+declineReason);
            FsDeclineGameContentMessage decline = new FsDeclineGameContentMessage(declineReason);
            LOGGER.debug("sending decline message: "+decline);
            clientWorker.send(dataPool.messageParser.toJson(decline));
            return;
        }
        FsSubmitGameContentMessage response = new FsSubmitGameContentMessage(game.gameContent);
        LOGGER.debug("sending submit message: "+response);
        clientWorker.send(dataPool.messageParser.toJson(response));
    }

    private String getDeclinedReason(Game game, FcGetGameContentMessage message) {
        if(!dataPool.currentPlayers.contains(message.player) ){
            return "Player '"+message.player+"' is not registered";
        }
        if(game == null){
            return "Cannot find game with UUID '"+message.game.gameId+"'";
        }
        if(!game.isParticipant(message.player)){
            return "Player '"+message.player+"' is not part of this game";
        }
        return null;
    }
}
