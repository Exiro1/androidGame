package com.exiro.fortgame.communication;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.exiro.fortgame.Player;
import com.exiro.fortgame.base.Base;
import com.exiro.fortgame.fortGame;
import com.exiro.fortgame.parts.Parts;
import com.exiro.fortgame.parts.guns.Gun;
import com.exiro.fortgame.renderer.screens.BattleGame;
import com.exiro.fortgame.utils.AmmoType;
import com.exiro.fortgame.utils.Bullet;
import com.exiro.fortgame.utils.Fire;
import com.exiro.fortgame.utils.PartInteractionState;
import com.exiro.fortgame.utils.Poison;
import com.exiro.fortgame.utils.Team;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;

public class GameServerConnection implements Runnable {


    public static int GameID;
    public Player p1, p2;
    public int distance;
    Socket serverSocket;
    boolean stop = false;
    ServerListenThread serverListenThread;
    BattleGame battleScreen;
    String lastStatep;
    Parts selected;


    public GameServerConnection(Socket serverSocket, BattleGame battleScreen) {
        this.serverSocket = serverSocket;
        this.serverListenThread = new ServerListenThread(serverSocket);
        this.battleScreen = battleScreen;
    }

    public void reconnect() {

    }

    public void init(String data) {
        String[] pdata = data.split(";");
        p1 = fortGame.getInstance().player;
        p1.setBase(Base.getBaseFromData(pdata[0], Team.ALLY, battleScreen.leftVp));
        p1.getBase().setOwner(p1);
        p1.getBase().setxCoord(0);

        p2 = new Player();
        p2.setBase(Base.getBaseFromData(pdata[1], Team.ENEMY, battleScreen.rightVp));
        p2.getBase().setOwner(p2);
        p2.getBase().setxCoord(Integer.parseInt(pdata[3]));

        p1.setEnemy(p2);
        p2.setEnemy(p1);

        distance = Integer.parseInt(pdata[3]);
        GameID = Integer.parseInt(pdata[2]);
    }

    public void processBullet(Player fr, Player en, double delta) {
        Iterator<Bullet> i = fr.getBullets().iterator();
        while (i.hasNext()) {
            Bullet b = i.next();
            b.process(delta / 1000, en);
            if (b.getDistanceLeft() < 0)
                i.remove();
        }
    }

    public void processPart(Player p, double delta) {
        for (Parts part : p.getBase().getParts().values()) {
            part.process(delta);
        }
    }

    public void updateData(Player player, String data) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsont = (JSONObject) parser.parse(data);
            JSONObject json = (JSONObject) jsont.get(player.getID());
            for (Iterator iterator = json.keySet().iterator(); iterator.hasNext(); ) {
                String key = (String) iterator.next();
                if (Integer.parseInt(key) < 100) {
                    Parts p = player.getBase().getParts().get(Integer.parseInt(key));
                    if (p instanceof Gun) {
                        //TODO mettre les changement d'etat coté client sous forme de requete serveur , l'etat change quand le serveur renvoie le changement d'etat
                        JSONObject gun = (JSONObject) json.get(key);
                        ((Gun) p).setAutoFire((Boolean) gun.get("af"));
                        ((Gun) p).setCurrentAmmoType(AmmoType.getAmmoType(Integer.parseInt(String.valueOf(gun.get("at")))));
                        p.setActive(Boolean.parseBoolean(String.valueOf(gun.get("a"))));
                        if (player.equals(p2)) {
                            ((Gun) p).setTargetID(Integer.parseInt(String.valueOf(gun.get("t"))));

                        }
                        ((Gun) p).setFire(Boolean.parseBoolean(String.valueOf(gun.get("f"))));

                        p.setCurrentLife(Double.parseDouble(String.valueOf(gun.get("l"))));
                        ((Gun) p).setTimeLeft(Double.parseDouble(String.valueOf(gun.get("r"))));
                        if (gun.containsKey("fi"))
                            Fire.setFire(p, Integer.parseInt(String.valueOf(gun.get("fi"))));
                        if (gun.containsKey("p"))
                            Poison.setPoison(p, Integer.parseInt(String.valueOf(gun.get("p"))));
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getPlayerInfo(Player player) {

        JSONObject json = new JSONObject();
        for (Parts p : player.getBase().getParts().values()) {
            JSONObject part = new JSONObject();
            if (p instanceof Gun) {
                part.put("af", ((Gun) p).isAutoFire());
                part.put("at", AmmoType.getAmmoType(((Gun) p).getCurrentAmmoType()));
                part.put("a", p.isActive());
                part.put("t", ((Gun) p).getTargetID());
                part.put("f", ((Gun) p).isFireRequest());
            }
            json.put(p.getID(), part);
        }
        return json.toJSONString();
    }

    public void tick(int delta) {
        String data = serverListenThread.lastDataReceived;
        if (!data.equals("")) {
            updateData(p1, data);
            updateData(p2, data);
            serverListenThread.lastDataReceived = "";
        }
        processBullet(p1, p2, delta);
        processBullet(p2, p1, delta);
        processPart(p1, delta);
        processPart(p2, delta);
    }

    public String getInfoToSend() {
        String newInfo = getPlayerInfo(p1);
        if (newInfo.equals(lastStatep))
            return "";
        lastStatep = newInfo;
        return newInfo;
    }

    public void setListener() {
        for (final Parts part : p1.getBase().getParts().values()) {
            part.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //super.clicked(event, x, y);
                    Parts parts = (Parts) event.getListenerActor();
                    selected = parts;

                    parts.setInteractionState(PartInteractionState.SELECTED);
                    System.out.println(fortGame.TAG + " selectedID " + selected.getID() + " currentLife " + parts.getCurrentLife());
                }
            });
        }
        for (final Parts part : p2.getBase().getParts().values()) {
            part.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //super.clicked(event, x, y);
                    Parts parts = (Parts) event.getListenerActor();

                    if (selected instanceof Gun) {
                        ((Gun) selected).setTargetID(part.getID());

                        parts.setInteractionState(PartInteractionState.TARGETED);
                        System.out.println(fortGame.TAG + " gun selected ID " + selected.getID() + " TargetID " + ((Gun) selected).getTargetID());
                    } else {
                        parts.setInteractionState(PartInteractionState.SELECTED);
                        System.out.println(fortGame.TAG + " selected part ID " + parts.getID() + " currentLife " + parts.getCurrentLife());
                    }
                }
            });
        }
    }


    @Override
    public void run() {


        try {
            String initData = ClientConnection.readInput(serverSocket.getInputStream());
            init(initData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setListener();


        fortGame.getInstance().setScreen(battleScreen);
        InputMultiplexer inputMultiplexer;
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(p1.getBase().getBaseStage());
        inputMultiplexer.addProcessor(p2.getBase().getBaseStage());
        Gdx.input.setInputProcessor(inputMultiplexer);
        try {

            int delta = 0;
            long start;
            String firstData = ClientConnection.readInput(serverSocket.getInputStream());
            Thread thread = new Thread(serverListenThread);
            thread.start();
            p2.setID(firstData);
            while (!stop) {
                start = System.currentTimeMillis();

                tick(delta);

                try {

                    if (50 - (System.currentTimeMillis() - start) > 0)
                        Thread.sleep(50 - (System.currentTimeMillis() - start)); //20 calculs par seconde
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String ndata = getInfoToSend();
                if (!ndata.equals(""))
                    ClientConnection.write(ndata, serverSocket.getOutputStream());
                delta = (int) (System.currentTimeMillis() - start);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            serverSocket.getInputStream().close();
            serverSocket.getOutputStream().close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
