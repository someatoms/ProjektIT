package se.chalmers.tda367.vt13.dimensions.controller.screens;

import se.chalmers.tda367.vt13.dimensions.components.MenuButton;
import se.chalmers.tda367.vt13.dimensions.controller.Dimensions;
import se.chalmers.tda367.vt13.dimensions.model.LevelHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen extends AbstractMenuScreen {

	private final TextButton levelSelect;
	private final TextButton option;
	private final TextButton exit;

	public MainMenuScreen(final Dimensions game) {
		super(game);
		levelSelect = new TextButton("Level Select", getButtonStyle());
		option = new TextButton("Options", getButtonStyle());
		exit = new TextButton("Exit game", getButtonStyle());
		MenuButton playButton = new MenuButton("data/play.png");

		playButton.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				dispose();
				game.getGameScreen().nextLevel(
						LevelHandler.getInstance().getNextUnfinishedLevel());
				game.setScreen(game.getGameScreen());

			}
		});

		levelSelect.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				dispose();
				game.setScreen(new LevelSelectScreen(game));

			}
		});

		option.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				dispose();
				game.setScreen(new OptionScreen(game));

			}
		});

		exit.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				System.exit(0);

			}
		});

		getTable().add(playButton);
		getTable().row();
		getTable().add(levelSelect);
		getTable().row();
		getTable().add(option);
		getTable().row();
		getTable().add(exit);
		setStageInput();

	}

	@Override
	public void render(float delta) {
		super.render(delta);

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		super.dispose();

	}

}