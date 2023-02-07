package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessserver.server.message.FcSelectFigureMessage;
import com.github.martinfrank.games.chessserver.server.message.FsDeclineSelectFigureMessage;
import com.github.martinfrank.games.chessserver.server.message.FsSubmitSelectFigureMessage;
import com.github.martinfrank.games.chessserver.server.model.Game;
import com.github.martinfrank.games.chessserver.server.model.ServerAppDataPool;
import com.github.martinfrank.games.chessserver.server.model.chess.Field;
import com.github.martinfrank.games.chessserver.server.model.chess.Figure;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SelectFigureHandler extends AbstractHandler<FcSelectFigureMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelectFigureHandler.class);

    public SelectFigureHandler(ServerAppDataPool serverAppDataPool, FcSelectFigureMessage message) {
        super(serverAppDataPool, message);
    }

    public void handle() {
        ClientWorker clientWorker = serverAppDataPool.clientMapping.getClientWorker(message.player);
        if(clientWorker == null){
            LOGGER.warn("could not find matching client worker");
            return;
        }
        Game game = serverAppDataPool.currentGames.findById(message.gameId);
        String reason = getDeclineReason(game, message);
        if (reason != null) {
            FsDeclineSelectFigureMessage response = new FsDeclineSelectFigureMessage(reason);
            clientWorker.send(serverAppDataPool.messageParser.toJson(response));
            return;
        }
        Figure figure = game.board.findFigure(message.field);
        List<Field> path = game.board.getPath(message.field);
        FsSubmitSelectFigureMessage submitServerInfoMessage = new FsSubmitSelectFigureMessage(game, figure, message.field, path);
        String json = serverAppDataPool.messageParser.toJson(submitServerInfoMessage);
        clientWorker.send(json);
        sendToOtherParticipant(json, game, message.player);
    }



    private String getDeclineReason(Game game, FcSelectFigureMessage message) {
        if(game == null){
            return "cannot find game with id "+message.gameId;
        }
        if(!game.hostPlayer.equals(message.player) || !game.getGuestPlayer().equals(message.player)){
            return "you are not part of this game";
        }
        if(!game.isStarted() ){
            return "the game is not started yet";
        }
        if(game.board.findFigure(message.field) == null){
            return "there is no figure on that field";
        }
        return null;
    }
}
