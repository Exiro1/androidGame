package com.exiro.fortgame.parts;

import com.exiro.fortgame.base.Base;
import com.exiro.fortgame.utils.DamageState;
import com.exiro.fortgame.utils.Fire;
import com.exiro.fortgame.utils.PartInteractionState;
import com.exiro.fortgame.utils.PartState;
import com.exiro.fortgame.utils.PartType;
import com.exiro.fortgame.utils.Poison;
import com.exiro.fortgame.utils.Sprite;
import com.exiro.fortgame.utils.Worker;

import java.util.List;

public abstract class Parts {

    private final int DESTROY_LIFE_FACTOR = 2;
    private final int REPAIR_LIFE_FACTOR = 3;

    private Sprite defaultSprite, actionSprite, damagedSprite, damagedActionSprite;
    private PartType type;
    private double maxLife, currentLife; //life get negative up to -maxLife (when destroy currentLife = -maxLife) when get to 0 -> get repaired
    private int maxPeople, level, Xlength, Ylength, x, y;
    private List<Worker> workers;
    private int ammoCost, energyCost; //default cost
    private int currentAmmoModifier, currentEnergyModifier; // + = add , - = take
    private Parts upperPart, exposedSidePart, otherSidePart, bottomPart;
    private PartInteractionState interactionState;
    private Base base;
    private boolean active;
    private DamageState damageState;
    private PartState partState;
    private Fire fire;
    private Poison poison;

    public Parts(Sprite defaultSprite, Sprite actionSprite, Sprite damagedSprite, Sprite damagedActionSprite, PartType type, float life, int ammoCost, int energyCost, int maxPeople, int level, int xlength, int ylength, int x, int y, int currentAmmoModifier, int currentEnergyModifier, Parts upperPart, Parts exposedSidePart, PartInteractionState interactionState, Base base, List<Worker> workers) {
        this.defaultSprite = defaultSprite;
        this.actionSprite = actionSprite;
        this.damagedSprite = damagedSprite;
        this.damagedActionSprite = damagedActionSprite;
        this.type = type;
        this.maxLife = life;
        this.ammoCost = ammoCost;
        this.energyCost = energyCost;
        this.maxPeople = maxPeople;
        this.level = level;
        Xlength = xlength;
        Ylength = ylength;
        this.x = x;
        this.y = y;
        this.currentAmmoModifier = currentAmmoModifier;
        this.currentEnergyModifier = currentEnergyModifier;
        this.upperPart = upperPart;
        this.exposedSidePart = exposedSidePart;
        this.interactionState = interactionState;
        this.base = base;
        this.active = true;
        this.damageState = DamageState.FULL;
        this.currentLife = life;
        this.workers = workers;
        this.partState = PartState.NORMAL;
        this.fire = null;
        this.poison = null;
    }

    /**
     * called every Calculation (frame or every X frame for good framerate)
     *
     * @param delta deltaTime since last calculation (ms)
     */
    public void process(double delta) {

        effectProcess(delta);
        switch (this.damageState) {
            case DESTROY:
                destroyCase(delta);
                break;
            case DAMAGED:
                damagedCase(delta);
                break;
            case FULL:
                fullCase(delta);
                break;
        }


    }

    /**
     * manage effect
     *
     * @param delta time in ms
     */
    private void effectProcess(double delta) {
        if (this.fire != null) {
            if (fire.getLife() <= 0) {
                this.fire = null; //delete Fire effect
                if (this.partState == PartState.BOTH) {
                    this.setPartState(PartState.POISON);
                } else {
                    this.setPartState(PartState.NORMAL);
                }
            } else {
                damage(Fire.FIRE_DAMAGE * delta / 1000d);
                for (Worker w : this.workers) {
                    w.damage(Fire.FIRE_DAMAGE * delta / 1000d);
                }
                this.fire.addLife(delta / 1000d * Fire.FIRE_INCREASE_RATE);
            }
        }
        if (poison != null) {
            if (poison.getTimeLeft() <= 0) {
                poison = null;
                if (this.partState == PartState.BOTH) {
                    this.setPartState(PartState.FIRE);
                } else {
                    this.setPartState(PartState.NORMAL);
                }
            } else {
                for (Worker w : this.workers) {
                    w.damage(Poison.POISON_DAMAGE * delta / 1000d);
                }
                poison.removeTime(delta / 1000d);
            }
        }
    }


    private void destroyCase(double delta) {
        for (Worker w : this.workers) {
            if (this.partState == PartState.FIRE) {
                w.setPartState(this.partState);
                this.fire.addLife(-(delta / 1000d * Worker.EXTINGUISH_RATE));
            } else if (this.partState == PartState.NORMAL) {
                w.setPartState(this.partState);
                setCurrentLife(this.getCurrentLife() + Worker.REPAIR_RATE * delta / 1000d);
            }
        }
        if (this.getCurrentLife() > 0) {
            repaired();
        }
    }

    private void damagedCase(double delta) {
        for (Worker w : this.workers) {
            if (this.partState == PartState.FIRE) {
                w.setPartState(this.partState);
                this.fire.addLife(-(delta / 1000d * Worker.EXTINGUISH_RATE));
            } else if (this.partState == PartState.NORMAL) {
                w.setPartState(this.partState);
                setCurrentLife(this.getCurrentLife() + Worker.REPAIR_RATE * delta / 1000d);
            }
        }
        if (this.getCurrentLife() >= this.getMaxLife()) {
            fullRepaired();
        }
    }

    private void fullCase(double delta) {

    }


    private void damage(double amount) {
        this.currentLife -= amount;
        if (this.currentLife < -this.maxLife) {
            this.currentLife = -this.maxLife;
        } else if (this.currentLife <= 0 && this.damageState != DamageState.DESTROY) {
            destroy();
        } else if (this.currentLife < this.maxLife && this.damageState == DamageState.FULL) {
            damaged();
        }
    }


    /**
     * called when get damaged when it was full Life
     */
    public void damaged() {
        this.damageState = DamageState.DAMAGED;
    }

    /**
     * called when destroy
     */
    private void destroy() {
        this.active = false;
        this.base.addAmmoCurrentSupply(this.getAmmoCost() - this.currentAmmoModifier);
        this.base.addEnergyCurrentSupply(this.getEnergyCost() - this.currentEnergyModifier);
        this.damageState = DamageState.DESTROY;
        this.currentLife = -(this.maxLife / DESTROY_LIFE_FACTOR);
    }

    /**
     * called when get repaired when was destroy
     */
    private void repaired() { //active
        this.active = true;
        this.base.addAmmoCurrentSupply(-this.getAmmoCost() + this.currentAmmoModifier);
        this.base.addEnergyCurrentSupply(-this.getEnergyCost() + this.currentEnergyModifier);
        this.damageState = DamageState.DAMAGED;
        this.setCurrentLife(this.maxLife / REPAIR_LIFE_FACTOR);
    }

    /**
     * called when full repaired
     */
    private void fullRepaired() { //full life
        this.active = true;
        this.damageState = DamageState.FULL;
    }

    public abstract void desactivate();

    public abstract void activate();

    public double getMaxLife() {
        return maxLife;
    }

    public void setMaxLife(float maxLife) {
        this.maxLife = maxLife;
    }

    public int getAmmoCost() {
        return ammoCost;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public double getCurrentLife() {
        return currentLife;
    }

    public void setCurrentLife(double currentLife) {
        this.currentLife = currentLife;
    }

    public PartState getPartState() {
        return partState;
    }

    public void setPartState(PartState partState) {
        this.partState = partState;
    }


    public void setFire(Fire f) {
        this.fire = f;
    }

    public void setPoison(Poison poison) {
        this.poison = poison;
    }

    public Base getBase() {
        return base;
    }
}
