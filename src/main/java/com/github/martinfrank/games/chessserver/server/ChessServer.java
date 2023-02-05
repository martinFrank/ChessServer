package com.github.martinfrank.games.chessserver.server;

import com.github.martinfrank.tcpclientserver.ClientWorker;
import com.github.martinfrank.tcpclientserver.ServerMessageReceiver;
import com.github.martinfrank.tcpclientserver.TcpServer;

public class ChessServer implements ServerMessageReceiver {

    private final TcpServer tcpServer;

    public ChessServer(){
        tcpServer = new TcpServer(8100, this);
    }
    public void start() {
        tcpServer.start();
    }

    public void receive(ClientWorker clientWorker, String s) {
        clientWorker.send("echo: "+s);
    }

    public void notifyDisconnect(ClientWorker clientWorker) {

    }

    public void notifyUp(String s) {

    }

    public void notifyConnect(ClientWorker clientWorker) {

    }
}
