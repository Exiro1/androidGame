package com.exiro.fortgame.renderer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.exiro.fortgame.fortGame;
import com.exiro.fortgame.renderer.stages.MenuStage;

public class MenuScreen implements Screen, InputProcessor {

    public final fortGame game;
    OrthographicCamera camera;
    BitmapFont font;
    ShapeRenderer shapeRenderer;
    MenuStage menuStage;
    InputMultiplexer inputMultiplexer;


    public MenuScreen(fortGame game) {
        this.game = game;
        //font = new BitmapFont();

        shapeRenderer = game.shapeRenderer;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(fortGame.VIRTUAL_WIDTH, fortGame.VIRTUAL_HEIGHT);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        menuStage = new MenuStage(new StretchViewport(fortGame.VIRTUAL_WIDTH, fortGame.VIRTUAL_HEIGHT, camera), this);


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/fonttest.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose();
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(menuStage.getStage());
        inputMultiplexer.addProcessor(game.player.getBase().getBaseStage());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 1f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        menuStage.act(Gdx.graphics.getDeltaTime());
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        /*Matrix4 matrix = game.batch.getTransformMatrix();
        shapeRenderer.setTransformMatrix(matrix);
*/
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(0, 160, 60, 20);
        shapeRenderer.rect(260, 160, 60, 20);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(300, 10, 5, 75);
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(300, 10, 5, 120);

        shapeRenderer.end();

        //additional drawing
        game.batch.begin();
        game.batch.setColor(Color.BLACK);
        font.getData().setScale(0.50f);
        font.draw(game.batch, "Money : " + game.player.getMoney(), 5, 170);
        font.draw(game.batch, game.player.getName(), 265, 170);
        game.batch.end();

        //menuStage drawing
        menuStage.draw();
        game.player.getBase().draw(game.batch, shapeRenderer);


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
        shapeRenderer.dispose();
        font.dispose();
        menuStage.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println(fortGame.TAG + " touch : " + screenX + " " + screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
