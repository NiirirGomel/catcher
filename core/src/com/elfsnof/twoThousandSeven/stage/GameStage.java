package com.elfsnof.twoThousandSeven.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.elfsnof.twoThousandSeven.gui.Gui;
import com.elfsnof.twoThousandSeven.location.objects.Gift;
import com.elfsnof.twoThousandSeven.location.Location;

/**
 * Created by elfsn on 29.01.2017.
 */

public class GameStage extends Stage implements ContactListener {
    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;
    public World world;
    private Box2DDebugRenderer renderer;

    public Progress progress = new Progress();
    public GameState state;

    public Location location;
    public Gui gui;

    public GameStage() {
        super(new ExtendViewport(10,10));
        getCamera().position.set(0,0,0);
        Gdx.input.setInputProcessor(this);

        world = new World(new Vector2(0, -20f), true);
        world.setContactListener(this);
        renderer = new Box2DDebugRenderer();

        location = new Location(this);
        addActor(location);

        gui = new Gui(this);
        addActor(gui);

        setState(GameState.MENU);
    }

    public void setState(GameState state) {
        switch (state){
            case PLAY:
                gui.setVisible(false);
                location.gameScore.setScore(0);
                location.gameTimer.start(progress.timeout);
                location.catcher.setTouchListen(true);
                break;
            case MENU:
                progress.saveScore(location.gameScore.score);
                location.catcher.setTouchListen(false);
                gui.storeBtn.scoreCounter.refresh();
                gui.setVisible(true);
                break;
        }
        this.state = state;
    }

    @Override
    public void act(float delta) {

        Array<Body> bodies = new Array<Body>(world.getBodyCount());
        world.getBodies(bodies);

        for(Body body: bodies) {
            update(body);
        }

        // Fixed timestep
        accumulator += delta;
        while (accumulator >= delta) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }

        super.act(delta);
    }

    private void update(Body body) {

    }


    @Override
    public void draw() {
        super.draw();
        // отрисовка всех коллайдеров на стейдже
        renderer.render(world,getCamera().combined);
    }

    @Override
    public Actor hit(float stageX, float stageY, boolean touchable) {
        Actor target = super.hit(stageX, stageY, touchable);
        return target;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // расшифровываем координаты касания и записываем их в touchPoint
        //getCamera().unproject(touchPoint.set(screenX,screenY,0));
        //console.setText("touchPoint"+touchPoint.toString());
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //getCamera().unproject(touchPoint.set(screenX,screenY,0));
        //GameConsole.setText("touchPoint"+touchPoint.toString());

        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //getCamera().unproject(touchPoint.set(screenX,screenY,0));
        //console.setText("touchPoint"+touchPoint.toString());

        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public void beginContact(Contact contact) {

    }

    @Override
    public void endContact(Contact p1) {

    }

    @Override
    public void preSolve(Contact contact, Manifold p2) {
        if(contact.isTouching()){
            // если кетчер на земле убираем трение при движении
            Fixture catcher_fixture = findContact(contact, Category.CATCHER, Category.GROUND);
            if(catcher_fixture != null) {
                Vector2 pos = catcher_fixture.getBody().getPosition();
                WorldManifold manifold = contact.getWorldManifold();
                boolean below = true;
                for(int j = 0; j < manifold.getNumberOfContactPoints(); j++) {
                    below &= (manifold.getPoints()[j].y < pos.y+0.04f);// - getWidth()/2f);
                }
                if(below) {
                    if(location.catcher.isTouched()) {
                        contact.setFriction(0f);
                    } else {
                        contact.setFriction(1f);
                    }
                }
            }
            // если гифт коснулся кетчера то начисляем очки и уничтожаем гифт
            Fixture catch_gift_fixture = findContact(contact, Category.GIFT, Category.CATCHER);
            if(catch_gift_fixture != null) {
                Gift gift = findGift(catch_gift_fixture);
                if(gift != null){
                    contact.setEnabled(false);
                    if(!gift.immortal && !gift.remove && state == GameState.PLAY) {
                        location.gameScore.addScore(gift.score);
                        gift.remove = true;
                    }
                }
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // если гифт на земле уничтожаем его
        Fixture remove_gift_fixture = findContact(contact, Category.GIFT, Category.GROUND);
        if (remove_gift_fixture != null) {
            Gift gift = findGift(remove_gift_fixture);
            if (gift != null && !gift.immortal && !gift.remove) {
                gift.remove = true;
            }
        }
    }

    private Fixture findContact(Contact contact, short find_category, short contact_category){
        Fixture f1 = contact.getFixtureA();
        Fixture f2 = contact.getFixtureB();
        if (f1.getFilterData().categoryBits == find_category && f2.getFilterData().categoryBits == contact_category) {
            return f1;
        } else if (f1.getFilterData().categoryBits == contact_category && f2.getFilterData().categoryBits == find_category) {
            return f2;
        }
        return null;
    }

    private Gift findGift(Fixture fixture) {
        for (Actor actor : location.gifts.getChildren()) {
            Gift gift = (Gift) actor;
            if (gift.fixture == fixture) {
                return gift;
            }
        }
        return null;
    }
}
