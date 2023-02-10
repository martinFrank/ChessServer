package com.github.martinfrank.games.chessserver.server.data;

import com.github.martinfrank.games.chessmodel.message.MessageParser;

public class ServerAppDataPool {

    public final Games currentGames = new Games();
    public final Players currentPlayers = new Players();
    public final MessageParser messageParser = new MessageParser();
    public final ClientMapping clientMapping = new ClientMapping();

}
