package com.elfsnof.twoThousandSeven.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by elfsn on 05.02.2017.
 */

public class TextActor extends Group {

    private TextLine textLine;

    public TextActor(Viewport viewport) {
        setScale(viewport.getWorldHeight()/viewport.getScreenHeight());
        textLine = new TextLine();
        addActor(textLine);
    }

    public void setText(CharSequence str) {
        textLine.setText(str);
    }

    protected class TextLine extends Actor{
        BitmapFont font;
        BitmapFontCache fontCache;
        GlyphLayout layout;

        CharSequence text;

        public TextLine() {
            // инициализация шрифта
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/tahoma.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
            params.size = 48;
            font = generator.generateFont(params);
            fontCache = font.getCache();
            generator.dispose();

            setText("");
        }

        public void setText(CharSequence str) {
            // получаем ширину и высоту строки до вывода
            fontCache.clear();
            layout = fontCache.addText(text = str, getX(), getY());
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            font.draw(batch,text,-layout.width/2f,layout.height/2f);
        }
    }
}
