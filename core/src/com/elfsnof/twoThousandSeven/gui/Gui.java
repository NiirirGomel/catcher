package com.elfsnof.twoThousandSeven.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.elfsnof.twoThousandSeven.gui.store.Store;
import com.elfsnof.twoThousandSeven.stage.GameStage;

/**
 * Created by elfsnof on 09.02.17.
 */

public class Gui extends Group {
    GuiState state;
    TextureRegion background;
    PlayBtn playBtn;
    public StoreBtn storeBtn;

    Store store;

    public Gui(GameStage stage) {
        background = new TextureRegion(new Texture(Gdx.files.internal("drawable/gui/45t.png")));
        playBtn = new PlayBtn(stage.getViewport());
        playBtn.setPosition(5f, 3.5f, Align.center);
        addActor(playBtn);
        storeBtn = new StoreBtn(stage.getViewport());
        storeBtn.setPosition(5f, 0, Align.center);
        addActor(storeBtn);
        store = new Store(stage.getViewport());
        addActor(store);
        setState(GuiState.MENU);
    }

    public void setState(GuiState state) {
        if(state != this.state) {
            switch (state) {
                case MENU:
                    store.setVisible(false);
                    storeBtn.setVisible(true);
                    break;
                case STORE:
                    store.setVisible(true);
                    storeBtn.setVisible(false);
                    break;
            }
            this.state = state;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(background,-10f,-5f,20f,10f);
        super.draw(batch, parentAlpha);
    }
}
