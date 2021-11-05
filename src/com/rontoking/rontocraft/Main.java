package com.rontoking.rontocraft;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

public class Main extends ApplicationAdapter {

    // TODO: fix FPS drops when loading chunks.

	public static final int width = 1600, height = 900;
	public static final String title = "RontoCraft";
	public static int windowWidth = width;
	public static int windowHeight = height;
	public static InputManager inputManager = new InputManager();
	public static Vector3 prevPos;
	public static String gameState = "game"; // paused, options menu, inventory...
	public static boolean isRunning = true;
	public static boolean vSyncOn = false;

	@Override
	public void create () {
		Gdx.input.setCursorCatched(true);

		Gdx.input.setInputProcessor(inputManager);
		Block.setUp();
		TextureManager.loadTextures();
		ModelManager.loadModels();
		CameraManager.createCamera();
		World.createEnvironment();
		TerrainGenerator.generate();
		Chat.load();

		AudioManager.music.setLooping(true);
		AudioManager.music.setVolume(0.1f);
		AudioManager.music.play();
	}

	@Override
	public void render () {
		prevPos = new Vector3(Player.position);
		Chat.update();
		InputManager.update();
		if(gameState.equals("game")) {
			Physics.updatePlayer();

			if (playerMoved())
				CameraManager.followPlayer();

			if (CameraManager.shouldUpdateVisibility) {
				CameraManager.shouldUpdateVisibility = false;
				CameraManager.updateVisibleChunks();
			}

			GameInput.setRay();
			GameInput.setBlocksInRange();
			GameInput.setLookedAtBlock();
		}
		CameraManager.updateModels();
		ChunkLoader.loadQueuedChunks();
		Renderer.render();
	}

	public static boolean playerMoved() {
		if (prevPos.x != Player.position.x || prevPos.y != Player.position.y || prevPos.z != Player.position.z) {
			return true;
		}
		return false;
	}

	public static float deltaTime(){
		if(Gdx.graphics.getDeltaTime() <= 0.05f)
			return Gdx.graphics.getDeltaTime();
		return 0.05f;
	}

	@Override
	public void dispose () {
		isRunning = false;
		World.dispose();
		ModelManager.dispose();
		TextureManager.dispose();
		AudioManager.dispose();
		Renderer.dispose();
	}
}