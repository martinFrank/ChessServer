package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessmodel.message.getgamecontent.FcGetGameContentMessage;
import com.github.martinfrank.games.chessmodel.message.getgamecontent.FsDeclineGameContentMessage;
import com.github.martinfrank.games.chessmodel.message.getgamecontent.FsSubmitGameContentMessage;
import com.github.martinfrank.games.chessmodel.message.movefigure.FcMoveFigureMessage;
import com.github.martinfrank.games.chessmodel.message.movefigure.FsDeclineMoveFigureMessage;
import com.github.martinfrank.games.chessmodel.message.movefigure.FsSubmitMoveFigureMessage;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessmodel.model.chess.Field;
import com.github.martinfrank.games.chessserver.server.data.DataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveFigureHandler extends AbstractHandler<FcMoveFigureMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameContentHandler.class);

    public MoveFigureHandler(DataPool dataPool) {
        super(dataPool);
    }

    public void handle(ClientWorker clientWorker, FcMoveFigureMessage message) {
        if(clientWorker == null){
            LOGGER.warn("could not find matching client worker");
            return;
        }

        Game game = dataPool.currentGames.findById(message.gameId);
        String declineReason = getDeclinedReason(game, message);
        if(declineReason != null){
            LOGGER.warn("declining reason = "+declineReason);
            FsDeclineMoveFigureMessage decline = new FsDeclineMoveFigureMessage(declineReason);
            LOGGER.debug("sending decline message: "+decline);
            clientWorker.send(dataPool.messageParser.toJson(decline));
            return;
        }

        game.gameContent.moveFigure(message.from, message.to);

        FsSubmitMoveFigureMessage response = new FsSubmitMoveFigureMessage(message.player, game, message.from, message.to);
        LOGGER.debug("sending submit message: "+response);
        String jsonResponse = dataPool.messageParser.toJson(response);
        clientWorker.send(jsonResponse);
        sendToOtherParticipant(jsonResponse, game, message.player);
    }

    private String getDeclinedReason(Game game, FcMoveFigureMessage message) {
        if(!dataPool.currentPlayers.contains(message.player) ){
            return "Player '"+message.player+"' is not registered";
        }
        if(game == null){
            return "Cannot find game with UUID '"+message.gameId+"'";
        }
        if(!game.isParticipant(message.player)){
            return "Player '"+message.player+"' is not part of this game";
        }
        if(!game.gameContent.isValidMove(message.from, message.to, message.player)){
            return "It is not valid for player '"+message.player+"' to move from '"+message.from+"' to '"+message.to+"'";
        }
        return null;
    }
}
