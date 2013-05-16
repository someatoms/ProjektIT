package se.chalmers.tda367.vt13.dimensions.controller.screens;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import se.chalmers.tda367.vt13.dimensions.controller.Dimensions;
import se.chalmers.tda367.vt13.dimensions.model.CollisionHandler;
import se.chalmers.tda367.vt13.dimensions.model.GameObject;
import se.chalmers.tda367.vt13.dimensions.model.GameWorld;

import se.chalmers.tda367.vt13.dimensions.model.Vector3;

import se.chalmers.tda367.vt13.dimensions.model.Level;

import se.chalmers.tda367.vt13.dimensions.model.GameWorld.Dimension;
import se.chalmers.tda367.vt13.dimensions.model.GameWorld.State;
import se.chalmers.tda367.vt13.dimensions.model.SoundObserver;
import se.chalmers.tda367.vt13.dimensions.model.WorldListener;
import se.chalmers.tda367.vt13.dimensions.util.TiledMapHandler;
import se.chalmers.tda367.vt13.dimensions.view.GameView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;

public class GameScreen implements Screen, SoundObserver, WorldListener {
	private GameWorld world;
	private GameView view;
	private Map<String, Sound> files;
	private Dimensions game;
	private boolean wasEscapePressed = false;
	private boolean wasEnterPressed = false;
	private Level nextLevel;

	public GameScreen(Dimensions game) {
		this.game = game;
	}

	@Override
	public void show() {

		TiledMapHandler tiledMapHandler = new TiledMapHandler();
		CollisionHandler collisionHandler = new CollisionHandler(tiledMapHandler);
		world = new GameWorld(nextLevel, collisionHandler);
		world.addWorldListener(this);
		view = new GameView(world, tiledMapHandler.getMap(nextLevel
				.getMapXYPath()), tiledMapHandler.getMap(nextLevel
				.getMapXZPath()));
		tiledMapHandler.setGameView(view);
		loadSoundFiles();
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render(float delta) {
		getInput();
		world.update();
		view.draw();
	}

	private void getInput() {
		getInputUp();
		getInputDown();
		getInputSpecialKeys();
	}

	/**
	 * Handles all input that isn't player navigation
	 */
	private void getInputSpecialKeys() {
		if (Gdx.input.isKeyPressed(Keys.ENTER)) {
			if (!wasEnterPressed) {
				world.resetToCheckPoint();
				wasEnterPressed = true;
			}
		} else {
			wasEnterPressed = false;
		}

		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			if (!wasEscapePressed) {
				togglePause();
				wasEscapePressed = true;
			}
		} else {
			wasEscapePressed = false;
		}
	}

	/**
	 * Called when the game should be paused.
	 */
	private void togglePause() {
		if (world.getCurrentState() == State.GAME_RUNNING) {
			world.setCurrentState(State.GAME_PAUSED);
		} else {
			world.setCurrentState(State.GAME_RUNNING);
		}
	}

	/**
	 * Handles all input from upkeys and uptouchareas.
	 */
	private void getInputUp() {
		if (Gdx.input.isKeyPressed(Keys.UP)
				|| (Gdx.input.isTouched() && (Gdx.input.getY() < Gdx.graphics
						.getHeight() / 2))) {
			// Actions when dimension is XY
			if (world.getDimension() == Dimension.XY) {
				world.getPlayer().jump();

				// Actions when dimension is XZ
			} else if (world.getDimension() == Dimension.XZ) {
				world.getPlayer().moveUp();
			}

		}
	}

	public void nextLevel(Level l) {
		this.nextLevel = l;
	}

	/**
	 * Handles all input from downkeys and downtouchareas.
	 */
	private void getInputDown() {
		if (Gdx.input.isKeyPressed(Keys.DOWN)
				|| (Gdx.input.isTouched() && (Gdx.input.getY() >= Gdx.graphics
						.getHeight() / 2))) {
			// Actions when dimension is XY
			if (world.getDimension() == Dimension.XY) {
				world.getPlayer().dash();

				// Actions when dimension is XZ
			} else if (world.getDimension() == Dimension.XZ) {
				world.getPlayer().moveDown();
			}

		}
	}

	public GameWorld getGameModel() {
		return world;
	}

	@Override
	public void playSound(String s) {
		files.get(s).play(0.5f);
	}

	private void loadSoundFiles() {
		files = new HashMap<String, Sound>();
		for (GameObject g : world.getGameObjects()) {
			g.addObserver(this);
			String file = g.getSoundFileAsString();

			if (!files.containsKey(file) && !file.equals("")) {
				Sound sound = Gdx.audio.newSound(Gdx.files.internal(file));
				files.put(file, sound);
			}
		}
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {

	}

	/**
	 * Performs updates if world state changes
	 */
	@Override
	public void worldChange(State newWorldState, final GameWorld world) {
		if (newWorldState == State.GAME_OVER) {
			game.setScreen(new GameOverScreen(game));
			
		} else if (newWorldState == State.DIMENSION_CHANGE) {
			world.getPlayer().setIsGrounded(true);
			view.changeMap(world.getDimension());
		}
		
		else if(newWorldState == State.DIMENSION_WILLCHANGE){
			view.setDimensionChange(true);
			
			
			Timer t = new Timer();
			t.schedule(new TimerTask(){

				@Override
				public void run() {
					view.setDimensionChange(false);
					
				}
				
			}, 2000);
			
			
			
			
			 
			
			
			
			
		} else if (newWorldState == State.LEVEL_FINISHED) {
			game.setScreen(new LevelFinishedScreen(game));
		}
	}
}
