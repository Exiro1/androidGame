package com.exiro.fortgame;

import com.exiro.fortgame.base.Base;
import com.exiro.fortgame.utils.Bullet;

import java.net.Socket;
import java.util.ArrayList;

public class Player {

    public int startX;
    Base base;
    String name;
    int money;
    int xp;
    int level;
    int mmPoint;
    Player enemy;

    // bullet pool.
    /*
    private final Array<Bullet> activeBullets = new Array<>();



    private final Pool<Bullet> bulletPool = new Pool<Bullet>() {
        @Override
        protected Bullet newObject() {
            return new Bullet();
        }
    };
    */

    ArrayList<Bullet> bullets;


    Socket socket;
    String ID;

    public Player() {
        this.bullets = new ArrayList<>();
    }


    public Player getEnemy() {
        return enemy;
    }

    public void setEnemy(Player enemy) {
        this.enemy = enemy;
    }


    public int getMmPoint() {
        return mmPoint;
    }

    public void setMmPoint(int mmPoint) {
        this.mmPoint = mmPoint;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setmmPoint(int mmPoint) {
        this.mmPoint = mmPoint;
    }
}
