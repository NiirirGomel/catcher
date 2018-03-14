package com.elfsnof.twoThousandSeven.stage;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by elfsnof on 30.01.17.
 */

public class Progress {
    Preferences prefs;
    public int score = 0, pitcher_count = 2;
    public float timeout = 20f;

    public Progress() {
        prefs = Gdx.app.getPreferences("Progress");
        load();
    }

    public void load() {
        boolean flush = false;
        if(!prefs.contains("score")) {
            prefs.putInteger("score",score);
            flush = true;
        } else {
            score = prefs.getInteger("score");
        }
        if(!prefs.contains("pitcher_count")) {
            prefs.putInteger("pitcher_count",pitcher_count);
            flush = true;
        } else {
            pitcher_count = prefs.getInteger("pitcher_count");
        }
        if(!prefs.contains("timeout")) {
            prefs.putFloat("timeout",timeout);
            flush = true;
        } else {
            timeout = prefs.getFloat("timeout");
        }
        if (flush) prefs.flush();
    }
    public void saveScore(int score) {
        this.score += score;
        prefs.putInteger("score",this.score);
        prefs.flush();
    }
}
