package com.github.martinfrank.games.chessserver.server.message;

public enum MessageType {

    UNKNOWN ("UNKNOWN"),
    FS_WELCOME("FS_WELCOME"),
    FC_GET_PARTICIPATING_GAMES("FC_GET_PARTICIPATING_GAMES"),
    FS_SUBMIT_PARTICIPATING_GAMES("FS_SUBMIT_PARTICIPATING_GAMES"),
    FC_CREATE_GAME("FC_CREATE_GAME"),
    FS_SUBMIT_CREATED_GAME("FS_SUBMIT_CREATED_GAME"),

    FC_LOGIN("FC_LOGIN"),
    FS_CONFIRM_LOGIN("FS_CONFIRM_LOGIN");

    public final String typeString;

    MessageType(String typeString){
        this.typeString = typeString;
    }
}
