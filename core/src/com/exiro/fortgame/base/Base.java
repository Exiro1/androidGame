package com.exiro.fortgame.base;

import java.util.List;

public class Base {

    private int ammoBaseSupply, energyBaseSupply, ammoCurrentSupply, energyCurrentSupply;
    private List<Case> Cases; //case used


    public int getAmmoCurrentSupply() {
        return ammoCurrentSupply;
    }

    public void setAmmoCurrentSupply(int ammoCurrentSupply) {
        this.ammoCurrentSupply = ammoCurrentSupply;
    }

    public void addAmmoCurrentSupply(int amount) {
        this.ammoCurrentSupply += amount;
    }

    public int getEnergyCurrentSupply() {
        return energyCurrentSupply;
    }

    public void setEnergyCurrentSupply(int energyCurrentSupply) {
        this.energyCurrentSupply = energyCurrentSupply;
    }

    public void addEnergyCurrentSupply(int amount) {
        this.energyCurrentSupply += amount;
    }
}
