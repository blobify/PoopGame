package com.antibot.food.gameobj;

import java.util.Random;

import com.antibot.food.Assets;
import com.antibot.food.WorldRenderer;
import com.antibot.food.ObjectHandler;
import com.antibot.food.Static;
import com.framework.shaderPrograms.TextureShaderProgram;
import com.game.framework.gl.SpriteBatcher;
import com.game.framework.gl.TextureRegion;
import com.game.math.Vector2;

public class Coin extends CircularCollidableObject
{
	
	public static final float RADIUS  = 0.175f;
    public static final float RADIUS_OF_ATTRACTION = 2.5f;
	public static final float RADIUSINTOTWO = RADIUS*2;
	public static final float  MAX_VEL = 3f;
	
	public Vector2 vel, acc;
	
	private float drawLen;
	private float scale;
	
	boolean bulgingPhase = false;

	
	public Coin()
	{
		super(ObjectHandler.INDEX_COIN, DRAW_PRIORITY_LOW);
		
		vel = new Vector2();
		acc = new Vector2();
	}
		
	
	public void setWithVelocity(float posX, float posY)
	{		
		Random rand = Static.rand;
		if(rand.nextBoolean())
		{
			vel.x = 1 + rand.nextFloat() * MAX_VEL;
		}
		else
		{
			vel.x = -rand.nextFloat() * MAX_VEL - 1;
		}
		
		vel.y = 8 + rand.nextFloat();
		
		
		bulgingPhase = true;
		scale = 0;
		super.set(posX, posY);
		this.radius = RADIUS;
		collidable = false;
	}

    @Override
	public void set(float posX, float posY)
	{		
		setScale(1);
		
		bulgingPhase = false;
		super.set(posX, posY);
		this.radius = RADIUS;
	}

	@Override
	public void update(float deltaTime)
	{
        if(state == STATE_ALIVE_AND_KICKING) {
            if (bulgingPhase) {
                pos.x += vel.x * deltaTime;
                pos.y += vel.y * deltaTime;

                vel.y -= 20 * deltaTime;  // dampening
                if (vel.y < 0) vel.y = 0;


                scale += 3 * deltaTime;

                if (scale > 1) {
                    scale = 1;
                    bulgingPhase = false;
                    collidable = true;
                }
                setDrawLen();
            }
            else
            {
                updateVelAndPos(deltaTime, Static.musca.pos);


                teleportify(radius,true);
            }
        }
		
		else if(state == STATE_DEATH_PHASE)
		{
			reduceScale(deltaTime,3);
			
			if(scale <= 0)
			{
				disable();
			}
		}

	}
	
	private void updateVelAndPos(float deltaTime, Vector2 muscaPos)
	{
        if(pos.y - muscaPos.y <= RADIUS_OF_ATTRACTION*2)  //early exit
        {
            boolean isWithingRangeOfMusca = (muscaPos.x - pos.x) * (muscaPos.x - pos.x) + (muscaPos.y - pos.y) * (muscaPos.y - pos.y) <= RADIUS_OF_ATTRACTION * RADIUS_OF_ATTRACTION;

            if (isWithingRangeOfMusca) {
                Vector2 dir = Static.tempVector;

                float len = dir.set(muscaPos).sub(pos).len();

                dir.nor();

                pos.x += dir.x * 2 * deltaTime / len;
                pos.y += dir.y * 2 * deltaTime / len;
            }

        }
		
	}
	
	private void reduceScale(float deltaTime, float scaleVel)
	{
		scale -= scaleVel * deltaTime;
		setDrawLen();
	}
	
	private void setDrawLen()
	{
		drawLen = scale * RADIUSINTOTWO;
	}
	
	private void setScale(float scale)
	{
		this.scale = scale;
		setDrawLen();
	}
	
	@Override
	public void draw()
	{
		SpriteBatcher batcher = WorldRenderer.batcher;
		TextureShaderProgram texShaderProgram = WorldRenderer.texShaderProgram;
		
		TextureRegion coinRegion = Assets.coin_region;
		
		batcher.drawCarefulSprite(pos.x, pos.y, drawLen, drawLen, coinRegion, texShaderProgram);
	}

	@Override
	public float getBottomPos(){return pos.y - radius;}


	@Override
	public float getTopPos(){return pos.y + radius;}
}
