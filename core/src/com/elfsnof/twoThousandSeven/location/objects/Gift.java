package com.elfsnof.twoThousandSeven.location.objects;

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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import com.elfsnof.twoThousandSeven.stage.Category;

/**
 * Created by elfsn on 02.02.2017.
 */

public class Gift extends Actor {
    public Body body;
    public Fixture fixture;
    public boolean remove = false;
    TextureRegion textureRegion;

    public boolean immortal = true;
    Timer timer;
    Timer.Task immortal_off = new Timer.Task(){
        @Override
        public void run() {
            immortal = false;
        }
    };

    float immortal_time = 0.2f;
    public int score = 20;

    public Gift(World world) {
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("drawable/location/objects/gift.png")));
        setBounds(0,0,1f,1f);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixture_def = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth()/2f, getHeight()/2f, new Vector2(0f,getHeight()/2f),0f);
        fixture_def.shape = shape;
        //fixture.friction = 1f;
        fixture_def.density = 1f;
        fixture_def.restitution = 0.5f;
        fixture = body.createFixture(fixture_def);
        shape.dispose();
        Filter filter = new Filter();
        filter.categoryBits = Category.GIFT;
        //filter.maskBits = ~Category.CATCHER;
        fixture.setFilterData(filter);

        timer = new Timer();
        timer.schedule(immortal_off, immortal_time);
    }

    @Override
    public void act(float delta) {
        if(remove){
            body.setActive(false);
            body.getWorld().destroyBody(body);
            remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (body != null) {
            setPosition(body.getPosition().x, body.getPosition().y);
            setRotation((float) Math.toDegrees(body.getAngle()));
        }
        batch.draw(textureRegion,getX()-getWidth()/2f,getY(),getWidth()/2f,0,getWidth(),getHeight(),1,1,getRotation());
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return null; // делаем актера некликабельным
    }
}
