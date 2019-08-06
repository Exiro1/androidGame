package com.exiro.fortgame.utils;

import com.exiro.fortgame.base.Case;

public class Worker {


    public static final int EXTINGUISH_RATE = 4;
    public static final int REPAIR_RATE = 2;
    private double life, maxLife;
    private Case dir, current;
    private Team team;
    private boolean flashed;
    private double flashedTime;
    private PartState partState;


    public void damage(double ammount) {
        this.life -= ammount;
        if (life <= 0)
            kill();

    }

    public void kill() {

    }

    public double getFlashedTime() {
        return flashedTime;
    }

    public void setFlashedTime(double flashedTime) {
        this.flashedTime = flashedTime;
    }

    public double getLife() {
        return life;
    }

    public void setLife(double life) {
        this.life = life;
    }

    public double getMaxLife() {
        return maxLife;
    }

    public void setMaxLife(double maxLife) {
        this.maxLife = maxLife;
    }

    public Case getDir() {
        return dir;
    }

    public void setDir(Case dir) {
        this.dir = dir;
    }

    public Case getCurrent() {
        return current;
    }

    public void setCurrent(Case current) {
        this.current = current;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public boolean isFlashed() {
        return flashed;
    }

    public void setFlashed(boolean flashed) {
        this.flashed = flashed;
    }

    public PartState getPartState() {
        return partState;
    }

    public void setPartState(PartState partState) {
        this.partState = partState;
    }
}
