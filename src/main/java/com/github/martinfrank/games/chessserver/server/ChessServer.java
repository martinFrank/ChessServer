package com.github.martinfrank.games.chessserver.server;

import com.github.martinfrank.games.chessserver.server.message.Message;
import com.github.martinfrank.games.chessserver.server.message.MessageParser;
import com.github.martinfrank.games.chessserver.server.model.Games;
import com.github.martinfrank.tcpclientserver.ClientWorker;
import com.github.martinfrank.tcpclientserver.ServerMessageReceiver;
import com.github.martinfrank.tcpclientserver.TcpServer;

public class ChessServer implements ServerMessageReceiver {

    private final TcpServer tcpServer;
    private final Games currentGames = new Games();
    private final MessageParser messageParser = new MessageParser();

    public ChessServer(){
        tcpServer = new TcpServer(8100, this);
    }
    public void start() {
        tcpServer.start();
    }

    public void receive(ClientWorker clientWorker, String s) {
        clientWorker.send("echo: "+s);
        Message message = messageParser.fromJson(s);
    }

    public void notifyDisconnect(ClientWorker clientWorker) {

    }

    public void notifyUp(String s) {

    }

    public void notifyConnect(ClientWorker clientWorker) {
        clientWorker.send(messageParser.toJson(new Message()));
    }
}
