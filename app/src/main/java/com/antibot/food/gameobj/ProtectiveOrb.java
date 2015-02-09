package com.antibot.food.gameobj;

import com.antibot.food.WorldRenderer;
import com.antibot.food.Assets;
import com.antibot.food.ObjectHandler;
import com.antibot.food.Static;
import com.framework.shaderPrograms.TextureShaderProgram;
import com.game.framework.gl.SpriteBatcher;
import com.game.math.Vector2;

public class ProtectiveOrb extends CircularCollidableObject
{

	public static final float RADIUS  = 0.34f;
	
	public static final float ANGULAR_VELOCITY = 220;

	float angle;

    Musca acquiredMusca;

	public ProtectiveOrb()
	{		
		super(ObjectHandler.INDEX_PROTECTIVE_ORB,DRAW_PRIORITY_HIGH);
	}
	
	public void set(float posX, float posY)
	{
	
		super.set(posX, posY);
		this.radius = RADIUS;
		
		teleportify(radius,true);



	}
	

	@Override
	public void update(float deltaTime) {
        angle += ANGULAR_VELOCITY * deltaTime;

        if (angle > 360) {
            angle = 360 - angle;
        }


        if (state == STATE_DEATH_PHASE) {

            if(acquiredMusca != null) {
                Vector2 dir = Static.tempVector;
                float len = dir.set(acquiredMusca.pos).sub(pos).len();

                if (len > 1) {

                    acquiredMusca.enableOrb(angle);
                    disable();

                } else if (len <= 0.1f)  // crunch successful
                {
                    acquiredMusca.enableOrb(angle);
                    disable();
                } else  //homing effect
                {
                    dir.nor();

                    pos.x += dir.x * deltaTime / len;
                    pos.y += dir.y * deltaTime / len;

                }
            }
        }
    }

    public void acquireMusca(Musca musca)
    {
        this.acquiredMusca = musca;
        deathToYou();
    }


	@Override
	public void draw()
	{
		SpriteBatcher batcher = WorldRenderer.batcher;
		TextureShaderProgram texShaderProgram = WorldRenderer.texShaderProgram;
				
		batcher.drawCarefulSprite(pos.x, pos.y, radius*2, radius*2, angle, Assets.protective_orb, texShaderProgram);		
	}

	
	@Override
	public float getBottomPos(){return pos.y - radius;}


	@Override
	public float getTopPos(){return pos.y + radius;}

    @Override
    public void disable() {
        super.disable();
        acquiredMusca = null;
    }
}
