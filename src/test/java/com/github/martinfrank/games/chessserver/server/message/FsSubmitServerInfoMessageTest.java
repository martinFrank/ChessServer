package com.github.martinfrank.games.chessserver.server.message;

import com.github.martinfrank.games.chessserver.server.model.Game;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FsSubmitServerInfoMessageTest {

    private static final UUID TEST_ID = UUID.fromString("f62b659a-30ae-465b-8765-0096519dee70");
    private static final String TEST_NAME = "testName";

    public void testFsSubmitServerInfoMessageToJson(){
        Game aGame = new Game( TEST_ID );
        Game bGame = new Game();
        Game cGame = new Game();
        List<Game> games = Arrays.asList(aGame, bGame, cGame);

        FsSubmitServerInfoMessage fsSubmitServerInfoMessage = new FsSubmitServerInfoMessage(games);
    }
}