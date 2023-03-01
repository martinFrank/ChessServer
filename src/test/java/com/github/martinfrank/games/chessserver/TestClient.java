package com.github.martinfrank.games.chessserver;

import com.github.martinfrank.games.chessmodel.message.FcCreateGameMessage;
import com.github.martinfrank.games.chessmodel.message.FcGetOpenGamesMessage;
import com.github.martinfrank.games.chessmodel.message.FcJoinGameMessage;
import com.github.martinfrank.games.chessmodel.message.FcLoginMessage;
import com.github.martinfrank.games.chessmodel.message.FcStartGameMessage;
import com.github.martinfrank.games.chessmodel.message.Message;
import com.github.martinfrank.games.chessmodel.message.MessageParser;
import com.github.martinfrank.games.chessmodel.message.MessageType;
import com.github.martinfrank.games.chessmodel.model.Game;
import com.github.martinfrank.games.chessmodel.model.Player;
import com.github.martinfrank.tcpclientserver.ClientMessageReceiver;
import com.github.martinfrank.tcpclientserver.TcpClient;

import java.util.Scanner;
import java.util.UUID;

public class TestClient {

    private final Player player = new Player(UUID.randomUUID(), "testeeeee");
    private Game currentGame;

    public static void main(String[] args){
        new TestClient().start();

    }

    private void start() {
        ClientMessageReceiver serverMessageReceiver = new ClientMessageReceiver() {
            @Override
            public void receive(String message) {
                System.out.println("received: "+message);
            }

            @Override
            public void notifyDisconnect() {
                System.out.println("disconnect");
            }
        };

//        TcpClient client = new TcpClient("192.168.0.65", 8100, serverMessageReceiver);
        TcpClient client = new TcpClient("elitegames.chickenkiller.com", 8100, serverMessageReceiver);

        client.start();

        Scanner scanner = new Scanner(System.in);
        CommandParser commandParser = new CommandParser();
        MessageParser messageParser = new MessageParser();
        String input = "test";
        while(!"EXIT".equalsIgnoreCase(input)){
            input = scanner.nextLine();
            Message message = commandParser.parseCommand(input);
            if(message != null){
                String json = messageParser.toJson(message);
                client.send(json);
            }
        }
        client.close();
    }

    private class CommandParser {

        public Message parseCommand(String message){
            if(message == null ||message.length() == 0){
                System.out.println("empty command - return null...");
                return null;
            }
            String[] splits = message.split(" ");

            MessageType messageType;
            try{
                messageType = MessageType.valueOf(splits[0]);
            }catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException e){
                System.out.println("unknown Message type - return null...");
                return null;
            }
            String [] args = new String[splits.length-1];
            System.arraycopy(splits, 1, args, 0, args.length);

            return createCommand(messageType, args);

        }

        private Message createCommand(MessageType messageType, String[] args) {
            switch (messageType){
                case FC_LOGIN: return new FcLoginMessage(player);
                case FC_CREATE_GAME: return new FcCreateGameMessage(player);
                case FC_GET_OPEN_GAMES: return new FcGetOpenGamesMessage(player);
                case FC_JOIN_GAME: return new FcJoinGameMessage(player, UUID.fromString(args[0]));
                case FC_START_GAME: return new FcStartGameMessage(player, UUID.fromString(args[0]));

                default: return null;
            }
        }
    }

}
