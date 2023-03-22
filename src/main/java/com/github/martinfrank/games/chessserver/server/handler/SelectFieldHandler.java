package com.github.martinfrank.games.chessserver.server.handler;

import com.github.martinfrank.games.chessmodel.message.selectfield.FcSelectFieldMessage;
import com.github.martinfrank.games.chessmodel.message.selectfield.FsDeclineSelectFieldMessage;
import com.github.martinfrank.games.chessmodel.message.selectfield.FsSubmitSelectFieldMessage;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessserver.server.data.DataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectFieldHandler extends AbstractHandler<FcSelectFieldMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelectFieldHandler.class);

    public SelectFieldHandler(DataPool dataPool) {
        super(dataPool);
    }

    public void handle(ClientWorker clientWorker, FcSelectFieldMessage message) {
        LOGGER.debug("handle select field start");
        if (clientWorker == null) {
            LOGGER.warn("could not find matching client worker");
            return;
        }
        Game game = dataPool.currentGames.findById(message.gameId);

        String reason = getDeclineReason(game, message);
        if (reason != null) {
            LOGGER.warn("declining reason = " + reason);
            FsDeclineSelectFieldMessage response = new FsDeclineSelectFieldMessage(reason);
            clientWorker.send(dataPool.messageParser.toJson(response));
            return;
        }

        dataPool.updatePlayersInGames(game);
        game.chessGame.getThisParticipant(message.player).selectField(message.field);

        FsSubmitSelectFieldMessage submitServerInfoMessage = new FsSubmitSelectFieldMessage(message.player, game, message.field);
        String json = dataPool.messageParser.toJson(submitServerInfoMessage);
        LOGGER.debug("handle select field done, sending json: " + json);
        clientWorker.send(json);
        sendToOtherParticipant(json, game, message.player);
    }


    private String getDeclineReason(Game game, FcSelectFieldMessage message) {
        if (game == null) {
            return "cannot find game with id " + message.gameId;
        }
        if (!game.isParticipant(message.player)) {
            return "you are not part of this game";
        }
        if (!game.chessGame.isStarted()) {
            return "the game is not started yet";
        }
        return null;
    }
}
