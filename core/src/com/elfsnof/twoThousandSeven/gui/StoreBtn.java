package com.elfsnof.twoThousandSeven.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by elfsnof on 09.02.17.
 */

public class StoreBtn extends Group {
    TextureRegion background;
    TextActor text;

    public ScoreCounter scoreCounter;

    InputListener input_listener = new InputListener() {
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if(pointer == 0){
                return true;
            }
            return super.touchDown(event,x,y,pointer,button);
        }

        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            if(pointer == 0 && (x >= 0 && x <= getWidth() && y >= 0 && y <= getHeight())){
                ((Gui)getParent()).setState(GuiState.STORE);
            }
            super.touchUp(event, x, y, pointer, button);
        }
    };

    public StoreBtn(Viewport viewport) {
        background = new TextureRegion(new Texture(Gdx.files.internal("drawable/gui/45t_orange.png")));
        setBounds(-2f,-1f,4f,2f);
        text = new TextActor(viewport);
        text.setText("Store");
        text.setPosition(getWidth()/2f,getHeight()/2f);
        addActor(text);
        scoreCounter = new ScoreCounter(viewport);
        scoreCounter.setPosition(getWidth()/2f,getHeight()/2f-1f);
        addActor(scoreCounter);
        addListener(input_listener);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(background,getX(),getY(),getWidth()/2f,getHeight()/2f,getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
        super.draw(batch, parentAlpha);
    }
}
