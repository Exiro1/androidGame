package com.exiro.fortgame.parts.guns;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.exiro.fortgame.base.Base;
import com.exiro.fortgame.fortGame;
import com.exiro.fortgame.parts.Parts;
import com.exiro.fortgame.utils.AmmoType;
import com.exiro.fortgame.utils.Bullet;
import com.exiro.fortgame.utils.BulletType;
import com.exiro.fortgame.utils.PartInteractionState;
import com.exiro.fortgame.utils.PartType;
import com.exiro.fortgame.utils.Sprite;
import com.exiro.fortgame.utils.Team;
import com.exiro.fortgame.utils.Worker;

import java.util.List;

public abstract class Gun<T extends Gun<T>> extends Parts<T> {


    boolean autoFire;
    int reloadingTime; //time to reload
    int damage;
    double timeLeft; //time before reloaded
    int targetID;
    boolean fireAmmo;
    boolean flashAmmo;
    boolean poisonAmmo;
    boolean deathAmmo;
    AmmoType currentAmmoType;
    Texture containerTex;
    double timeBeforeImpact = 1000d; //TODO change that to be in XML save File
    boolean fireRequest, fire;  // fireRequest is true when player want to fire , fire is true when server authorize the fire action
    double speed = 100d;

    public Gun(String conatainerTexPath, Sprite defaultSprite, Sprite actionSprite, Sprite damagedSprite, Sprite damagedActionSprite, int ammoCost, int energyCost, int level, int xlength, int ylength, int x, int y, Parts upperPart, Parts exposedSidePart, Base base, List<Worker> workers, boolean fireAmmo, boolean flashAmmo, boolean poisonAmmo, boolean deathAmmo, AmmoType currentAmmoType, String gunTexPath) {
        super(defaultSprite, actionSprite, damagedSprite, damagedActionSprite, PartType.GUN, ammoCost, energyCost, level, xlength, ylength, x, y, 0, 0, upperPart, exposedSidePart, PartInteractionState.NONE, base, workers, gunTexPath);
        this.autoFire = true;
        this.timeLeft = (double) reloadingTime;
        this.targetID = -1;
        this.fireAmmo = fireAmmo;
        this.flashAmmo = flashAmmo;
        this.poisonAmmo = poisonAmmo;
        this.deathAmmo = deathAmmo;
        this.currentAmmoType = currentAmmoType;
        this.containerTex = fortGame.getInstance().assetManager.get(conatainerTexPath, Texture.class);
    }

    @Override
    public void process(double delta) {
        super.process(delta);
        if (this.timeLeft > 0)
            this.timeLeft -= delta / 1000d;

        if (fire) //fire only when server says to
            fire();

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

    }

    public void fire() {
        fire = false;
        fireRequest = false;
        //TODO implement bullet pool

        Parts target = this.getBase().getOwner().getEnemy().getBase().getParts().get(targetID);
        int fireDistance = this.getBase().getOwner().getEnemy().getBase().getxCoord() - this.getXpart() * 20 - 30 + 160 - target.getXpart() * 20 - 30;
        if (this.getBase().getTeam().equals(Team.ALLY)) {

            this.getBase().getOwner().getBullets().add(new Bullet(this.getXpart() * 20 + 30, this.getYpart() * 20 + 30, getDamage(), targetID, getCurrentAmmoType(), fireDistance, speed, BulletType.SMALL, this.getBase().getTeam()));

        } else {

            this.getBase().getOwner().getBullets().add(new Bullet(this.getBase().getxCoord() + 160 - this.getXpart() * 20 - 30, this.getYpart() * 20 + 30, getDamage(), targetID, getCurrentAmmoType(), fireDistance, speed, BulletType.SMALL, this.getBase().getTeam()));

        }

        this.timeLeft = (double) reloadingTime;
    }

    public void setAmmoModifier(int amount) {
        this.setAmmoModifier(amount);
        this.getBase().addAmmoCurrentSupply(-amount);
    }

    public Texture getContainerTex() {
        return containerTex;
    }

    public void setContainerTex(Texture containerTex) {
        this.containerTex = containerTex;
    }

    public boolean isFireRequest() {
        return fireRequest;
    }

    public void setFireRequest(boolean fireRequest) {
        this.fireRequest = fireRequest;
    }

    public boolean isFire() {
        return fire;
    }

    public void setFire(boolean fire) {
        this.fire = fire;
    }

    public void setReloadingTime(int reloadingTime) {
        this.reloadingTime = reloadingTime;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setFireAmmo(boolean fireAmmo) {
        this.fireAmmo = fireAmmo;
    }

    public void setFlashAmmo(boolean flashAmmo) {
        this.flashAmmo = flashAmmo;
    }

    public void setPoisonAmmo(boolean poisonAmmo) {
        this.poisonAmmo = poisonAmmo;
    }

    public void setDeathAmmo(boolean deathAmmo) {
        this.deathAmmo = deathAmmo;
    }

    public boolean isAutoFire() {
        return autoFire;
    }

    public void setAutoFire(boolean autoFire) {
        this.autoFire = autoFire;
    }

    public int getReloadingTime() {
        return reloadingTime;
    }

    public double getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(double timeLeft) {
        this.timeLeft = timeLeft;
    }

    public int getTargetID() {
        return targetID;
    }

    public void setTargetID(int targetID) {
        this.targetID = targetID;
    }

    public double getTimeBeforeImpact() {
        return timeBeforeImpact;
    }

    public void setTimeBeforeImpact(double timeBeforeImpact) {
        this.timeBeforeImpact = timeBeforeImpact;
    }

    public boolean isFireAmmo() {
        return fireAmmo;
    }

    public boolean isFlashAmmo() {
        return flashAmmo;
    }

    public boolean isPoisonAmmo() {
        return poisonAmmo;
    }

    public boolean isDeathAmmo() {
        return deathAmmo;
    }

    public AmmoType getCurrentAmmoType() {
        return currentAmmoType;
    }

    public void setCurrentAmmoType(AmmoType currentAmmoType) {
        this.currentAmmoType = currentAmmoType;
    }
}
