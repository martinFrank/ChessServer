package com.github.martinfrank.games.chessserver.server.message;

import com.github.martinfrank.games.chessserver.server.model.Game;
import com.github.martinfrank.games.chessserver.server.model.Player;
import org.junit.Test;

import java.util.UUID;

public class FcCreateGameMessageTest {

    private static final UUID TEST_ID = UUID.fromString("f62b659a-30ae-465b-8765-0096519dee70");
    private static final String TEST_NAME = "testName";

    @Test
    public void testFcCreateGameMessage(){
        //given
        Player player = new Player(TEST_ID, TEST_NAME);
        FcCreateGameMessage message = new FcCreateGameMessage(player);
        MessageParser messageParser = new MessageParser();

        //when
        String jsonString = messageParser.toJson(message);
        System.out.println(jsonString);
    }

    @Test
    public void testFsSubmitCreatedGame(){
        //given
        Player player = new Player(TEST_ID, TEST_NAME);
        Game game = new Game(player);
        FsSubmitCreatedGameMessage message = new FsSubmitCreatedGameMessage(game);
        MessageParser messageParser = new MessageParser();

        //when
        game.setHostOnline(true);
        String jsonString = messageParser.toJson(message);
        System.out.println(jsonString);
    }
}
