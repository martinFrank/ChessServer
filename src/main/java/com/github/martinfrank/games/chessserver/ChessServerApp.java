package com.github.martinfrank.games.chessserver;

import com.github.martinfrank.games.chessmodel.message.FcCreateGameMessage;
import com.github.martinfrank.games.chessmodel.message.FcGetGameContentMessage;
import com.github.martinfrank.games.chessmodel.message.FcGetOpenGamesMessage;
import com.github.martinfrank.games.chessmodel.message.FcGetParticipatingGamesMessage;
import com.github.martinfrank.games.chessmodel.message.FcJoinGameMessage;
import com.github.martinfrank.games.chessmodel.message.FcLoginMessage;
import com.github.martinfrank.games.chessmodel.message.FcSelectColorMessage;
import com.github.martinfrank.games.chessmodel.message.FcSelectFigureMessage;
import com.github.martinfrank.games.chessmodel.message.FcStartGameMessage;
import com.github.martinfrank.games.chessmodel.message.FsWelcomeMessage;
import com.github.martinfrank.games.chessmodel.message.Message;
import com.github.martinfrank.games.chessserver.server.data.ServerAppDataPool;
import com.github.martinfrank.games.chessserver.server.handler.CreateGameHandler;
import com.github.martinfrank.games.chessserver.server.handler.GameContentHandler;
import com.github.martinfrank.games.chessserver.server.handler.GetOpenGameHandler;
import com.github.martinfrank.games.chessserver.server.handler.GetParticipatingGamesHandler;
import com.github.martinfrank.games.chessserver.server.handler.JoinGameHandler;
import com.github.martinfrank.games.chessserver.server.handler.LoginHandler;
import com.github.martinfrank.games.chessserver.server.handler.SelectColorHandler;
import com.github.martinfrank.games.chessserver.server.handler.SelectFigureHandler;
import com.github.martinfrank.games.chessserver.server.handler.StartGameHandler;
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
//            case UNKNOWN: {
//                handleUnknownMessage(clientWorker, raw);
//                break;
//            }
            case FC_LOGIN: {
                LOGGER.debug("handle FS Login: " + message);
                new LoginHandler(serverAppDataPool).handle(clientWorker, (FcLoginMessage) message);
                break;
            }
            case FC_GET_PARTICIPATING_GAMES: {
                LOGGER.debug("handle FS get participating games : " + message);
                new GetParticipatingGamesHandler(serverAppDataPool).handle(clientWorker, (FcGetParticipatingGamesMessage) message);
                break;
            }
            case FC_GET_OPEN_GAMES:{
                LOGGER.debug("handle FC select figure: " + message);
                new GetOpenGameHandler(serverAppDataPool).handle(clientWorker, (FcGetOpenGamesMessage) message);
                break;
            }
            case FC_CREATE_GAME: {
                LOGGER.debug("handle FS create game: " + message);
                new CreateGameHandler(serverAppDataPool).handle(clientWorker, (FcCreateGameMessage) message);
                break;
            }
            case FC_SELECT_COLOR: {
                LOGGER.debug("handle FC select color: " + message);
                new SelectColorHandler(serverAppDataPool).handle(clientWorker, (FcSelectColorMessage) message);
                break;
            }
            case FC_START_GAME: {
                LOGGER.debug("handle FC start game: " + message);
                new StartGameHandler(serverAppDataPool).handle(clientWorker, (FcStartGameMessage) message);
                break;
            }
            case FC_SELECT_FIGURE: {
                LOGGER.debug("handle FC select figure: " + message);
                new SelectFigureHandler(serverAppDataPool).handle(clientWorker, (FcSelectFigureMessage)message);
                break;
            }
            case FC_JOIN_GAME:{
                LOGGER.debug("handle FC join game: " + message);
                new JoinGameHandler(serverAppDataPool).handle(clientWorker, (FcJoinGameMessage)message);
                break;
            }
            case FC_GET_GAME_CONTENT:{
                LOGGER.debug("handle FC get game content: " + message);
                new GameContentHandler(serverAppDataPool).handle(clientWorker, (FcGetGameContentMessage)message);
                break;
            }

            default: {
                handleUnknownMessage(clientWorker, raw);
            }
        }
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
