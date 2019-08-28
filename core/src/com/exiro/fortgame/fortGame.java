package com.exiro.fortgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.exiro.fortgame.renderer.screens.LoadingScreen;

import pl.mk5.gdx.fireapp.GdxFIRApp;
import pl.mk5.gdx.fireapp.GdxFIRAuth;
import pl.mk5.gdx.fireapp.auth.GdxFirebaseUser;
import pl.mk5.gdx.fireapp.functional.Consumer;

public class fortGame extends Game {
	public static final int VIRTUAL_WIDTH = 320;
	public static final int VIRTUAL_HEIGHT = 180;
	public static final float VIRTUAL_ASPECT_RATIO = VIRTUAL_WIDTH / VIRTUAL_HEIGHT;

	public SpriteBatch batch;
	public static String USERUID, USERNAME;

	public BitmapFont font;
	public AssetManager assetManager;
	public static final String TAG = "FORTGAMETAG";
	public static boolean SIGNIN, CONNECTED;
	public ShapeRenderer shapeRenderer;


	static fortGame instance;
	public Player player;
	public static fortGame getInstance() {
		return instance;
	}


	@Override
	public void create () {
		GdxFIRApp.inst().configure();
		player = new Player();

		GdxFIRAuth.inst().google().signIn().then(new Consumer<GdxFirebaseUser>() {
			@Override
			public void accept(GdxFirebaseUser gdxFirebaseUser) {
				String test = gdxFirebaseUser.getUserInfo().getDisplayName() + " " + gdxFirebaseUser.getUserInfo().getUid();
				USERUID = gdxFirebaseUser.getUserInfo().getUid();
				System.out.println(TAG + " " + test);
				SIGNIN = true;
			}
		});

		instance = this;
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
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
		assetManager.load("testGun.png", Texture.class);
		assetManager.load("background.png", Texture.class);
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
		GdxFIRAuth.inst().google().signOut();
		assetManager.dispose();
	}


}
