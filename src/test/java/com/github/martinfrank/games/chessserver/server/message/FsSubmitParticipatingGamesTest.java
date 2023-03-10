package com.github.martinfrank.games.chessserver.server.message;

import com.github.martinfrank.games.chessmodel.message.getparticipatinggames.FsSubmitParticipatingGamesMessage;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessmodel.model.Player;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FsSubmitParticipatingGamesTest {

    private static final UUID TEST_ID_A = UUID.fromString("f62b659a-30ae-465b-8765-0096519dee70");
    private static final UUID TEST_ID_B = UUID.fromString("f62b659a-30ae-465b-8765-0096519dee71");

    private static final String TEST_NAME_A = "a-name";
    private static final String TEST_NAME_B = "b-name";
    private static final int BLACK = 0x0;

    @Test
    public void testFsSubmitServerInfoMessageToJson(){
        Player aPlayer = new Player(TEST_ID_A, TEST_NAME_A, BLACK);
        Player bPlayer = new Player(TEST_ID_B, TEST_NAME_B, BLACK);
        Game aGame = new Game(UUID.randomUUID(), aPlayer); //offenes
        Game bGame = new Game(UUID.randomUUID(), aPlayer); //mit teilnehmer b
        bGame.setGuestPlayer(bPlayer);
        Game cGame = new Game(UUID.randomUUID(), bPlayer); //offenes
        Game dGame = new Game(UUID.randomUUID(), bPlayer); //mit Teilnehmer a
        dGame.setGuestPlayer(aPlayer);
        List<Game> games = Arrays.asList(aGame, bGame, cGame);

        FsSubmitParticipatingGamesMessage fsSubmitParticipatingGamesMessage = new FsSubmitParticipatingGamesMessage(games);
    }
}