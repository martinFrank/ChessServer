package com.github.martinfrank.games.chessserver.server.message;

import com.github.martinfrank.games.chessmodel.message.FcLoginMessage;
import com.github.martinfrank.games.chessmodel.message.MessageParser;
import com.github.martinfrank.games.chessmodel.model.Player;
import org.junit.Test;

import java.util.UUID;

public class FcLoginMessageTest {

    private static final UUID TEST_ID = UUID.fromString("f62b659a-30ae-465b-8765-0096519dee70");
    private static final String TEST_NAME = "testName";


    @Test
    public void testLoginIn(){
        //given
        FcLoginMessage message = new FcLoginMessage(new Player(TEST_ID, TEST_NAME));
        MessageParser messageParser = new MessageParser();

        //when
        String jsonStr = messageParser.toJson(message);

        System.out.println(jsonStr);
    }

}