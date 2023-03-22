package com.github.martinfrank.games.chessserver.server.data;

import com.github.martinfrank.games.chessmodel.message.MessageParser;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessmodel.model.Player;

public class DataPool {

    public final Players currentPlayers = new Players();
    public final Games currentGames = new Games();
    public final MessageParser messageParser = new MessageParser();
    public final ClientMapping clientMapping = new ClientMapping();

    public void updatePlayersInGames(Game game) {
        Player hostOriginal = game.getHostPlayer();
        Player guestOriginal = game.getGuestPlayer();
        if(hostOriginal != null){
            Player hostUpdate = currentPlayers.get(hostOriginal.playerId);
            game.updateHostPlayer(hostUpdate);
        }
        if(guestOriginal != null){
            Player guestUpdate = currentPlayers.get(guestOriginal.playerId);
            game.updateGuestPlayer(guestUpdate);
        }
    }
}
