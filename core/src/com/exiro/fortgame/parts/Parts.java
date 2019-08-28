package com.exiro.fortgame.parts;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.exiro.fortgame.base.Base;
import com.exiro.fortgame.fortGame;
import com.exiro.fortgame.utils.Bullet;
import com.exiro.fortgame.utils.DamageState;
import com.exiro.fortgame.utils.Fire;
import com.exiro.fortgame.utils.PartInteractionState;
import com.exiro.fortgame.utils.PartState;
import com.exiro.fortgame.utils.PartType;
import com.exiro.fortgame.utils.Poison;
import com.exiro.fortgame.utils.Sprite;
import com.exiro.fortgame.utils.Team;
import com.exiro.fortgame.utils.Worker;

import java.util.List;

public abstract class Parts<T extends Parts<T>> extends Actor {

    private final int DESTROY_LIFE_FACTOR = 2;
    private final int REPAIR_LIFE_FACTOR = 3;


    private int ID;
    private Sprite defaultSprite, actionSprite, damagedSprite, damagedActionSprite;
    private com.badlogic.gdx.graphics.g2d.Sprite actualSprite;
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

    public Parts(Sprite defaultSprite, Sprite actionSprite, Sprite damagedSprite, Sprite damagedActionSprite, PartType type, int ammoCost, int energyCost, int level, int xlength, int ylength, int x, int y, int currentAmmoModifier, int currentEnergyModifier, Parts upperPart, Parts exposedSidePart, PartInteractionState interactionState, Base base, List<Worker> workers, String texPath) {
        this.defaultSprite = defaultSprite;
        this.actionSprite = actionSprite;
        this.damagedSprite = damagedSprite;
        this.damagedActionSprite = damagedActionSprite;
        this.type = type;
        this.ammoCost = ammoCost;
        this.energyCost = energyCost;
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
        this.workers = workers;
        this.partState = PartState.NORMAL;
        this.fire = null;
        this.poison = null;

        actualSprite = new com.badlogic.gdx.graphics.g2d.Sprite(fortGame.getInstance().assetManager.get(texPath, Texture.class));
        if (base.getTeam() == Team.ENEMY)
            actualSprite.flip(true, false);
        spritePos(x, y);
        setTouchable(Touchable.enabled);

    }


    public abstract T getValueFromFile(String path);

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

    public void addListenerGame() {
        this.setBounds(0, 0, 20, 20);

        this.setPosition(actualSprite.getX(), actualSprite.getY());
       /* addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("clicked part ID : "+getID() + " type "+ this.getClass().getName() + " X :"+x + " Y :"+y + " X2 :"+event.getStageX() + " Y2 :"+event.getStageY() );
                return super.touchDown(event, x, y, pointer, button);
            }

        });*/
    }


    public void spritePos(float x, float y) {
        this.x = (int) x;
        this.y = (int) y;

        if (base.getTeam() == Team.ALLY)
            actualSprite.setPosition(x * 20 + 20, y * 20 + 20);
        if (base.getTeam() == Team.ENEMY)
            actualSprite.setPosition(getBase().getxCoord() + 120 - x * 20, y * 20 + 20);

        actualSprite.setSize(20, 20);
        setBounds(actualSprite.getX(), actualSprite.getY(), 20, 20);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        actualSprite.draw(batch);

    }


    public abstract void setLevel(int level);

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

    public void attacked(Bullet b) {
        damage(b.getDamage());
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
        desactivate();
        this.damageState = DamageState.DESTROY;
        this.currentLife = -(this.maxLife / DESTROY_LIFE_FACTOR);
    }

    /**
     * called when get repaired when was destroy
     */
    private void repaired() { //active
        activate();
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

    public void desactivate() {
        this.active = false;
        this.base.addAmmoCurrentSupply(this.getAmmoCost() - this.currentAmmoModifier);
        this.base.addEnergyCurrentSupply(this.getEnergyCost() - this.currentEnergyModifier);
    }

    public void activate() {
        this.active = true;
        this.base.addAmmoCurrentSupply(-this.getAmmoCost() + this.currentAmmoModifier);
        this.base.addEnergyCurrentSupply(-this.getEnergyCost() + this.currentEnergyModifier);
    }

    public double getMaxLife() {
        return maxLife;
    }

    public void setMaxLife(float maxLife) {
        this.maxLife = maxLife;
        this.currentLife = maxLife;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public PartType getType() {
        return type;
    }

    public void setType(PartType type) {
        this.type = type;
    }


    public Sprite getDefaultSprite() {
        return defaultSprite;
    }

    public void setDefaultSprite(Sprite defaultSprite) {
        this.defaultSprite = defaultSprite;
    }

    public Sprite getActionSprite() {
        return actionSprite;
    }

    public void setActionSprite(Sprite actionSprite) {
        this.actionSprite = actionSprite;
    }

    public Sprite getDamagedSprite() {
        return damagedSprite;
    }

    public void setDamagedSprite(Sprite damagedSprite) {
        this.damagedSprite = damagedSprite;
    }

    public Sprite getDamagedActionSprite() {
        return damagedActionSprite;
    }

    public void setDamagedActionSprite(Sprite damagedActionSprite) {
        this.damagedActionSprite = damagedActionSprite;
    }

    public void setMaxLife(double maxLife) {
        this.maxLife = maxLife;
    }

    public int getLevel() {
        return level;
    }

    public int getXlength() {
        return Xlength;
    }

    public void setXlength(int xlength) {
        Xlength = xlength;
    }

    public int getYlength() {
        return Ylength;
    }

    public void setYlength(int ylength) {
        Ylength = ylength;
    }

    public int getXpart() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getYpart() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public void setAmmoCost(int ammoCost) {
        this.ammoCost = ammoCost;
    }

    public void setEnergyCost(int energyCost) {
        this.energyCost = energyCost;
    }

    public int getCurrentAmmoModifier() {
        return currentAmmoModifier;
    }

    public void setCurrentAmmoModifier(int currentAmmoModifier) {
        this.currentAmmoModifier = currentAmmoModifier;
    }

    public int getCurrentEnergyModifier() {
        return currentEnergyModifier;
    }

    public void setCurrentEnergyModifier(int currentEnergyModifier) {
        this.currentEnergyModifier = currentEnergyModifier;
    }

    public Parts getUpperPart() {
        return upperPart;
    }

    public void setUpperPart(Parts upperPart) {
        this.upperPart = upperPart;
    }

    public Parts getExposedSidePart() {
        return exposedSidePart;
    }

    public void setExposedSidePart(Parts exposedSidePart) {
        this.exposedSidePart = exposedSidePart;
    }

    public Parts getOtherSidePart() {
        return otherSidePart;
    }

    public void setOtherSidePart(Parts otherSidePart) {
        this.otherSidePart = otherSidePart;
    }

    public Parts getBottomPart() {
        return bottomPart;
    }

    public void setBottomPart(Parts bottomPart) {
        this.bottomPart = bottomPart;
    }

    public PartInteractionState getInteractionState() {
        return interactionState;
    }

    public void setInteractionState(PartInteractionState interactionState) {
        this.interactionState = interactionState;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public DamageState getDamageState() {
        return damageState;
    }

    public void setDamageState(DamageState damageState) {
        this.damageState = damageState;
    }

    public Fire getFire() {
        return fire;
    }

    public Poison getPoison() {
        return poison;
    }



}
