package com.github.martinfrank.games.chessserver.server.message;

import com.github.martinfrank.games.chessserver.server.model.Player;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class FcGetParticipatingGamesMessageTest {

    private static final UUID TEST_ID = UUID.fromString("f62b659a-30ae-465b-8765-0096519dee70");
    private static final String TEST_NAME = "testName";

    @Test
    public void testFcGetServerInfoMessageToJson(){
        //given
        MessageParser messageParser = new MessageParser();
        Player player = new Player(TEST_ID, TEST_NAME);
        FcGetParticipatingGamesMessage test = new FcGetParticipatingGamesMessage(player);

        //when
        String json = messageParser.toJson(test);
        System.out.println(json);

        //then
        Assert.assertTrue(json.contains("\"playerName\":\"testName\""));
        Assert.assertTrue(json.contains("\"playerId\":\"f62b659a-30ae-465b-8765-0096519dee70\""));
        Assert.assertTrue(json.contains("\"msgType\":\""+MessageType.FC_GET_PARTICIPATING_GAMES.typeString+"\""));
    }

    @Test
    public void testJsonToFcGetServerInfoMessage(){
        //given
        MessageParser messageParser = new MessageParser();
        String json = "{\"player\":{\"playerId\":\"f62b659a-30ae-465b-8765-0096519dee70\",\"playerName\":\"testName\"},\"msgType\":\"FC_GET_PARTICIPATING_GAMES\",\"version\":2}";

        //when
        Message message = messageParser.fromJson(json);
        FcGetParticipatingGamesMessage fcGetParticipatingGamesMessage = (FcGetParticipatingGamesMessage) message;

        //then
        Assert.assertEquals(TEST_NAME, fcGetParticipatingGamesMessage.player.playerName);
        Assert.assertEquals(TEST_ID, fcGetParticipatingGamesMessage.player.playerId);
        Assert.assertEquals(2, fcGetParticipatingGamesMessage.version);
    }
}
