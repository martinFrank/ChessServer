package com.github.martinfrank.games.chessserver;

//import com.github.martinfrank.games.chessserver.server.ChessServer;

import com.github.martinfrank.games.chessserver.server.handler.CreateGameHandler;
import com.github.martinfrank.games.chessserver.server.handler.GetParticipatingGamesHandler;
import com.github.martinfrank.games.chessserver.server.handler.LoginHandler;
import com.github.martinfrank.games.chessserver.server.handler.GetOpenGameHandler;
import com.github.martinfrank.games.chessserver.server.handler.SelectColorHandler;
import com.github.martinfrank.games.chessserver.server.handler.SelectFigureHandler;
import com.github.martinfrank.games.chessserver.server.handler.StartGameHandler;
import com.github.martinfrank.games.chessserver.server.message.*;
import com.github.martinfrank.games.chessserver.server.model.ServerAppDataPool;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import com.github.martinfrank.tcpclientserver.ServerMessageReceiver;
import com.github.martinfrank.tcpclientserver.TcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Hello world!
 */
public class ChessServerApp implements ServerMessageReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChessServerApp.class);
    private final ServerAppDataPool serverAppDataPool = new ServerAppDataPool();

    public void receive(ClientWorker clientWorker, String raw) {
        LOGGER.debug("received raw: " + raw);
        Message message = serverAppDataPool.messageParser.fromJson(raw);
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
                handleGetParticipatingGames((FcGetParticipatingGamesMessage) message);
                break;
            }
            case FC_GET_OPEN_GAMES:{
                handleGetOpenGames((FcGetOpenGamesMessage)message);
                break;
            }
            case FC_CREATE_GAME: {
                handleCreateGame((FcCreateGameMessage) message);
                break;
            }
            case FC_SELECT_COLOR: {
                handleSelectColor((FcSelectColorMessage) message);
                break;
            }
            case FC_START_GAME: {
                handleStartGame((FcStartGameMessage) message);
                break;
            }
            case FC_SELECT_FIGURE: {
                handleSelectFigure((FcSelectFigureMessage)message);
                break;
            }
            default: {
                handleUnknownMessage(clientWorker, raw);
            }
        }
    }

    private void handleSelectFigure(FcSelectFigureMessage message) {
        LOGGER.debug("handle FC select figure: " + message);
        new SelectFigureHandler(serverAppDataPool, message).handle();
    }

    private void handleGetOpenGames(FcGetOpenGamesMessage message) {
        LOGGER.debug("handle FC get open games: " + message);
        new GetOpenGameHandler(serverAppDataPool, message).handle();
    }

    private void handleStartGame(FcStartGameMessage message) {
        LOGGER.debug("handle FC start game: " + message);
        new StartGameHandler(serverAppDataPool, message).handle();

    }

    private void handleSelectColor(FcSelectColorMessage message) {
        LOGGER.debug("handle FC select color: " + message);
        new SelectColorHandler(serverAppDataPool, message).handle();
    }

    private void handleCreateGame(FcCreateGameMessage message) {
        LOGGER.debug("handle FS create game: " + message);
        new CreateGameHandler(serverAppDataPool, message).handle();
    }

    private void handleLogin(ClientWorker clientWorker, FcLoginMessage loginMessage) {
        LOGGER.debug("handle FS Login: " + loginMessage);
        new LoginHandler(serverAppDataPool, loginMessage, clientWorker).handle();
    }

    private void handleGetParticipatingGames(FcGetParticipatingGamesMessage getParticipatingGamesMessage) {
        LOGGER.debug("handle FS get participating games : " + getParticipatingGamesMessage);
        new GetParticipatingGamesHandler(serverAppDataPool, getParticipatingGamesMessage).handle();
    }

    private void handleUnknownMessage(ClientWorker clientWorker, String raw) {
        LOGGER.debug("handle unknown Message: " + raw);
        clientWorker.send("unknown message, sorry could not parse content: " + raw);
    }

    public void notifyDisconnect(ClientWorker clientWorker) {
        LOGGER.debug("client has disconnected: '" + clientWorker + "'");
        UUID playerId = serverAppDataPool.clientMapping.getPlayerId(clientWorker);
        serverAppDataPool.currentPlayers.remove(playerId);
        serverAppDataPool.clientMapping.remove(clientWorker);
    }

    public void notifyUp(String s) {
        LOGGER.debug("Server is up: '" + s + "'");
    }

    public void notifyConnect(ClientWorker clientWorker) {
        LOGGER.debug("new Client connection established, we send a greeting: '" + clientWorker + "'");
        clientWorker.send(serverAppDataPool.messageParser.toJson(new FsWelcomeMessage()));
    }

    public static void main(String[] args) {
        ChessServerApp chessServerApp = new ChessServerApp();
        TcpServer tcpServer = new TcpServer(8100, chessServerApp);
        tcpServer.start();
    }
}
