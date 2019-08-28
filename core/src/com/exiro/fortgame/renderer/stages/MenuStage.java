package com.exiro.fortgame.renderer.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.exiro.fortgame.communication.MatchMakingConnection;
import com.exiro.fortgame.fortGame;
import com.exiro.fortgame.renderer.screens.MenuScreen;

public class MenuStage {


    TextButton playButton;
    MenuScreen ms;
    Skin mySkin;
    Stage stage;

    public MenuStage(Viewport vp, MenuScreen menuScreen) {
        this.ms = menuScreen;
        stage = new Stage(vp, menuScreen.game.batch);
        stage.setDebugAll(true);

        /*Table table = new Table();
        table.setFillParent(true);
        table.center();
        addActor(table);*/
        mySkin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        playButton = new TextButton("Play", mySkin, "default");
        playButton.setSize(60, 20);
        playButton.setPosition(130, 155);
        Gdx.input.setInputProcessor(stage);
        playButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log(fortGame.TAG, "play button pressed");
                MatchMakingConnection matchMakingConnection = new MatchMakingConnection();
                Thread th = new Thread(matchMakingConnection);
                th.start();
            }

        });
        //table.add(playButton);
        stage.addActor(playButton);


    }

    public void act(float delta) {
        stage.act(Gdx.graphics.getDeltaTime());
    }

    public void draw() {
//      playButton.draw(ms.game.batch,1);
        stage.draw();

    }

    public Stage getStage() {
        return stage;
    }

    public void dispose() {

        mySkin.dispose();
    }
}
