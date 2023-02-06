package com.github.martinfrank.games.chessserver;

//import com.github.martinfrank.games.chessserver.server.ChessServer;

import com.github.martinfrank.games.chessserver.server.message.FcCreateGameMessage;
import com.github.martinfrank.games.chessserver.server.message.FcGetParticipatingGamesMessage;
import com.github.martinfrank.games.chessserver.server.message.FcLoginMessage;
import com.github.martinfrank.games.chessserver.server.message.FsConfirmLoginMessage;
import com.github.martinfrank.games.chessserver.server.message.FsSubmitCreatedGameMessage;
import com.github.martinfrank.games.chessserver.server.message.FsSubmitParticipatingGamesMessage;
import com.github.martinfrank.games.chessserver.server.message.FsWelcomeMessage;
import com.github.martinfrank.games.chessserver.server.message.Message;
import com.github.martinfrank.games.chessserver.server.message.MessageParser;
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
                handleGetParticiatingGames(clientWorker, (FcGetParticipatingGamesMessage) message);
                break;
            }
            case FC_CREATE_GAME: {
                handleCreateGame(clientWorker, (FcCreateGameMessage)message);
                break;
            }
            default: {
                handleUnknownMessage(clientWorker, raw);
            }

        }
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

    private void handleGetParticiatingGames(ClientWorker clientWorker, FcGetParticipatingGamesMessage getServerInfoMessage) {
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
