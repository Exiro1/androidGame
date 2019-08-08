package com.exiro.fortgame.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.exiro.fortgame.base.Base;
import com.exiro.fortgame.fortGame;
import com.exiro.fortgame.parts.guns.testGun;
import com.exiro.fortgame.utils.Worker;

import java.util.ArrayList;
import java.util.List;

public class MenuScreen implements Screen {

    final fortGame game;

    public MenuScreen(fortGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        System.out.println(fortGame.TAG + " loaded : " + game.assetManager.getLoadedAssets());
        System.out.println(fortGame.TAG + " loaded : " + game.assetManager.getAssetNames().toString());
        Base b = new Base();
        List<Worker> w = new ArrayList<>();
        testGun test = new testGun(b, w, 0);
        System.out.println(fortGame.TAG + " life " + test.getMaxLife() + " damage " + test.getDamage());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 1f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
