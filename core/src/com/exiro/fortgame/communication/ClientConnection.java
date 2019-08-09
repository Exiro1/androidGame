package com.exiro.fortgame.communication;

import com.badlogic.gdx.Gdx;
import com.exiro.fortgame.base.Base;
import com.exiro.fortgame.fortGame;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;


public class ClientConnection implements Runnable {


    final String host = "86.248.196.81";
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
            String message = fortGame.USERUID + ";Exiro";
            write(message, os);
            String str = readInput(is);
            Base.getBaseFromData(str);

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

    public String readInput(InputStream inputStream) {

        byte[] messageByte = new byte[65532];
        boolean end = false;
        String dataString = "";

        try {
            DataInputStream in = new DataInputStream(inputStream);
            int bytesRead = 0;
            messageByte[0] = in.readByte();
            messageByte[1] = in.readByte();
            ByteBuffer byteBuffer = ByteBuffer.wrap(messageByte, 0, 2);

            int bytesToRead = byteBuffer.getShort();


            in.readFully(messageByte, 0, bytesToRead);
            dataString = new String(messageByte, 0, bytesToRead);
            Gdx.app.log(fortGame.TAG, "read : " + dataString);
            return dataString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void run() {
        connect();
    }
}
