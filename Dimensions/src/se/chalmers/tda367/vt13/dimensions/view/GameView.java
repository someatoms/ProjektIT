package se.chalmers.tda367.vt13.dimensions.view;

import java.util.Iterator;

import se.chalmers.tda367.vt13.dimensions.model.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Game view.
 * @author Carl Fredriksson
 */
public class GameView {

	// Instance Variables
	private GameModel model;
	private SpriteBatch spriteBatch;
	private Texture platformTexture;
	private Texture playerTexture;
	private Texture speedPowerUpTexture;
	
	// Public Methods
	/**
	 * Constructor.
	 * @param model the GameModel
	 */
	public GameView(GameModel model) {
		this.model = model;
		spriteBatch = new SpriteBatch();
		platformTexture = new Texture(Gdx.files.internal("data/PlatformImg.png"));
		playerTexture = new Texture(Gdx.files.internal("data/PlayerImg.png"));
		speedPowerUpTexture = new Texture(Gdx.files.internal("data/SpeedPowerUpImg.png"));
	}
	
	/**
	 * Draw GameObjects on the screen.
	 */
	public void draw() {
		// Clear screen with white color
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// Draw GameObjects
		spriteBatch.begin();
		Iterator<GameObject> iterator = model.getGameObjects().iterator();
		while (iterator.hasNext()) {
			GameObject gameObject = iterator.next();
			if (gameObject instanceof Platform) {
				spriteBatch.draw(platformTexture, (float)gameObject.getPosition().getX(),
						(float)gameObject.getPosition().getY(), (float)gameObject.getWidth(),
							(float)gameObject.getHeight());
			}
			else if (gameObject instanceof SpeedPowerUp) {
				spriteBatch.draw(speedPowerUpTexture, (float)gameObject.getPosition().getX(),
						(float)gameObject.getPosition().getY(), (float)gameObject.getWidth(),
							(float)gameObject.getHeight());
			}
		}
		Player player = model.getPlayer();
		spriteBatch.draw(playerTexture, (float)player.getPosition().getX(),
				(float)player.getPosition().getY(), (float)player.getWidth(),
					(float) player.getHeight());
		spriteBatch.end();
	}
	
	// Private Methods
	
}