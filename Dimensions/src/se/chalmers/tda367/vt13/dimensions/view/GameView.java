package se.chalmers.tda367.vt13.dimensions.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import se.chalmers.tda367.vt13.dimensions.model.*;
import se.chalmers.tda367.vt13.dimensions.model.powerup.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Game view.
 * 
 * @author Carl Fredriksson
 */
public class GameView {

	// Instance Variables
	private GameModel model;
	private SpriteBatch spriteBatch;
	private Map<String, Texture> textures;
	private OrthographicCamera camera;

	// Public Methods
	/**
	 * Constructor.
	 * 
	 * @param model
	 *            the GameModel
	 */
	public GameView(GameModel model) {
		this.model = model;
		spriteBatch = new SpriteBatch();

		loadImageFiles();

		camera = new OrthographicCamera();
		camera.setToOrtho(false);

	}

	/**
	 * Draw GameObjects on the screen.
	 */
	public void draw() {
		// Clear screen with white color
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		camera.update();
		updateCameraPosition(3, 10); // Try changing to parameters to get the
										// right feeling
		spriteBatch.setProjectionMatrix(camera.combined);
		// Draw GameObjects
		spriteBatch.begin();

		drawGameObjects();

		spriteBatch.end();
	}

	/**
	 * Makes the camera follow the player
	 * 
	 * @param speed
	 *            How fast the camera is following the player(y-axis)
	 * @param activation
	 *            How many pixels away from center the player is going to be
	 *            before camera starts following
	 */
	private void updateCameraPosition(int speed, int distance) {
		camera.position.x = model.getPlayer().getPosition().getX() + 400;
		float delta = camera.position.y
				- model.getPlayer().getPosition().getY();
		if (Math.abs(delta) > distance) {
			camera.position.y -= delta / 100 * speed;
		}
	}

	private void loadImageFiles() {
		textures = new HashMap<String, Texture>();
		for (GameObject g : model.getGameObjects()) {
			String file = g.getImageFileAsString();
			if (!textures.containsKey(file) && !file.equals("")) {
				Texture t = new Texture(Gdx.files.internal(file));
				textures.put(file, t);
			}
		}
		String file = model.getPlayer().getImageFileAsString();
		textures.put(file, new Texture(Gdx.files.internal(file)));
	}

	private void drawGameObjects() {
		Iterator<GameObject> iterator = model.getGameObjects().iterator();
		while (iterator.hasNext()) {
			GameObject g = iterator.next();
			Vector3 pos = g.getPosition();
			Vector3 size = g.getSize();
			spriteBatch.draw(textures.get(g.getImageFileAsString()),
					pos.getX(), pos.getY(), size.getX(), size.getY());
		}
		Player p = model.getPlayer();
		spriteBatch.draw(textures.get(p.getImageFileAsString()), p
				.getPosition().getX(), p.getPosition().getY(), p.getSize()
				.getX(), p.getSize().getY());
	}

	public void dispose() {
		// All assets should be disposed, music images etc
		spriteBatch.dispose();
	}
}
