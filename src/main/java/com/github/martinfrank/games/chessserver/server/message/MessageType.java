package com.github.martinfrank.games.chessserver.server.message;

public enum MessageType {

    UNKNOWN ("UNKNOWN"),
    FC_GET_SERVER_INFO("FC_GET_SERVER_INFO"),
    FS_SUBMIT_SERVER_INFO("FS_SUBMIT_SERVER_INFO"),
    FC_CREATE_GAME("FC_CREATE_GAME"),
    FS_SUBMIT_GAME_INFO("FS_SUBMIT_GAME_INFO");

    public final String typeString;

    MessageType(String typeString){
        this.typeString = typeString;
    }
}
