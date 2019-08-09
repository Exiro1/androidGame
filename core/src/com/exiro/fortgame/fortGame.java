package com.exiro.fortgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.exiro.fortgame.communication.ClientConnection;
import com.exiro.fortgame.renderer.LoadingScreen;

import pl.mk5.gdx.fireapp.GdxFIRApp;
import pl.mk5.gdx.fireapp.GdxFIRAuth;
import pl.mk5.gdx.fireapp.auth.GdxFirebaseUser;
import pl.mk5.gdx.fireapp.functional.Consumer;

public class fortGame extends Game {
	public static final int VIRTUAL_WIDTH = 1920;
	public static final int VIRTUAL_HEIGHT = 1080;
	public static final float VIRTUAL_ASPECT_RATIO = VIRTUAL_WIDTH / VIRTUAL_HEIGHT;
	public SpriteBatch batch;
	public BitmapFont font;
	public AssetManager assetManager;
	public static final String TAG = "FORTGAMETAG";
	public static String USERUID;


	static fortGame instance;
	public ClientConnection clientConnection;

	public static fortGame getInstance() {
		return instance;
	}


	@Override
	public void create () {
		GdxFIRApp.inst().configure();


		GdxFIRAuth.inst().google().signIn().then(new Consumer<GdxFirebaseUser>() {
			@Override
			public void accept(GdxFirebaseUser gdxFirebaseUser) {
				String test = gdxFirebaseUser.getUserInfo().getDisplayName() + " " + gdxFirebaseUser.getUserInfo().getUid();
				USERUID = gdxFirebaseUser.getUserInfo().getUid();
				System.out.println(TAG + " " + test);
				clientConnection = new ClientConnection();
				new Thread(clientConnection).start();
			}
		});

		instance = this;
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
		assetManager.load("testGun.png", Texture.class);
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
	}
}
