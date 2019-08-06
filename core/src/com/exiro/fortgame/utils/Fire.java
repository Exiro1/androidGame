package com.exiro.fortgame.utils;

import com.exiro.fortgame.parts.Parts;

public class Fire {

    static public final double FIRE_DAMAGE = 5;
    static public final double FIRE_INCREASE_RATE = 2;
    private double life;
    private double propagationTimer;
    private Parts part;

    private Fire(double startLife, Parts p) {
        this.life = startLife;
        this.part = p;
        this.propagationTimer = 0;
    }

    public static void setFire(Parts part, int life) {
        if (part.getPartState() == PartState.POISON) {
            part.setPartState(PartState.BOTH);
        } else if (part.getPartState() == PartState.NORMAL) {
            part.setPartState(PartState.FIRE);
        }
        part.setFire(new Fire(life, part));
    }

    public double getLife() {
        return life;
    }

    public void setLife(double life) {
        this.life = life;
    }

    private void propagation() {
        this.propagationTimer = 0;
    }

    public void addLife(double life) {
        this.life += life;
        this.propagationTimer += life;
        if (this.propagationTimer > 30)
            propagation();
        if (this.life > 100)
            this.life = 100;
    }
}
