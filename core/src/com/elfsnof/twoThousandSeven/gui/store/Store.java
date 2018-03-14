package com.elfsnof.twoThousandSeven.gui.store;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.elfsnof.twoThousandSeven.gui.store.items.OneMorePitcher;

/**
 * Created by elfsnof on 10.02.17.
 */

public class Store extends Group {
    Group items;
    public Store(Viewport viewport) {
        items = new Group();
        items.setPosition(-7, 0);
        addActor(items);
        items.addActor(new OneMorePitcher(viewport));
        items.addActor(new OneMorePitcher(viewport));
        items.addActor(new OneMorePitcher(viewport));
        items.addActor(new OneMorePitcher(viewport));
        items.addActor(new OneMorePitcher(viewport));
        items.addActor(new OneMorePitcher(viewport));
        items.addActor(new OneMorePitcher(viewport));
        items.addActor(new OneMorePitcher(viewport));
        items.addActor(new OneMorePitcher(viewport));
        items.addActor(new OneMorePitcher(viewport));
        itemsSort();
    }

    private void itemsSort() {
        int i = 0, j = 0;
        for (Actor item : items.getChildren()) {
            item.setPosition(3*i, -2*j);
            if(i == 2) {
                i = 0;
                j++;
            } else {
                i++;
            }
        }
    }
}
