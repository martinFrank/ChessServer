package com.github.martinfrank.games.chessserver.server.message;

import com.github.martinfrank.games.chessmodel.message.creategame.FcCreateGameMessage;
import com.github.martinfrank.games.chessmodel.message.creategame.FsSubmitCreatedGameMessage;
import com.github.martinfrank.games.chessmodel.message.MessageParser;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessmodel.model.Player;
import org.junit.Test;

import java.util.UUID;

public class FcCreateGameMessageTest {

    private static final UUID TEST_ID = UUID.fromString("f62b659a-30ae-465b-8765-0096519dee70");
    private static final String TEST_NAME = "testName";

    private static final int BLACK = 0x0;

    @Test
    public void testFcCreateGameMessage(){
        //given
        Player player = new Player(TEST_ID, TEST_NAME, BLACK);
        FcCreateGameMessage message = new FcCreateGameMessage(player);
        MessageParser messageParser = new MessageParser();

        //when
        String jsonString = messageParser.toJson(message);
        System.out.println(jsonString);
    }

}
