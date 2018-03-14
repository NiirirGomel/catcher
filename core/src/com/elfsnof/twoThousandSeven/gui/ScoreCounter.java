package com.elfsnof.twoThousandSeven.gui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.elfsnof.twoThousandSeven.stage.GameStage;

/**
 * Created by elfsnof on 09.02.17.
 */

public class ScoreCounter extends Group {
    TextActor score;
    public ScoreCounter(Viewport viewport) {
        score = new TextActor(viewport);
        addActor(score);
    }
    public void refresh() {
        score.setText("score: "+((GameStage)getStage()).progress.score);
    }
}
