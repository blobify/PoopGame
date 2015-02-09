package com.antibot.food.gameobj;

import com.antibot.food.WorldRenderer;
import com.antibot.food.Assets;
import com.antibot.food.ObjectHandler;
import com.antibot.food.Static;
import com.framework.shaderPrograms.TextureShaderProgram;
import com.game.framework.gl.SpriteBatcher;
import com.game.framework.gl.TextureRegion;

public class Gem extends CircularCollidableObject
{

    public static final float RADIUS = 0.25f, ANGULAR_VEL = 1, MAX_ANGLE = 20;

    public static final float DECOR_MEAN_ALPHA = 0.5f, DECOR_ALPHA_AMPLITUDE = 0.3f, DECOR_ANGULAR_VEL = 30, DECOR_ALPHA_ANGULAR_VEL = 5;


    float angle; // varies form 45 to -45
    float alpha;

    float angleDecor;
    float alphaDecor; // varies from 1 to 0.5
    float decorAngleDirection;

    float time;

    public Gem()
    {
        super(ObjectHandler.INDEX_GEM,DRAW_PRIORITY_HIGH);
    }


    @Override
    public void set(float posX, float posY) {
        super.set(posX, posY);
        radius = RADIUS;
        angle = 0;
        alpha = 1;
        angleDecor = 0;
        time = 0;
        alphaDecor = DECOR_MEAN_ALPHA;

       if(Static.rand.nextBoolean()) decorAngleDirection = 1;
       else decorAngleDirection = -1;

    }

    @Override
    public float getBottomPos() {
        return pos.y - radius;
    }

    @Override
    public float getTopPos() {
        return pos.y + radius;
    }

    @Override
    public void update(float deltaTime) {

        time += deltaTime;

        if(state == STATE_ALIVE_AND_KICKING) {
            angle = MAX_ANGLE * (float)Math.sin(time * ANGULAR_VEL);

            angleDecor += decorAngleDirection * DECOR_ANGULAR_VEL * deltaTime;

            alphaDecor = DECOR_MEAN_ALPHA + DECOR_ALPHA_AMPLITUDE * (float)Math.sin(time * DECOR_ALPHA_ANGULAR_VEL);

        }
        else if(state == STATE_DEATH_PHASE)
        {
            alphaDecor -= 2*deltaTime;
            if(alphaDecor < 0 ) alphaDecor = 0;

            alpha -= 3*deltaTime;

            if(alpha < 0 )
            {
                alpha = 0;
                disable();
            }

        }
    }


    @Override
    public void deathToYou() {
        super.deathToYou();
        addSomeParticles(4);
    }

    public void addSomeParticles(int count)
    {
        for(int i=0; i<count; i++) {
            Particle p = Static.objHandler.addParticle();
            if(p != null) {
                p.setBasics_Alpha(pos.x, pos.y, Static.rand.nextFloat() * 0.3f, 1, 1.3f, Assets.star);
                p.setVelRandomized(2);
                p.setAngularVel(0, Static.rand.nextFloat()*200);
            }
            else break;
        }
    }

    @Override
    public void draw() {

        SpriteBatcher batcher = WorldRenderer.batcher;
        TextureShaderProgram texShaderProgram = WorldRenderer.texShaderProgram;

        batcher.prepareForDrawingAlpha(alphaDecor, texShaderProgram);
        TextureRegion decorRegion = Assets.gem_decor;
        batcher.drawCarefulSprite(pos.x,pos.y,decorRegion.drawWidth,decorRegion.drawHeight,angleDecor,decorRegion,texShaderProgram);
        batcher.finalizeDrawingAlpha(texShaderProgram);



        TextureRegion gemRegion = Assets.gem;

        if(state == STATE_DEATH_PHASE)
        {
            batcher.prepareForDrawingAlpha(alpha,texShaderProgram);
        }

        batcher.drawCarefulSprite(pos.x,pos.y,gemRegion.drawWidth,gemRegion.drawHeight,angle,gemRegion,texShaderProgram);

        if(state == STATE_DEATH_PHASE)
        {
            batcher.finalizeDrawingAlpha(texShaderProgram);
        }
    }
}
