package com.exiro.fortgame.communication;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.exiro.fortgame.Player;
import com.exiro.fortgame.base.Base;
import com.exiro.fortgame.fortGame;
import com.exiro.fortgame.utils.Team;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;


public class ClientConnection implements Runnable {


    final String host = "86.248.146.40";  //"86.248.146.40";//"192.168.1.21";
    final int portNumber = 6260;
    Player currPlayer;

    public ClientConnection(Player player) {
        this.currPlayer = player;
    }

    public static void write(String msg, OutputStream out) {
        ByteBuffer dbuf = ByteBuffer.allocate(2);
        dbuf.putShort((short) msg.getBytes().length);
        byte[] length = dbuf.array();
        try {
            out.write(length);
            out.write(msg.getBytes());
            Gdx.app.log(fortGame.TAG, "write : " + length + " " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readInput(InputStream inputStream) throws IOException {

        byte[] messageByte = new byte[65532];
        boolean end = false;
        String dataString = "";

            DataInputStream in = new DataInputStream(inputStream);
            int bytesRead = 0;
            messageByte[0] = in.readByte();
            messageByte[1] = in.readByte();
            ByteBuffer byteBuffer = ByteBuffer.wrap(messageByte, 0, 2);

            int bytesToRead = byteBuffer.getShort();


            in.readFully(messageByte, 0, bytesToRead);
            dataString = new String(messageByte, 0, bytesToRead);
        // Gdx.app.log(fortGame.TAG, "read : " + dataString);
            return dataString;

    }

    public String Querry(String querry) {
        try {

            Socket socket = new Socket(host, portNumber);

            OutputStream os = socket.getOutputStream();
            DataInputStream is = new DataInputStream(socket.getInputStream());
            String message = fortGame.USERUID + ";" + querry;
            write(message, os);
            String str = readInput(is);


            os.close();
            is.close();


            socket.close();

            return str;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    public String askForPseudo() {
        final String[] rep = new String[1];
        rep[0] = "";
        final boolean[] success = {true};
        Gdx.input.getTextInput(new Input.TextInputListener() {
            @Override
            public void input(String text) {
                rep[0] = text;
            }

            @Override
            public void canceled() {
                success[0] = false;
            }
        }, "Entrez votre pseudo", "", "pseudo");
        while (rep[0].equals("") && success[0] == true) {

        }
        return rep[0];
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
            String message = fortGame.USERUID + ";connect";
            write(message, os);
            String str = readInput(is);

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(str);
            Base b = Base.getBaseFromData(str, Team.ALLY, new StretchViewport(fortGame.VIRTUAL_WIDTH, fortGame.VIRTUAL_HEIGHT));
            String pse;
            if ((json.get("name")).equals("")) {
                pse = askForPseudo();
                String pseudo = fortGame.USERUID + ";setPseudo;" + pse;
                System.out.println(fortGame.TAG + " write " + pseudo);
                write(pseudo, os);
                String success = readInput(is);
                System.out.println(fortGame.TAG + " result " + success);
            } else {
                pse = (String) json.get("name");
            }

            os.close();
            is.close();

            socket.close();

            currPlayer.setBase(b);
            currPlayer.setID(fortGame.USERUID);
            currPlayer.setName(pse);
            currPlayer.setMoney(Integer.parseInt(String.valueOf(json.get("money"))));
            currPlayer.setXp(Integer.parseInt(String.valueOf(json.get("xp"))));
            currPlayer.setLevel(Integer.parseInt(String.valueOf(json.get("level"))));
            currPlayer.setmmPoint(Integer.parseInt(String.valueOf(json.get("mmPoint"))));

            fortGame.CONNECTED = true;

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        connect();
    }
}
