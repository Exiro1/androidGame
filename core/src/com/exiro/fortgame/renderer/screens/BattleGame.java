package com.exiro.fortgame.renderer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.exiro.fortgame.Player;
import com.exiro.fortgame.communication.GameServerConnection;
import com.exiro.fortgame.fortGame;
import com.exiro.fortgame.parts.Parts;
import com.exiro.fortgame.utils.Bullet;

import java.util.Iterator;

public class BattleGame implements Screen {


    static final public int BASE_WIDTH = 160;
    static public final int BASE_HEIGHT = 180;
    final fortGame game;
    final int BASE_DISTANCE = 1000;
    public Viewport leftVp, rightVp;
    GameServerConnection gameThread;
    OrthographicCamera camera, camera2;
    BitmapFont font;
    ShapeRenderer shapeRenderer;
    Texture fd;
    Sprite bg;

    public BattleGame(Player p, fortGame game) {
        this.game = game;
        font = new BitmapFont();

        shapeRenderer = game.shapeRenderer;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        fd = fortGame.getInstance().assetManager.get("background.png", Texture.class);
        bg = new Sprite(fd);
        bg.setPosition(0, 0);
        bg.setSize(BASE_WIDTH + 160, fortGame.VIRTUAL_HEIGHT);

        camera = new OrthographicCamera();
        camera2 = new OrthographicCamera();

        leftVp = new StretchViewport(BASE_WIDTH, BASE_HEIGHT, camera);
        leftVp.setScreenBounds(0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        leftVp.apply();
        leftVp.apply();

        rightVp = new StretchViewport(BASE_WIDTH, BASE_HEIGHT, camera2);
        rightVp.setScreenX(Gdx.graphics.getWidth() / 2);
        rightVp.setScreenBounds(Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        rightVp.apply();

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera2.position.set(BASE_DISTANCE + BASE_WIDTH / 2, camera2.viewportHeight / 2f, 0);

        gameThread = new GameServerConnection(p.getSocket(), this);
        Thread thread = new Thread(gameThread);
        thread.start();

    }

    @Override
    public void show() {
        bg.setSize(gameThread.distance + BASE_WIDTH, fortGame.VIRTUAL_HEIGHT);


        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera2.position.set(gameThread.distance + BASE_WIDTH / 2, camera2.viewportHeight / 2f, 0);
        camera.update();
        camera2.update();


        for (Parts parts : gameThread.p2.getBase().getParts().values()) {
            parts.spritePos(parts.getXpart(), parts.getYpart());
            parts.addListenerGame();
        }
        for (Parts parts : gameThread.p1.getBase().getParts().values()) {
            parts.spritePos(parts.getXpart(), parts.getYpart());
            parts.addListenerGame();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 1f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        /*Left Half*/


        leftVp.apply();
        game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        game.batch.begin();
        bg.draw(game.batch);
        game.batch.setColor(Color.BLACK);
        font.getData().setScale(0.50f);
        font.draw(game.batch, gameThread.p1.getName() + " t", 5, 170);
        drawBullets(gameThread.p1, game.batch);
        drawBullets(gameThread.p2, game.batch);
        game.batch.end();
        gameThread.p1.getBase().draw(game.batch, shapeRenderer);

        /*Right Half*/

        rightVp.apply();
        game.batch.setProjectionMatrix(camera2.combined);
        shapeRenderer.setProjectionMatrix(camera2.combined);

        game.batch.begin();
        bg.draw(game.batch);
        game.batch.setColor(Color.BLACK);
        font.getData().setScale(0.50f);
        font.draw(game.batch, gameThread.p2.getName() + " t", gameThread.distance - 50, 170);
        drawBullets(gameThread.p1, game.batch);
        drawBullets(gameThread.p2, game.batch);
        game.batch.end();
        gameThread.p2.getBase().draw(game.batch, shapeRenderer);


    }

    public void drawBullets(Player p, SpriteBatch batch) {
        for (Iterator<Bullet> it = p.getBullets().iterator(); it.hasNext(); ) {
            Bullet b = it.next();
            b.draw(batch);
        }
    }


    @Override
    public void resize(int width, int height) {
        leftVp.update(width / 2, height);
        rightVp.update(width / 2, height);
        rightVp.setScreenX(width / 2);
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
        shapeRenderer.dispose();
        font.dispose();
    }
}
