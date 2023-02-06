package com.github.martinfrank.games.chessserver.server.message;

public class FsConfirmLoginMessage extends Message{

    public final String loginConfirmationMessage = "login confirmed";

    public FsConfirmLoginMessage() {
        super(MessageType.FS_CONFIRM_LOGIN);
    }

    @Override
    public String toString() {
        return "FsConfirmLoginMessage{" +
                "loginConfirmationMessage='" + loginConfirmationMessage + '\'' +
                ", msgType=" + msgType +
                ", version=" + version +
                '}';
    }
}
