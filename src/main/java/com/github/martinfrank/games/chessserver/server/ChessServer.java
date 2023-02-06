package com.github.martinfrank.games.chessserver.server;

import com.github.martinfrank.games.chessserver.server.message.*;
import com.github.martinfrank.games.chessserver.server.model.Game;
import com.github.martinfrank.games.chessserver.server.model.Games;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import com.github.martinfrank.tcpclientserver.ServerMessageReceiver;
import com.github.martinfrank.tcpclientserver.TcpServer;

import java.util.List;

public class ChessServer implements ServerMessageReceiver {

    private final TcpServer tcpServer;
    private final Games currentGames = new Games();
    private final MessageParser messageParser = new MessageParser();

    public ChessServer(){
        tcpServer = new TcpServer(8100, this);
    }

    //visibleForTesting
    ChessServer(TcpServer tcpServer){
        this.tcpServer = tcpServer;
    }
    public void start() {
        tcpServer.start();
    }

    public void receive(ClientWorker clientWorker, String raw) {
        clientWorker.send("echo: "+raw);
        Message message = messageParser.fromJson(raw);
        switch (message.msgType){
            case UNKNOWN: handleUnknownMessage(clientWorker, raw);
            case FC_GET_SERVER_INFO: handleGetServerInfo(clientWorker, (FcGetServerInfoMessage)message);
            //case FC_CREATE_NEW_GAME:
        }
    }

    private void handleGetServerInfo(ClientWorker clientWorker, FcGetServerInfoMessage getServerInfoMessage) {
        List<Game> runningGames = currentGames.getJoinedGames(getServerInfoMessage.getPlayerData());
        FsSubmitServerInfoMessage submitServerInfoMessage = new FsSubmitServerInfoMessage(runningGames);
        clientWorker.send(messageParser.toJson(submitServerInfoMessage));
    }

    private void handleUnknownMessage(ClientWorker clientWorker, String raw) {
        clientWorker.send("unknown message, sorry could not parse content: "+raw);
    }

    public void notifyDisconnect(ClientWorker clientWorker) {

    }

    public void notifyUp(String s) {

    }

    public void notifyConnect(ClientWorker clientWorker) {
        clientWorker.send(messageParser.toJson(new Message(MessageType.UNKNOWN)));
    }
}
