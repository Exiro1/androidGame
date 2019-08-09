package com.exiro.fortgame.parts.guns;

import com.badlogic.gdx.graphics.Texture;
import com.exiro.fortgame.base.Base;
import com.exiro.fortgame.base.Case;
import com.exiro.fortgame.fortGame;
import com.exiro.fortgame.parts.Parts;
import com.exiro.fortgame.utils.AmmoType;
import com.exiro.fortgame.utils.PartInteractionState;
import com.exiro.fortgame.utils.PartType;
import com.exiro.fortgame.utils.Sprite;
import com.exiro.fortgame.utils.Worker;

import java.util.List;

public abstract class Gun<T extends Gun<T>> extends Parts<T> {


    boolean autoFire;
    int reloadingTime;
    int damage;
    double timeLeft;
    Case target;
    boolean fireAmmo;
    boolean flashAmmo;
    boolean poisonAmmo;
    boolean deathAmmo;
    AmmoType currentAmmoType;
    Texture containerTex;
    Texture gun;

    public Gun(String conatainerTexPath, Sprite defaultSprite, Sprite actionSprite, Sprite damagedSprite, Sprite damagedActionSprite, int ammoCost, int energyCost, int level, int xlength, int ylength, int x, int y, Parts upperPart, Parts exposedSidePart, Base base, List<Worker> workers, boolean fireAmmo, boolean flashAmmo, boolean poisonAmmo, boolean deathAmmo, AmmoType currentAmmoType, String gunTexPath) {
        super(defaultSprite, actionSprite, damagedSprite, damagedActionSprite, PartType.GUN, ammoCost, energyCost, level, xlength, ylength, x, y, 0, 0, upperPart, exposedSidePart, PartInteractionState.NONE, base, workers);
        this.autoFire = true;
        this.timeLeft = (double) reloadingTime;
        this.target = null;
        this.fireAmmo = fireAmmo;
        this.flashAmmo = flashAmmo;
        this.poisonAmmo = poisonAmmo;
        this.deathAmmo = deathAmmo;
        this.currentAmmoType = currentAmmoType;
        this.containerTex = fortGame.getInstance().assetManager.get(conatainerTexPath, Texture.class);
        this.gun = fortGame.getInstance().assetManager.get(gunTexPath, Texture.class);
    }

    @Override
    public void process(double delta) {
        super.process(delta);
        if (this.timeLeft > 0)
            this.timeLeft -= delta / 1000d;
        if (this.timeLeft <= 0) {
            if (autoFire)
                fire();
        }


    }

    public void fire() {

    }

    public void setAmmoModifier(int amount) {
        this.setAmmoModifier(amount);
        this.getBase().addAmmoCurrentSupply(-amount);
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

    public Case getTarget() {
        return target;
    }

    public void setTarget(Case target) {
        this.target = target;
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
