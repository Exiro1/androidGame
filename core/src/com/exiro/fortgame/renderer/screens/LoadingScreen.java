package com.exiro.fortgame.renderer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.exiro.fortgame.communication.ClientConnection;
import com.exiro.fortgame.fortGame;

public class LoadingScreen implements Screen {

    final fortGame game;

    OrthographicCamera camera;
    Texture loadingTex;
    Sprite loadImage;

    boolean connectionStarted = false;

    @Override
    public void show() {

    }
    public LoadingScreen(final fortGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        loadingTex = new Texture("loading.jpg");

        loadImage = new Sprite(loadingTex);
        loadImage.setPosition(0, 0);
        loadImage.setSize(fortGame.VIRTUAL_WIDTH, fortGame.VIRTUAL_HEIGHT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(fortGame.VIRTUAL_WIDTH, fortGame.VIRTUAL_HEIGHT);

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();



    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (game.assetManager.update()) {
            if (fortGame.SIGNIN) {
                if (!connectionStarted) {
                    connectionStarted = true;
                    ClientConnection clientConnection = new ClientConnection(game.player);
                    new Thread(clientConnection).start();
                }
                if (fortGame.CONNECTED) {
                    this.dispose();
                    game.setScreen(new MenuScreen(game));
                }
            }
        }

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        loadImage.draw(game.batch);
        game.batch.end();


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
        loadingTex.dispose();
    }

}
