package com.exiro.fortgame.communication;

import java.io.IOException;
import java.net.Socket;

public class ServerListenThread implements Runnable {


    public String lastDataReceived = "";
    Socket serverSocket;
    boolean stop = false;

    public ServerListenThread(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                if (!serverSocket.isClosed()) {
                    String data = ClientConnection.readInput(serverSocket.getInputStream());
                    lastDataReceived = data;

                } else {
                    System.out.println("socket closed");
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("erreur -> diusconnected from server");
                stop = true;
            }
        }

    }
}
