package com.elfsnof.twoThousandSeven.gui.store.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.elfsnof.twoThousandSeven.gui.TextActor;

/**
 * Created by elfsnof on 10.02.17.
 */

public class StoreItem extends Group {
    InputListener input_listener = new InputListener() {
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if(pointer == 0){
                return true;
            }
            return super.touchDown(event,x,y,pointer,button);
        }

        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            if(pointer == 0 && (x >= 0 && x <= getWidth() && y >= 0 && y <= getHeight())){
                touch();
            }
            super.touchUp(event, x, y, pointer, button);
        }
    };

    TextureRegion picture;
    TextActor text;
    int price;

    public StoreItem(Viewport viewport) {
        picture = new TextureRegion(new Texture(Gdx.files.internal("drawable/gui/45t_orange.png")));
        setSize(2f,1.5f);
        text = new TextActor(viewport);
        text.setPosition(getWidth()/2f,getHeight()/2f-1f);
        addActor(text);
        addListener(input_listener);
    }

    public void setPrice(int price) {
        this.price = price;
        text.setText("price: "+price);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(picture,getX(),getY(),getWidth()/2f,getHeight()/2f,getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
        super.draw(batch, parentAlpha);
    }

    public void touch() {

    }
}
