package com.elfsnof.twoThousandSeven.location.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.elfsnof.twoThousandSeven.stage.Category;

import java.util.LinkedList;

/**
 * Created by elfsn on 29.01.2017.
 */

public class Catcher extends Actor {

    private class Touch {
        int pointer;
        float x,y;
        public Touch(int pointer, float x, float y){
            this.pointer = pointer;
            this.x = x;
            this.y = y;
        }
    }
    private LinkedList<Touch> touchList = new LinkedList<Touch>(); // масив касаний экрана
    public boolean isTouched() {
        return !touchList.isEmpty();
    }

    InputListener input_listener = new InputListener() {
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            touchList.add(new Touch(pointer,x+getX(),y+getY()));
            return true;
        }

        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            Touch mv_touch = null;
            for (Touch touch : touchList) {
                if(touch.pointer == pointer){
                    mv_touch = touch;
                }
            }
            if(mv_touch != null){
                mv_touch.x = x+getX();
                mv_touch.y = y+getY();
            }

            super.touchDragged(event, x, y, pointer);
        }

        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            Touch rm_touch = null;
            for (Touch touch : touchList) {
                if(touch.pointer == pointer){
                    rm_touch = touch;
                }
            }
            if(rm_touch != null){
                touchList.remove(rm_touch);
            }

            super.touchUp(event, x, y, pointer, button);
        }
    };

    boolean touchListen = false;

    public Body body;
    public Fixture fixture;
    TextureRegion textureRegion;
    Vector2 moveVector = new Vector2();
    float acceleration = 0.4f;
    float max_speed = 5;

    public Catcher(World world) {
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("drawable/location/units/catcher.png")));
        setBounds(0,0,2f,2f);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixture_def = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(getWidth()/2f);
        shape.setPosition(new Vector2(0f,getHeight()/2f));
        fixture_def.shape = shape;
        //fixture_def.friction = 1f;
        fixture = body.createFixture(fixture_def);
        shape.dispose();

        Filter filter = new Filter();
        filter.categoryBits = Category.CATCHER;
        fixture.setFilterData(filter);
        addListener(input_listener);
    }
    private boolean isPlayerGrounded() {
        Array<Contact> contactList = body.getWorld().getContactList();
        for(int i = 0; i < contactList.size; i++) {
            Contact contact = contactList.get(i);
            Fixture fA = contact.getFixtureA();
            Fixture fB = contact.getFixtureB();
            if(contact.isTouching() && ((fA == fixture && fB.getFilterData().categoryBits == Category.GROUND) || (fB == fixture && fA.getFilterData().categoryBits == Category.GROUND))) {
                Vector2 pos = body.getPosition();
                WorldManifold manifold = contact.getWorldManifold();
                boolean below = true;
                for(int j = 0; j < manifold.getNumberOfContactPoints(); j++) {
                    below &= (manifold.getPoints()[j].y < pos.y+0.04f);// - getWidth()/2f);
                }
                if(below) {
                    if(touchList.isEmpty()) {
                        contact.setFriction(1f);
                    } else {
                        contact.setFriction(0);
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public void setTouchListen(boolean touchListen) {
        if (!touchListen) touchList.clear();
        this.touchListen = touchListen;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(!touchList.isEmpty()){
            Touch last_touch = touchList.getLast();
            if(last_touch.x > 0f){
                moveVector.x = acceleration;
            } else if(last_touch.x < 0) {
                moveVector.x = -acceleration;
            } else {
                moveVector.x = 0;
            }
            if (body != null) {
                Vector2 linearVelocity = body.getLinearVelocity();
                if(linearVelocity.x <= max_speed && linearVelocity.x >= -max_speed){
                    if (moveVector.x > 0 && linearVelocity.x + moveVector.x > max_speed) {
                        moveVector.x = max_speed - linearVelocity.x;
                    } else if(moveVector.x < 0 && linearVelocity.x + moveVector.x < -max_speed) {
                        moveVector.x = -max_speed - linearVelocity.x;
                    }
                    body.setLinearVelocity(linearVelocity.add(moveVector));
                }
            }
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
        return touchListen ? this : super.hit(x,y,touchable);
    }
}
