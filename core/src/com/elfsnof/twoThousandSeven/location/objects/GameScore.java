package com.elfsnof.twoThousandSeven.location.objects;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.elfsnof.twoThousandSeven.gui.TextActor;

/**
 * Created by elfsn on 05.02.2017.
 */

public class GameScore extends TextActor {
    public int score = 0;

    public GameScore(Viewport viewport) {
        super(viewport);
        setText(score+"");
    }

    public void addScore(int score) {
        this.score += score;
        setText(this.score+"");
    }

    public void setScore(int score) {
        this.score = score;
        setText(this.score+"");
    }
}
