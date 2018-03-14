package com.elfsnof.twoThousandSeven.location.objects;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.elfsnof.twoThousandSeven.gui.TextActor;
import com.elfsnof.twoThousandSeven.stage.GameStage;
import com.elfsnof.twoThousandSeven.stage.GameState;

/**
 * Created by elfsnof on 08.02.17.
 */

public class GameTimer extends TextActor {
    public float time = 0;
    public float timeout = 0;

    public GameTimer(Viewport viewport) {
        super(viewport);
        setText((int)time+"");
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(((GameStage)getStage()).state == GameState.PLAY) {
            if(setTime(time += delta) >= timeout) {
                setTime(timeout);
                ((GameStage)getStage()).setState(GameState.MENU);
            }
        }
    }

    public float setTime(float time) {
        this.time = time;
        setText((int)time+"");
        return time;
    }

    public void start(float timeout) {
        setTime(0);
        this.timeout = timeout;
    }
}
