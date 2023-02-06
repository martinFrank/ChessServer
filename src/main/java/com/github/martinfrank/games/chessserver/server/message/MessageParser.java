package com.github.martinfrank.games.chessserver.server.message;

import com.google.gson.Gson;

public class MessageParser {

    private final Gson gson = new Gson();

    public Message fromJson(String jsonStr) {
        Message message;
        try {
            message = gson.fromJson(jsonStr, Message.class);
        }catch (Exception e){
            message = new Message(MessageType.UNKNOWN);
        }
        switch (message.msgType){
            case FC_GET_SERVER_INFO: return createFcGetServerInfo(jsonStr);
        }
        return message;
    }

    private FcGetServerInfoMessage createFcGetServerInfo(String jsonStr) {
        return gson.fromJson(jsonStr, FcGetServerInfoMessage.class);
    }

    public String toJson(Message message) {
        return gson.toJson(message);
    }
}
