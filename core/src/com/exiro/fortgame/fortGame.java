package com.exiro.fortgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.exiro.fortgame.renderer.LoadingScreen;

public class fortGame extends Game {
	public static final int VIRTUAL_WIDTH = 1920;
	public static final int VIRTUAL_HEIGHT = 1080;
	public static final float VIRTUAL_ASPECT_RATIO = VIRTUAL_WIDTH / VIRTUAL_HEIGHT;
	public SpriteBatch batch;
	public BitmapFont font;
	public AssetManager assetManager;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.assetManager = new AssetManager();
		queueAssets(this.assetManager);
		this.setScreen(new LoadingScreen(this));
	}

	/**
	 * enqueue all assets
	 *
	 * @param assetManager assetsManager global Instance
	 */
	public void queueAssets(AssetManager assetManager) {
		assetManager.load("mediumCanonCase.png", Texture.class);
	}


	public void mainProcess() {

	}

	@Override
	public void render() {
		super.render();
		mainProcess();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
