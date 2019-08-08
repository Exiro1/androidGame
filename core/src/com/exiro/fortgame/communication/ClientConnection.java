package com.exiro.fortgame.communication;

import com.badlogic.gdx.Gdx;
import com.exiro.fortgame.fortGame;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;


public class ClientConnection implements Runnable {


    final String host = "XX.XXX.XXX.XX";
    final int portNumber = 6260;

    public ClientConnection() {

    }

    public void connect() {

        try {
            System.out.println("Creating socket to '" + host + "' on port " + portNumber + " " + InetAddress.getByName(host));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {

            Socket socket = new Socket(host, portNumber);

            OutputStream os = socket.getOutputStream();
            DataInputStream is = new DataInputStream(socket.getInputStream());

            String responseLine;
            String message = "Test de connection server";
            write(message, os);

            os.close();
            is.close();

            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void write(String msg, OutputStream out) {
        ByteBuffer dbuf = ByteBuffer.allocate(2);
        dbuf.putShort((short) msg.getBytes().length);
        byte[] length = dbuf.array();
        try {
            out.write(length);
            out.write(msg.getBytes());
            Gdx.app.log(fortGame.TAG, "write : " + length + msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        connect();
    }
}
