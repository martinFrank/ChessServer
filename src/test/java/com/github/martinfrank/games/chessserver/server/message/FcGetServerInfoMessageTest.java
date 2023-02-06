package com.github.martinfrank.games.chessserver.server.message;

import com.github.martinfrank.games.chessserver.server.message.FcGetServerInfoMessage;
import com.github.martinfrank.games.chessserver.server.message.Message;
import com.github.martinfrank.games.chessserver.server.message.MessageParser;
import com.github.martinfrank.games.chessserver.server.message.MessageType;
import com.github.martinfrank.games.chessserver.server.model.Player;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class FcGetServerInfoMessageTest {

    private static final UUID TEST_ID = UUID.fromString("f62b659a-30ae-465b-8765-0096519dee70");
    private static final String TEST_NAME = "testName";

    @Test
    public void testFcGetServerInfoMessageToJson(){
        //given
        MessageParser messageParser = new MessageParser();
        Player player = new Player(TEST_ID, TEST_NAME);
        FcGetServerInfoMessage test = new FcGetServerInfoMessage(player);

        //when
        String json = messageParser.toJson(test);
        System.out.println(json);

        //then
        Assert.assertTrue(json.contains("\"playerName\":\"testName\""));
        Assert.assertTrue(json.contains("\"playerId\":\"f62b659a-30ae-465b-8765-0096519dee70\""));
        Assert.assertTrue(json.contains("\"msgType\":\""+MessageType.FC_GET_SERVER_INFO.typeString+"\""));
    }

    @Test
    public void testJsonToFcGetServerInfoMessage(){
        //given
        MessageParser messageParser = new MessageParser();
        String json = "{\"player\":{\"playerId\":\"f62b659a-30ae-465b-8765-0096519dee70\",\"playerName\":\"testName\"},\"msgType\":\"FC_GET_SERVER_INFO\"}";

        //when
        Message message = messageParser.fromJson(json);
        FcGetServerInfoMessage fcGetServerInfoMessage = (FcGetServerInfoMessage) message;

        //then
        Assert.assertEquals(TEST_NAME, fcGetServerInfoMessage.getPlayerData().playerName);
        Assert.assertEquals(TEST_ID, fcGetServerInfoMessage.getPlayerData().playerId);
    }
}
