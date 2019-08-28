package com.exiro.fortgame.communication;

import com.exiro.fortgame.fortGame;
import com.exiro.fortgame.renderer.screens.BattleGame;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MatchMakingConnection implements Runnable {

    final String host = "86.248.146.40";  //"86.248.146.40";//"192.168.1.21";
    final int portNumber = 6260;

    public void addToMatchMaking() {
        try {

            Socket socket = new Socket(host, portNumber);
            fortGame.getInstance().player.setSocket(socket);
            OutputStream os = socket.getOutputStream();
            DataInputStream is = new DataInputStream(socket.getInputStream());
            String message = fortGame.USERUID + ";match";

            ClientConnection.write(message, os);

            BattleGame b = new BattleGame(fortGame.getInstance().player, fortGame.getInstance());


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        addToMatchMaking();
    }


}
