package com.exiro.fortgame.utils;

import com.exiro.fortgame.parts.Parts;

public class Poison {

    static public final double POISON_DAMAGE = 10;
    private double timeLeft;
    private Parts part;

    private Poison(double startTime, Parts p) {
        this.timeLeft = startTime;
        this.part = p;
    }

    public static void setPoison(Parts part, int time) {
        if (part.getPartState() == PartState.NORMAL) {
            part.setPartState(PartState.POISON);
        } else if (part.getPartState() == PartState.FIRE) {
            part.setPartState(PartState.BOTH);
        }
        part.setPoison(new Poison(time, part));

    }

    public double getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(double timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void removeTime(double delta) {
        this.timeLeft -= delta;
    }

    public Parts getPart() {
        return part;
    }

    public void setPart(Parts part) {
        this.part = part;
    }
}
