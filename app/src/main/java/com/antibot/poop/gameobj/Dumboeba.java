package com.antibot.poop.gameobj;

import java.util.LinkedList;

import com.antibot.poop.AnimeCounter;
import com.antibot.poop.AnimeCounter.AnimPackage;
import com.antibot.poop.Assets;
import com.antibot.poop.ObjectHandler;
import com.antibot.poop.Static;
import com.antibot.poop.World;
import com.antibot.poop.WorldRenderer;
import com.framework.shaderPrograms.TextureShaderProgram;
import com.framework.utils.Logger;
import com.game.framework.gl.SpriteBatcher;
import com.game.framework.gl.TextureRegion;
import com.game.math.Vector2;

public class Dumboeba extends CircularCollidableObject
{
	public static final float RADIUS = 0.23f;
	public static final float MOVE_SPEED = 4;

	public AnimeCounter animeCounter;
	
	public static final int MAX_PATH_VERTICES = 6;
	public static final float ANGULAR_VEL = 600f; // in degrees per sec

	float targetX, targetY;
	int quadrant;
	int pathDataIndex;

	Vector2 dir;

	float angle;
	float angleToReach;

	DumboebaGroup group; // part of which group?

	boolean newLineCalculatable;

    private float alpha;

	public Dumboeba()
	{

		super(ObjectHandler.INDEX_DUMB_ENEMY, DRAW_PRIORITY_MED);

		animeCounter = new AnimeCounter();
		animeCounter.setAnime(new AnimPackage(Assets.dumb_enemy_arr, AnimeCounter.ANIME_REVERSE, -1, 2));

		pos = new Vector2();
		dir = new Vector2();

	}

	public void setToGroup(DumboebaGroup dumboebaGroup, int index)
	{
		this.radius = RADIUS;
		
		newLineCalculatable = true;	
		pathDataIndex = index;

		group = dumboebaGroup;
		
		pos.set(group.pathData[index], group.pathData[index + 1]);

		dir.set(group.dirData[index], group.dirData[index + 1]);

        alpha = 1;

		enable();

	}

	@Override
	public void update(float deltaTime)
	{
		if (state == STATE_ALIVE_AND_KICKING)
		{
			if (newLineCalculatable)
			{
				newLineCalculatable = false;

				int indexX = pathDataIndex % group.pathData.length;
				int indexY = indexX + 1;

				int nextIndexX = (indexX + 2) % group.pathData.length;
				int nextIndexY = nextIndexX + 1;

				if (Float.isNaN(group.pathData[nextIndexX]))
				{
					nextIndexX = 0;
					nextIndexY = nextIndexX + 1;
				} else if (Float.isNaN(group.pathData[indexX]))
				{
					pathDataIndex = 0;
					indexX = pathDataIndex % group.pathData.length;
					indexY = indexX + 1;
					nextIndexX = (indexX + 2) % group.pathData.length;
					nextIndexY = nextIndexX + 1;
				}

				dir.set(group.dirData[indexX], group.dirData[indexY]);

				targetX = group.pathData[nextIndexX];
				targetY = group.pathData[nextIndexY];

				angleToReach = dir.angle();

				quadrant = DumboebaGroup.determineQuadrant(dir.x, dir.y);

				
				
			}

			if (angle == angleToReach)
				pos.add(dir.x * MOVE_SPEED * deltaTime, dir.y * MOVE_SPEED * deltaTime);
			else
				updateAngle(deltaTime);

			if (DumboebaGroup.checkIfLineTravelComplete(pos, targetX, targetY, quadrant))
			{
				newLineCalculatable = true;
				pathDataIndex = (pathDataIndex + 2) % group.pathData.length;
			}


		
			animeCounter.udpate();
		}
        else if(state == STATE_DEATH_PHASE)
        {
            alpha -= 5*deltaTime;
            if(alpha <= 0 )
            {
                alpha = 0; disable();
            }
        }

	}


	private void updateAngle(float deltaTime)
	{
			// determine if ccw or cw
			float diff = angleToReach - angle;

			if (diff > 0) // if angleToReach > angle
			{
				if (diff < 180)
				{
					angle += ANGULAR_VEL * deltaTime;
					if (angle >= angleToReach)
						angle = angleToReach;

				} else
				{
					angle -= ANGULAR_VEL * deltaTime;
					if (angle < 0)
					{
						angle += 360;
						if (angle < angleToReach)
						{
							angle = angleToReach;
						}
					}
				}

			} else
			// if angle > angleToReach
			{
				if (diff > -180)
				{
					angle -= ANGULAR_VEL * deltaTime;
					if (angle <= angleToReach)
						angle = angleToReach;
				} else
				{
					angle += ANGULAR_VEL * deltaTime;
					if (angle > 360)
					{
						angle -= 360;
						if (angle > angleToReach)
						{
							angle = angleToReach;
						}
					}
				}

			}

		
	}

	@Override
	public void disable()
	{
		super.disable();
	}
	
	@Override
	public void draw()
	{
		SpriteBatcher batcher = WorldRenderer.batcher;
		TextureShaderProgram texShaderProgram = WorldRenderer.texShaderProgram;
				
		TextureRegion region = animeCounter.arr[animeCounter.currentIndex];

        if(state == STATE_DEATH_PHASE)
        {
            batcher.prepareForDrawingAlpha(alpha,texShaderProgram);
        }

		batcher.drawCarefulSprite(pos.x, pos.y,region.drawWidth,region.drawHeight,angle+180,region, texShaderProgram);

        if(state == STATE_DEATH_PHASE)
        {
            batcher.finalizeDrawingAlpha(texShaderProgram);
        }
		
	}


    public void interactWithShooter(Shooter shooter) {
        if(standard_CC_CollsionCheck(this,shooter))
        {
            deathToYou(1);
            shooter.deathToYou(1);
        }
    }

	public void interactWithBullet(Bullet bullet)
	{
		if(standard_CC_CollsionCheck(this,bullet))
	    {
				deathToYou(Static.rand.nextInt(2)+1);
				bullet.deathToYou();

		}
		
	}

    public void interactWithWallBlock(WallBlock wallblock) {

        if(standard_CR_CollisionCheck(this,wallblock))
        {
            deathToYou(1);
            wallblock.deathToYou(1);
        }

    }

    public void interactWithBomb(Bomb bomb) {

        if(standard_CC_CollsionCheck(this,bomb))
        {
           deathToYou(Static.rand.nextInt(2)+1);
           bomb.explode();

        }

    }



	@Override
	public float getBottomPos(){return pos.y - radius;}


	@Override
	public float getTopPos(){return pos.y + radius;}

    @Override
    public void setPosYToBase(float threshold) {
        super.setPosYToBase(threshold);
        targetY-=threshold;
    }

    @Override
    public void deathToYou() {
        super.deathToYou();
        addDeathParticle(pos.x, pos.y, Static.rand.nextInt(3)+2);
    }

    public void deathToYou(int numberOfCoinsToSpawn)
    {
        Static.objHandler.addCoins(pos.x, pos.y, numberOfCoinsToSpawn);
        deathToYou();
    }

    private void addDeathParticle(float posX, float posY, int count)
    {
        for(int i=0; i<count; i++) {
            Particle p = Static.objHandler.addParticle();
            if(p != null) {
                p.setBasics_Pos(posX, posY, Static.rand.nextFloat() * 0.4f, 1, Assets.dumbo_particle_arr[Static.rand.nextInt(Assets.dumbo_particle_arr.length)]);
                p.setAlphaVaryable(1, 1);
                p.setVelRandomized(4);
                p.setAngularVel(0, Static.rand.nextFloat() * 100);
            }

        }
    }

}
