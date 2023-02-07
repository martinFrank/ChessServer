package com.github.martinfrank.games.chessserver;

//import com.github.martinfrank.games.chessserver.server.ChessServer;

import com.github.martinfrank.games.chessserver.server.message.*;
import com.github.martinfrank.games.chessserver.server.model.ClientMapping;
import com.github.martinfrank.games.chessserver.server.model.Game;
import com.github.martinfrank.games.chessserver.server.model.Games;
import com.github.martinfrank.games.chessserver.server.model.Players;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import com.github.martinfrank.tcpclientserver.ServerMessageReceiver;
import com.github.martinfrank.tcpclientserver.TcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

/**
 * Hello world!
 */
public class ChessServerApp implements ServerMessageReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChessServerApp.class);
    private final Games currentGames = new Games();
    private final Players currentPlayers = new Players();
    private final MessageParser messageParser = new MessageParser();

    private final ClientMapping clientMapping = new ClientMapping();

    public void receive(ClientWorker clientWorker, String raw) {
        LOGGER.debug("received raw: " + raw);
        Message message = messageParser.fromJson(raw);
        switch (message.msgType) {
            case UNKNOWN: {
                handleUnknownMessage(clientWorker, raw);
                break;
            }
            case FC_LOGIN: {
                handleLogin(clientWorker, (FcLoginMessage) message);
                break;
            }
            case FC_GET_PARTICIPATING_GAMES: {
                handleGetParticipatingGames(clientWorker, (FcGetParticipatingGamesMessage) message);
                break;
            }
            case FC_GET_OPEN_GAMES:{
                handleGetOpenGames(clientWorker, (FcGetOpenGamesMessage)message);
                break;
            }
            case FC_CREATE_GAME: {
                handleCreateGame(clientWorker, (FcCreateGameMessage) message);
                break;
            }
            case FC_SELECT_COLOR: {
                handleSelectColor(clientWorker, (FcSelectColorMessage) message);
            }
            case FC_START_GAME: {
                handleStartGame(clientWorker, (FcStartGameMessage) message);
            }
            default: {
                handleUnknownMessage(clientWorker, raw);
            }
        }
    }

    private void handleGetOpenGames(ClientWorker clientWorker, FcGetOpenGamesMessage message) {
        LOGGER.debug("handle FC get open games: " + message);
        List<Game> games = currentGames.findOpenGames(10);
        FsSubmitOpenGamesMessage response = new FsSubmitOpenGamesMessage(games);
        clientWorker.send(messageParser.toJson(response));
    }

    private void handleStartGame(ClientWorker clientWorker, FcStartGameMessage message) {
        LOGGER.debug("handle FC start game: " + message);
        Game game = currentGames.findById(message.gameId);
        String reason = getDeclineReasonForStartGame(game, message);
        if (reason != null) {
            FsDeclineSelectColorMessage response = new FsDeclineSelectColorMessage(reason);
            clientWorker.send(messageParser.toJson(response));
            return;
        }
        game.setStarted(true);
        FsSubmitCreatedGameMessage response = new FsSubmitCreatedGameMessage(game);
        String jsonResponse = messageParser.toJson(response);
        clientWorker.send(jsonResponse);
        sendToGuest(game, jsonResponse);
    }

    private void sendToGuest(Game game, String jsonResponse) {
        ClientWorker guestWorker = clientMapping.getClientWorker(game.getGuestPlayer());
        if (guestWorker != null){
            guestWorker.send(jsonResponse);
        }
    }

    private String getDeclineReasonForStartGame(Game game, FcStartGameMessage message) {
        if (game == null) {
            return "start game declined, no game with id " + message.gameId + " found";
        }
        if (game.isStarted()) {
            return "start game declined, game with id " + message.gameId + " is already started";
        }
        if (!game.hostPlayer.equals(message.player)) {
            return "start game declined, you are not host of game with id " + message.gameId;
        }
        if(game.getGuestPlayer() == null){
            return "start game declined, there is no guest in the game with id " + message.gameId;
        }
        return null;
    }

    private void handleSelectColor(ClientWorker clientWorker, FcSelectColorMessage message) {
        LOGGER.debug("handle FC select color: " + message);
        Game game = currentGames.findById(message.gameId);
        String reason = getDeclineReasonForColorSelect(game, message);
        if (reason != null) {
            FsDeclineSelectColorMessage response = new FsDeclineSelectColorMessage(reason);
            clientWorker.send(messageParser.toJson(response));
            return;
        }
        game.setHostColor(message.desiredColor);
        String change = "Player " + message.player.playerName + " changed his/her color to " + message.desiredColor;
        FsSubmitUpdateGameMessage response = new FsSubmitUpdateGameMessage(game, change);
        String jsonResponse = messageParser.toJson(response);
        clientWorker.send(jsonResponse);
        sendToGuest(game, jsonResponse);
    }

    private String getDeclineReasonForColorSelect(Game game, FcSelectColorMessage message) {
        if (game == null) {
            return "change color declined, no game with id " + message.gameId + " found";
        }
        if (game.isStarted()) {
            return "change color declined, game with id " + message.gameId + " is already started";
        }
        if (!game.hostPlayer.equals(message.player)) {
            return "change color declined, you are not host of game with id " + message.gameId;
        }
        return null;
    }

    private void handleCreateGame(ClientWorker clientWorker, FcCreateGameMessage message) {
        LOGGER.debug("handle FS create game: " + message);
        Game game = currentGames.createNew(message.player);
        FsSubmitCreatedGameMessage response = new FsSubmitCreatedGameMessage(game);
        clientWorker.send(messageParser.toJson(response));
    }

    private void handleLogin(ClientWorker clientWorker, FcLoginMessage loginMessage) {
        LOGGER.debug("handle FS Login: " + loginMessage);
        clientMapping.put(loginMessage.player.playerId, clientWorker);
        currentPlayers.add(loginMessage.player);
        clientWorker.send(messageParser.toJson(new FsConfirmLoginMessage()));
    }

    private void handleGetParticipatingGames(ClientWorker clientWorker, FcGetParticipatingGamesMessage getServerInfoMessage) {
        LOGGER.debug("handle FS get participating games : " + getServerInfoMessage);
        List<Game> participatingGames = currentGames.getParticipatingGames(getServerInfoMessage.player);
        FsSubmitParticipatingGamesMessage submitServerInfoMessage = new FsSubmitParticipatingGamesMessage(participatingGames);
        clientWorker.send(messageParser.toJson(submitServerInfoMessage));
    }

    private void handleUnknownMessage(ClientWorker clientWorker, String raw) {
        LOGGER.debug("handle unknown Message: " + raw);
        clientWorker.send("unknown message, sorry could not parse content: " + raw);
    }

    public void notifyDisconnect(ClientWorker clientWorker) {
        LOGGER.debug("client has disconnected: '" + clientWorker + "'");
        UUID playerId = clientMapping.getPlayerId(clientWorker);
        currentPlayers.remove(playerId);
        clientMapping.remove(clientWorker);

    }

    public void notifyUp(String s) {
        LOGGER.debug("Server is up: '" + s + "'");
    }

    public void notifyConnect(ClientWorker clientWorker) {
        LOGGER.debug("new Client connection established, we send a greeting: '" + clientWorker + "'");
        clientWorker.send(messageParser.toJson(new FsWelcomeMessage()));
    }

    public static void main(String[] args) {
        ChessServerApp chessServerApp = new ChessServerApp();
        TcpServer tcpServer = new TcpServer(8100, chessServerApp);
        tcpServer.start();
    }
}
