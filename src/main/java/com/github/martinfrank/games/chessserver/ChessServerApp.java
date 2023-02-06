package com.github.martinfrank.games.chessserver;

//import com.github.martinfrank.games.chessserver.server.ChessServer;

import com.github.martinfrank.games.chessserver.server.message.FcGetServerInfoMessage;
import com.github.martinfrank.games.chessserver.server.message.FcLoginMessage;
import com.github.martinfrank.games.chessserver.server.message.FsConfirmLoginMessage;
import com.github.martinfrank.games.chessserver.server.message.FsSubmitServerInfoMessage;
import com.github.martinfrank.games.chessserver.server.message.FsWelcomeMessage;
import com.github.martinfrank.games.chessserver.server.message.Message;
import com.github.martinfrank.games.chessserver.server.message.MessageParser;
import com.github.martinfrank.games.chessserver.server.model.Game;
import com.github.martinfrank.games.chessserver.server.model.Games;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import com.github.martinfrank.tcpclientserver.ServerMessageReceiver;
import com.github.martinfrank.tcpclientserver.TcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Hello world!
 */
public class ChessServerApp implements ServerMessageReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChessServerApp.class);
    private final Games currentGames = new Games();
    private final MessageParser messageParser = new MessageParser();
    private final Map<UUID, ClientWorker> clientMapping = new HashMap<>();

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
            case FC_GET_SERVER_INFO: {
                handleGetServerInfo(clientWorker, (FcGetServerInfoMessage) message);
                break;
            }
            default: {
                handleUnknownMessage(clientWorker, raw);
            }

        }
    }

    private void handleLogin(ClientWorker clientWorker, FcLoginMessage loginMessage) {
        LOGGER.debug("handle FS Login: " + loginMessage);
        clientMapping.put(loginMessage.getPlayer().playerId, clientWorker);
        clientWorker.send(messageParser.toJson(new FsConfirmLoginMessage()));
    }

    private void handleGetServerInfo(ClientWorker clientWorker, FcGetServerInfoMessage getServerInfoMessage) {
        LOGGER.debug("handle FS get Server Info: " + getServerInfoMessage);
        List<Game> runningGames = currentGames.getJoinedGames(getServerInfoMessage.getPlayer());
        FsSubmitServerInfoMessage submitServerInfoMessage = new FsSubmitServerInfoMessage(runningGames);
        clientWorker.send(messageParser.toJson(submitServerInfoMessage));
    }

    private void handleUnknownMessage(ClientWorker clientWorker, String raw) {
        LOGGER.debug("handle unknown Message: " + raw);
        clientWorker.send("unknown message, sorry could not parse content: " + raw);
    }

    public void notifyDisconnect(ClientWorker clientWorker) {
        LOGGER.debug("client has disconnected: '" + clientWorker + "'");
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
