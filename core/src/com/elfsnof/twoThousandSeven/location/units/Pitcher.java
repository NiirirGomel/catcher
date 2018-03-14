package com.elfsnof.twoThousandSeven.location.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import com.elfsnof.twoThousandSeven.location.objects.Gift;
import com.elfsnof.twoThousandSeven.stage.GameStage;
import com.elfsnof.twoThousandSeven.stage.GameState;


/**
 * Created by elfsn on 30.01.2017.
 */

public class Pitcher extends Actor {
    enum State {
        STAY,
        MOVE,
        THROW
    }
    State state;

    GameStage stage;

    TextureRegion textureRegion;
    Rectangle border; // граница перемещения питчера
    float speed = 2;
    int direction = 1;
    float throw_power = 16f;

    Timer timer;
    Timer.Task stay_task = new Timer.Task(){
        @Override
        public void run() {
            setState(State.STAY);
        }
    };
    Timer.Task move_task = new Timer.Task(){
        @Override
        public void run() {
            setState(State.MOVE);

        }
    };
    Timer.Task throw_task = new Timer.Task(){
        @Override
        public void run() {
            setState(stage.state == GameState.PLAY ? State.THROW : State.STAY);
        }
    };

    public Pitcher(final GameStage stage) {
        this.stage = stage;
        float screen_width = stage.getViewport().getWorldWidth();
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("drawable/location/units/pitcher.png")));
        border = new Rectangle(-screen_width/2f,0,screen_width/2f,0);
        setBounds(0,0,2f,2f);
        // запускаем ии
        timer = new Timer();
        setState(State.STAY);
    }

    public void setState(State state) {
        switch (state) {
            case STAY:
                timer.schedule(move_task,1f+MathUtils.random(1f));
                break;
            case MOVE:
                direction = MathUtils.randomSign();
                timer.schedule(throw_task,1f+MathUtils.random(1f));
                break;
            case THROW:
                Gift gift = new Gift(stage.world);
                gift.body.setTransform(getX(), getY() + getWidth() / 2f, 0);
                Vector2 throw_vector = new Vector2(0, throw_power);
                throw_vector.rotate(MathUtils.random(-30f, 30f));
                gift.body.setLinearVelocity(throw_vector);
                stage.location.gifts.addActor(gift);
                timer.schedule(stay_task, 1f + MathUtils.random(1f));
                break;
        }
        this.state = state;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        switch (state){
            case MOVE:
                // меняем направление если достигли границы
                float next_x = getX()+speed*delta*direction;
                if(direction > 0?next_x+getWidth()/2f > border.getWidth():next_x-getWidth()/2f < border.getX()){
                    direction *= -1;
                }
                moveBy(speed*delta*direction,0);
                break;
        }
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return null; // делаем актера некликабельным
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(textureRegion,getX()-getWidth()/2f,getY(),getWidth(),getHeight());
    }
}
