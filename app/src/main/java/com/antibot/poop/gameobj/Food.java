package com.antibot.poop.gameobj;

import com.antibot.poop.Assets;
import com.antibot.poop.ObjectHandler;
import com.antibot.poop.Static;
import com.antibot.poop.WorldRenderer;
import com.framework.shaderPrograms.TextureShaderProgram;
import com.game.framework.gl.SpriteBatcher;
import com.game.framework.gl.TextureRegion;
import com.game.math.Vector2;

public class Food extends CircularCollidableObject
{
    public static final float RANGE = 1.2f;

	public static final float RADIUS = 0.2f;

    public static final float ANGULAR_VEL = 30;


	
	public static final int INDEX_FOOD_DONUT = 0, INDEX_FOOD_BURGER = 1;
	
	public int index;  // for rendering different foods
	

    CollidableObject eatenByObj;

    private float alpha;
    private float scale;

    private float angularVel;
    private float angle;
	
	public Food()
	{
		super(ObjectHandler.INDEX_FOOD, DRAW_PRIORITY_HIGH);
	}
	
	public void set(float posX, float posY, int index)
	{
		super.set(posX, posY);
		this.radius = RADIUS;
		enable();
		this.index = index;

        alpha = 1;

        scale = 1;


       if(Static.rand.nextBoolean()) angularVel = -ANGULAR_VEL;
       else    angularVel = ANGULAR_VEL;

       angle = 0;
	}	

	
	
	@Override
	public void update(float deltaTime)
	{
		
		//if(enabled)
        if(state == STATE_ALIVE_AND_KICKING) {

            angle += angularVel * deltaTime;

            if(angle > 360) angle -= 360;
            else if(angle < 0 ) angle += 360;

        }
        else if(state == STATE_DEATH_PHASE)
        {
            if(eatenByObj != null) {
                Vector2 dir = Static.tempVector;
                float len = dir.set(eatenByObj.pos).sub(pos).len();

                if(len > 1)
                {
                    eatenByObj = null;
                    deathToYou();
                }
                else if(len <= 0.2f)  // crunch successful
                {
                    eatenByObj = null;
                    deathToYou();
                }
                else  //homing effect
                {
                    dir.nor();

                    pos.x += dir.x * deltaTime/len;
                    pos.y += dir.y * deltaTime/len;

                    scale -= 4*deltaTime;
                    if(scale <= 0 ){ scale = 0; deathToYou();}
                }

            }
            else
            {
                alpha -= 5*deltaTime;
                scale -= 5*deltaTime;

                if(alpha <= 0 || scale <= 0)
                {
                    disable();
                }
            }
        }
		
	
		
	}

    public void eaten(CollidableObject obj)
    {
        collidable = false;
        state = STATE_DEATH_PHASE;
        eatenByObj = obj;
    }

	@Override
	public void draw()
	{			
		SpriteBatcher batcher = WorldRenderer.batcher;
		TextureShaderProgram texShaderProgram = WorldRenderer.texShaderProgram;

		TextureRegion foodRegion = Assets.food_arr[index];

        if(state == STATE_ALIVE_AND_KICKING)
		    batcher.drawCarefulSprite(pos.x, pos.y, foodRegion.drawWidth,foodRegion.drawHeight, angle, foodRegion,texShaderProgram);

        else if(state == STATE_DEATH_PHASE)
        {
            if(alpha < 1)
            {
                batcher.prepareForDrawingAlpha(alpha, texShaderProgram);
            }

            batcher.drawCarefulSprite(pos.x, pos.y, foodRegion.drawWidth*scale,foodRegion.drawHeight*scale, angle, foodRegion, texShaderProgram);

            if(alpha < 1)
            {
                batcher.finalizeDrawingAlpha(texShaderProgram);
            }
        }

	}
	
	
	@Override
	public float getBottomPos(){return pos.y - RANGE;}


	@Override
	public float getTopPos(){return pos.y + RANGE;}

    @Override
    public void disable() {
        super.disable();
        eatenByObj = null;
    }

    @Override
    public void deathToYou() {
        super.deathToYou();
        addFoodParticles(4);
    }

    private void addFoodParticles(int count)
    {
        for(int i=0; i<count; i++) {
            Particle p = Static.objHandler.addParticle();
            if(p != null) {
                p.setBasics_Pos(pos.x, pos.y, Static.rand.nextFloat() * 0.3f, 1, Assets.food1);
                p.setVelRandomized(3);
            }
            else break;
        }
    }

    public void interactWithBullet(Bullet bullet) {

        if(standard_CC_CollsionCheck(this,bullet))
        {
            bullet.deathToYou();
            deathToYou();
        }
    }


    public void interactWithBomb(Bomb bomb) {
        if(standard_CC_CollsionCheck(this,bomb))
        {
            bomb.explode();
            deathToYou();
        }
    }
}
