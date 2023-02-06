package com.github.martinfrank.games.chessserver.server.message;

public class Message {
    public final MessageType msgType;
    public int version = 1;

    public Message (MessageType msgType ){
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msgType=" + msgType +
                ", version=" + version +
                '}';
    }
}
