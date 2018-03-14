package com.elfsnof.twoThousandSeven.location;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.elfsnof.twoThousandSeven.location.objects.GameScore;
import com.elfsnof.twoThousandSeven.location.objects.GameTimer;
import com.elfsnof.twoThousandSeven.location.units.Catcher;
import com.elfsnof.twoThousandSeven.location.units.Pitcher;
import com.elfsnof.twoThousandSeven.stage.GameStage;
import com.elfsnof.twoThousandSeven.stage.Category;

/**
 * Created by elfsn on 29.01.2017.
 */

public class Location extends Group {
    TextureRegion bg;
    Body body;

    public Catcher catcher;
    public Group pitchers;
    public Group gifts;

    public GameScore gameScore;
    public GameTimer gameTimer;

    public Location(GameStage stage) {
        super();
        // задник
        bg = new TextureRegion(new Texture(Gdx.files.internal("drawable/location/bg.png")));
        makeBorder(stage);

        // счётчик очков
        gameScore = new GameScore(stage.getViewport());
        addActor(gameScore);

        // таймер
        gameTimer = new GameTimer(stage.getViewport());
        gameTimer.setPosition(0,2f);
        addActor(gameTimer);

        // добавление питчеров
        addActor(pitchers = new Group());
        for(int i = 0; i < stage.progress.pitcher_count; i++){
            Pitcher pitcher = new Pitcher(stage);
            int spawn_direction = i%2==0?6-i:-6+(i-1);
            pitcher.setPosition(spawn_direction,-3.5f);
            pitchers.addActor(pitcher);
        }

        // кетчер
        catcher = new Catcher(stage.world);
        catcher.body.setTransform(0,-3.5f,0);
        addActor(catcher);

        // слой для хранения гифтов
        addActor(gifts = new Group());
    }

    public void makeBorder(GameStage stage) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = stage.world.createBody(bodyDef);
        float screen_width = stage.getViewport().getWorldWidth() > stage.getViewport().getWorldHeight() ? stage.getViewport().getWorldWidth() : stage.getViewport().getWorldHeight();
        // земля
        createBoxFixture(0f,-4.25f,screen_width/2f, 0.75f, Category.GROUND);
        // левая стена
        createBoxFixture(-screen_width/2f-0.5f,0f,0.5f, 5f,Category.BORDER);
        // правая стена
        createBoxFixture(screen_width/2f+0.5f,0f,0.5f, 5f,Category.BORDER);
        // потолок
        createBoxFixture(0f,5.5f,screen_width/2f, 0.5f,Category.BORDER);
    }

    private void createBoxFixture(float x, float y, float width, float height, short category){
        FixtureDef fixture_def = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height, new Vector2(x,y),0f);
        fixture_def.shape = shape;
        Fixture fixture = body.createFixture(fixture_def);
        shape.dispose();
        Filter filter = new Filter();
        filter.categoryBits = category;
        fixture.setFilterData(filter);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(bg,-10f,-5f,20f,10f);
        super.draw(batch, parentAlpha);
    }
}
