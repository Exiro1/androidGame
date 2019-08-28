package com.exiro.fortgame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public enum BulletType {

    SMALL, MEDIUM, BIG;


    static public Sprite getSprite(BulletType type) {
        Texture t = new Texture(Gdx.files.internal("smallAmmo.png"));
        switch (type) {

            case SMALL:
                t = new Texture(Gdx.files.internal("smallAmmo.png"));
                break;
            case MEDIUM:
                t = new Texture(Gdx.files.internal("mediumAmmo.png"));
                break;
            case BIG:
                t = new Texture(Gdx.files.internal("bigAmmo.png"));
                break;
        }
        return new com.badlogic.gdx.graphics.g2d.Sprite(t);
    }
}
