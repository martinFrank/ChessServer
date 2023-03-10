package com.github.martinfrank.games.chessserver;

import com.github.martinfrank.games.chessmodel.message.creategame.FcCreateGameMessage;
import com.github.martinfrank.games.chessmodel.message.getgamecontent.FcGetGameContentMessage;
import com.github.martinfrank.games.chessmodel.message.disconnect.FsSubmitDisconnectMessage;
import com.github.martinfrank.games.chessmodel.message.getopengames.FcGetOpenGamesMessage;
import com.github.martinfrank.games.chessmodel.message.getparticipatinggames.FcGetParticipatingGamesMessage;
import com.github.martinfrank.games.chessmodel.message.joingame.FcJoinGameMessage;
import com.github.martinfrank.games.chessmodel.message.login.FcLoginMessage;
import com.github.martinfrank.games.chessmodel.message.selectColor.FcSelectColorMessage;
import com.github.martinfrank.games.chessmodel.message.selectfield.FcSelectFieldMessage;
import com.github.martinfrank.games.chessmodel.message.startgame.FcStartGameMessage;
import com.github.martinfrank.games.chessmodel.message.welcome.FsWelcomeMessage;
import com.github.martinfrank.games.chessmodel.message.Message;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessmodel.model.Player;
import com.github.martinfrank.games.chessserver.server.data.DataPool;
import com.github.martinfrank.games.chessserver.server.handler.CreateGameHandler;
import com.github.martinfrank.games.chessserver.server.handler.GameContentHandler;
import com.github.martinfrank.games.chessserver.server.handler.GetOpenGameHandler;
import com.github.martinfrank.games.chessserver.server.handler.GetParticipatingGamesHandler;
import com.github.martinfrank.games.chessserver.server.handler.JoinGameHandler;
import com.github.martinfrank.games.chessserver.server.handler.LoginHandler;
import com.github.martinfrank.games.chessserver.server.handler.SelectColorHandler;
import com.github.martinfrank.games.chessserver.server.handler.SelectFieldHandler;
import com.github.martinfrank.games.chessserver.server.handler.StartGameHandler;
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
public class ChessServer implements ServerMessageReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChessServer.class);
    private final DataPool dataPool = new DataPool();

    public void receive(ClientWorker clientWorker, String raw) {
        LOGGER.debug("received raw: " + raw);
        Message message = dataPool.messageParser.fromJson(raw);
        debugData();
        //test
        switch (message.msgType) {
//            case UNKNOWN: {
//                handleUnknownMessage(clientWorker, raw);
//                break;
//            }
            case FC_LOGIN: {
                LOGGER.debug("handle FS Login: " + message);
                new LoginHandler(dataPool).handle(clientWorker, (FcLoginMessage) message);
                break;
            }
            case FC_GET_PARTICIPATING_GAMES: {
                LOGGER.debug("handle FS get participating games : " + message);
                new GetParticipatingGamesHandler(dataPool).handle(clientWorker, (FcGetParticipatingGamesMessage) message);
                break;
            }
            case FC_GET_OPEN_GAMES: {
                LOGGER.debug("handle FC select figure: " + message);
                new GetOpenGameHandler(dataPool).handle(clientWorker, (FcGetOpenGamesMessage) message);
                break;
            }
            case FC_CREATE_GAME: {
                LOGGER.debug("handle FS create game: " + message);
                new CreateGameHandler(dataPool).handle(clientWorker, (FcCreateGameMessage) message);
                break;
            }
            case FC_SELECT_COLOR: {
                LOGGER.debug("handle FC select color: " + message);
                new SelectColorHandler(dataPool).handle(clientWorker, (FcSelectColorMessage) message);
                break;
            }
            case FC_START_GAME: {
                LOGGER.debug("handle FC start game: " + message);
                new StartGameHandler(dataPool).handle(clientWorker, (FcStartGameMessage) message);
                break;
            }
            case FC_SELECT_FIELD: {
                LOGGER.debug("handle FC select field: " + message);
                new SelectFieldHandler(dataPool).handle(clientWorker, (FcSelectFieldMessage) message);
                break;
            }
            case FC_JOIN_GAME: {
                LOGGER.debug("handle FC join game: " + message);
                new JoinGameHandler(dataPool).handle(clientWorker, (FcJoinGameMessage) message);
                break;
            }
            case FC_GET_GAME_CONTENT: {
                LOGGER.debug("handle FC get game content: " + message);
                new GameContentHandler(dataPool).handle(clientWorker, (FcGetGameContentMessage) message);
                break;
            }

            default: {
                handleUnknownMessage(clientWorker, raw);
            }
        }
    }

    private void debugData() {
        LOGGER.debug("games.size: " + dataPool.currentGames.toString());
    }

    private void handleUnknownMessage(ClientWorker clientWorker, String raw) {
        LOGGER.debug("handle unknown Message: " + raw);
        clientWorker.send("unknown message, sorry could not parse content: " + raw);
    }

    public void notifyDisconnect(ClientWorker clientWorker) {
        LOGGER.debug("client has disconnected: '" + clientWorker + "'");
        UUID playerId = dataPool.clientMapping.getPlayerId(clientWorker);
        Player player = dataPool.currentPlayers.get(playerId);
        sendGoodbyeToOpenGames(player);
        dataPool.currentPlayers.remove(playerId);
        dataPool.clientMapping.remove(clientWorker);
    }

    private void sendGoodbyeToOpenGames(Player player) {
        List<Game> games = dataPool.currentGames.getParticipatingGames(player);
        for (Game game : games) {
            LOGGER.debug("sending goodbye");
            ClientWorker worker = dataPool.clientMapping.getClientWorker(game.getOther(player));
            if (worker != null) {
                FsSubmitDisconnectMessage message = new FsSubmitDisconnectMessage(player, game);
                LOGGER.debug("message = "+message);
                String json = dataPool.messageParser.toJson(message);
                worker.send(json);
            }
        }
    }


    public void notifyUp(String s) {
        LOGGER.debug("Server is up: '" + s + "'");
    }

    public void notifyConnect(ClientWorker clientWorker) {
        LOGGER.debug("new Client connection established, we send a greeting: '" + clientWorker + "'");
        clientWorker.send(dataPool.messageParser.toJson(new FsWelcomeMessage()));
    }

    public static void main(String[] args) {
        ChessServer chessServerApp = new ChessServer();
        TcpServer tcpServer = new TcpServer(8100, chessServerApp);
        tcpServer.start();
    }
}
