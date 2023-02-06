package com.github.martinfrank.games.chessserver.server.message;

public class FsWelcomeMessage extends Message{

    public final String welcome = "welcome to the chess server, connection established, please submit your login";

    public FsWelcomeMessage() {
        super(MessageType.FS_WELCOME);
    }

    @Override
    public String toString() {
        return "FsWelcomeMessage{" +
                "welcome='" + welcome + '\'' +
                ", msgType=" + msgType +
                ", version=" + version +
                '}';
    }
}
