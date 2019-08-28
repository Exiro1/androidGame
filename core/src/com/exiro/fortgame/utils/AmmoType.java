package com.exiro.fortgame.utils;

public enum AmmoType {
    NORMAL, FIRE, POISON, FLASH, DEATH;

    public static AmmoType getAmmoType(int ammotype) {
        AmmoType type = NORMAL;
        switch (ammotype) {
            case 0:
                type = NORMAL;
                break;
            case 1:
                type = FIRE;
                break;
            case 2:
                type = POISON;
                break;
            case 3:
                type = FLASH;
                break;
            case 4:
                type = DEATH;
                break;
        }
        return type;
    }

    public static int getAmmoType(AmmoType type) {
        int atype = 0;
        switch (type) {
            case NORMAL:
                atype = 0;
                break;
            case FIRE:
                atype = 1;
                break;
            case POISON:
                atype = 2;
                break;
            case FLASH:
                atype = 3;
                break;
            case DEATH:
                atype = 4;
                break;
        }
        return atype;
    }
}
