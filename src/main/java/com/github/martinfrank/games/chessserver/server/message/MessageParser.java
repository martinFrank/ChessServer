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
                case FC_GET_SERVER_INFO: return createFcGetServerInfoMessage(jsonStr);
                case FC_LOGIN: return createFcLoginMessage(jsonStr);

            }
        }catch (JsonParseException e){
            message = new Message(MessageType.UNKNOWN);
            LOGGER.debug("Exception: "+e);
        }
        return message;
    }

    private FcLoginMessage createFcLoginMessage(String jsonStr) {
        return gson.fromJson(jsonStr, FcLoginMessage.class);
    }

    private FcGetServerInfoMessage createFcGetServerInfoMessage(String jsonStr) {
        return gson.fromJson(jsonStr, FcGetServerInfoMessage.class);
    }

    public String toJson(Message message) {
        try{
            return gson.toJson(message);
        }catch (JsonParseException e){
            return gson.toJson(message);
        }
    }
}
