package com.elfsnof.twoThousandSeven;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.elfsnof.twoThousandSeven.stage.GameStage;

public class MyGdxGame extends ApplicationAdapter {
	GameStage gameStage;
	
	@Override
	public void create () {
		gameStage = new GameStage();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		// Clear the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Update the stage
		gameStage.draw();
		gameStage.act(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int width, int height) {
		gameStage.getViewport().update(width,height,false);
	}
	
	@Override
	public void dispose () {
		gameStage.dispose();
	}
}
