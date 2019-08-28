package com.exiro.fortgame.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;
import com.exiro.fortgame.Player;

public class Bullet implements Pool.Poolable {
    //pool variable
    public boolean alive;
    double damage;
    int targetID;
    AmmoType type;
    double speed;  // Dunit/s
    double distanceLeft;
    double posX, posY;
    BulletType bulletType;
    Team t;
    boolean visible;
    double totalD;
    Sprite sprite;
    int dir;

    public Bullet(float posX, float posY, double damage, int targetID, AmmoType type, double distance, double speed, BulletType bulletType, Team team) {
        this.damage = damage;
        this.targetID = targetID;
        this.type = type;
        this.posX = posX;
        this.posY = posY;
        this.bulletType = bulletType;
        this.distanceLeft = distance;
        this.speed = speed;
        totalD = distanceLeft;
        sprite = BulletType.getSprite(bulletType);
        sprite.setSize(3, 1);
        dir = 1;
        t = team;
        System.out.println(" Bullet from " + posX + " " + posY + " for a total distance of " + totalD);
    }

    public void rotate(int direction) {
        sprite.setRotation((float) Math.toDegrees(Math.atan(direction / 2)));
    }

    public void init(float posX, float posY, double damage, int targetID, AmmoType type, double distance, double speed, BulletType bulletType) {
        alive = true;
        this.damage = damage;
        this.targetID = targetID;
        this.type = type;
        this.posX = posX;
        this.posY = posY;
        this.bulletType = bulletType;
        this.distanceLeft = distance;
        this.speed = speed;
    }


    //les calcul de la balle ce font des deux coté mais seulement le serveur retire les dégats (le client le fait aussi mais juste pour l'affichage, pour evité un delai entre l'impact et les degats
    public void process(double delta, Player enemy) {
        distanceLeft -= speed * delta;
        if (distanceLeft < totalD / 2) {
            posY = posY - speed * delta / 2;
            dir = -1;
            rotate(dir);
        } else {
            posY = posY + speed * delta / 2;
            dir = 1;
            rotate(dir);
        }
        if (t == Team.ALLY) {
            posX = posX + speed * delta;
            if (posX < 160) {

            } else if (posX > enemy.getBase().getxCoord()) {

            }
        } else {
            posX = posX - speed * delta;
            if (posX > enemy.getEnemy().getBase().getxCoord()) {

            } else if (posX < 160) {

            }
        }
        if (distanceLeft < 0) {
            enemy.getBase().getParts().get(targetID).attacked(this);
            System.out.println(" DISTANCE LEFT < 0 total" + totalD);
        }


    }

    /**
     * Draw bullet , SpriteBatch need to be open
     */
    public void draw(SpriteBatch batch) {
        sprite.setCenter((float) getPosX(), (float) getPosY());
        sprite.draw(batch);
    }


    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public int getTargetID() {
        return targetID;
    }

    public void setTargetID(int targetID) {
        this.targetID = targetID;
    }

    public AmmoType getType() {
        return type;
    }

    public void setType(AmmoType type) {
        this.type = type;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDistanceLeft() {
        return distanceLeft;
    }

    public void setDistanceLeft(double distanceLeft) {
        this.distanceLeft = distanceLeft;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    public void setBulletType(BulletType bulletType) {
        this.bulletType = bulletType;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public void reset() {
        alive = false;
    }
}
