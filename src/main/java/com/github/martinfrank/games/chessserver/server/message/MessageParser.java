package com.github.martinfrank.games.chessserver.server.message;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MessageParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageParser.class);
    private final Gson gson = new Gson();

    public Message fromJson(String jsonStr) {
        Message message;
        try {
            message = gson.fromJson(jsonStr, Message.class);
            switch (message.msgType){
                case FC_GET_PARTICIPATING_GAMES: return createFcGetServerInfoMessage(jsonStr);
                case FC_LOGIN: return createFcLoginMessage(jsonStr);
                case FC_CREATE_GAME: return createFcCreateGameMessage(jsonStr);
                case FC_SELECT_COLOR: return createFcSelectColorMessage(jsonStr);
                case FC_START_GAME: return createFcStartGameMessage(jsonStr);

            }
        }catch (JsonParseException e){
            message = new Message(MessageType.UNKNOWN);
            LOGGER.debug("Exception: "+e);
        }
        return message;
    }

    private FcStartGameMessage createFcStartGameMessage(String jsonStr) {
        return gson.fromJson(jsonStr, FcStartGameMessage.class);
    }

    private FcSelectColorMessage createFcSelectColorMessage(String jsonStr) {
        return gson.fromJson(jsonStr, FcSelectColorMessage.class);
    }

    private FcCreateGameMessage createFcCreateGameMessage(String jsonStr) {
        return gson.fromJson(jsonStr, FcCreateGameMessage.class);
    }

    private FcLoginMessage createFcLoginMessage(String jsonStr) {
        return gson.fromJson(jsonStr, FcLoginMessage.class);
    }

    private FcGetParticipatingGamesMessage createFcGetServerInfoMessage(String jsonStr) {
        return gson.fromJson(jsonStr, FcGetParticipatingGamesMessage.class);
    }

    public String toJson(Message message) {
        try{
            return gson.toJson(message);
        }catch (JsonParseException e){
            return gson.toJson(message);
        }
    }
}
