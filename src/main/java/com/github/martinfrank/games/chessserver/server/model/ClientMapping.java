package com.github.martinfrank.games.chessserver.server.model;

import com.github.martinfrank.tcpclientserver.ClientWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientMapping {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMapping.class);

    private final Map<UUID, ClientWorker> clientMapping = new HashMap<>();

    public void put(UUID playerId, ClientWorker clientWorker) {
        clientMapping.put(playerId, clientWorker);
        LOGGER.debug("clientMapping.size(): " + clientMapping.size());
    }

    public void remove(ClientWorker clientWorker) {
        for(Map.Entry<UUID, ClientWorker> entry: clientMapping.entrySet()){
            if (entry.getValue().getId() == clientWorker.getId() ){
                clientMapping.remove(entry.getKey());
                break;
            }
        }
        LOGGER.debug("clientMapping.size(): " + clientMapping.size());
    }

    public UUID getPlayerId(ClientWorker clientWorker) {
        for(Map.Entry<UUID, ClientWorker> entry: clientMapping.entrySet()){
            if (entry.getValue().getId() == clientWorker.getId() ){
                return entry.getKey();
            }
        }
        return null;
    }

    public ClientWorker getClientWorker(Player player) {
        return clientMapping.get(player.playerId);
    }
}
