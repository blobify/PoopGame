package com.antibot.food.gameobj;


import com.antibot.food.Assets;
import com.antibot.food.WorldRenderer;
import com.antibot.food.ObjectHandler;
import com.antibot.food.Static;
import com.framework.shaderPrograms.TextureShaderProgram;
import com.game.framework.gl.SpriteBatcher;
import com.game.framework.gl.TextureRegion;
import com.game.math.Vector2;

public class Shooter extends CircularCollidableObject
{
	public static final float RADIUS = 0.25f;
	public static final float ANGULAR_VEL = 120f;  // in degrees per sec
	public static final float BULLET_SPEED = 6f;
	public static final int BULLET_DURATION = 80;
	
	public float angle;
	public float angleToReach;

    private float alpha;
	
	public Vector2 velocity;
	
	private int bulletSetCounter;
	
	private ShooterEventListener shootListener;

	public Shooter( ShooterEventListener listener)
	{
		super(ObjectHandler.INDEX_SHOOTER, DRAW_PRIORITY_MED);	
		velocity = new Vector2();
		
		bulletSetCounter = 0;
		this.shootListener = listener;
	}
	
	
	
	public void set(float posX, float posY)
	{
		super.set(posX, posY);
		this.radius = RADIUS;
		angle = 0;
		
		bulletSetCounter = 0;
        alpha = 1;
	}
	
	
	@Override
	public void update(float deltaTime )//float deltaTime, Vector2 muscaPos, Vector2 muscaVel, float camBottomPos)
	{		
		if(state == STATE_ALIVE_AND_KICKING)
		{
			Musca musca = Static.musca;
			
			Vector2 muscaPos = musca.pos;
			Vector2 muscaVel = musca.vel;
			
			//if(angleSetCounter.index == 1)  //every 3 frames
			{
				//calcInterceptTrajectory(pos, muscaPos, muscaVel,BULLET_SPEED, velocity);
				calcInterceptTrajectory2(pos, muscaPos, muscaVel, velocity);
				angleToReach = velocity.angle();  // varies from 0 to 360
			}
			
			if(angle != angleToReach)
			{
				// determine if ccw or cw
				float diff = angleToReach - angle;
				
				if(diff > 0) // if angleToReach > angle
				{
					if(diff < 180)
					{
						angle += ANGULAR_VEL * deltaTime;
						if(angle >= angleToReach) angle = angleToReach;		
							
					}
					else
					{
						angle -= ANGULAR_VEL * deltaTime;	
						if(angle < 0)	
						{
							angle += 360;
							if(angle < angleToReach)
							{
								angle = angleToReach;
							}
						}
					}
					
				}
				else // if angle > angleToReach
				{
					if(diff > -180)
					{
						angle -= ANGULAR_VEL * deltaTime;	
						if(angle <= angleToReach) angle = angleToReach;
					}
					else
					{
						angle += ANGULAR_VEL * deltaTime;
						if(angle > 360)
						{
							angle -= 360;
							if(angle > angleToReach)
							{
								angle = angleToReach;
							}
						}
					}
					
				}				
				
			}
			
			
			bulletSetCounter++;
			
			if(bulletSetCounter >= BULLET_DURATION && angle == angleToReach)
			{
                float angleInRad = Vector2.TO_RADIANS * angle;
                float spawnX = pos.x + (float)( (radius+Bullet.RADIUS*2)*Math.cos(angleInRad)); //Bullet.RADIUS*2 to prevent scenario of self destruct due to bullet
                float spawnY = pos.y + (float)( (radius+Bullet.RADIUS*2)*Math.sin(angleInRad));

				shootListener.onShoot(spawnX, spawnY, velocity.x*BULLET_SPEED, velocity.y*BULLET_SPEED); //setPosAndScale bullet
				bulletSetCounter = 0;
			}
			
			if(pos.y < Static.cam.bottomPos - RADIUS)
			{
				deathToYou();
			}
		}
        else if(state == STATE_DEATH_PHASE)
        {
            alpha -= 5*deltaTime;
            if(alpha <=0) {
                alpha = 0;
                disable();
            }
        }
		
	
	}

	
	public void calcInterceptTrajectory2(Vector2 shooterPos, Vector2 targetPos, Vector2 targetVel, Vector2 bulletVel)
	{
		final float SOME_CONST = 0.4f;
		
		float dx = targetVel.x * SOME_CONST;
		float dy = targetVel.y * SOME_CONST;
		
		float targetX = targetPos.x + dx;
		float targetY = targetPos.y + dy;
		
		bulletVel.x = targetX - shooterPos.x;
		bulletVel.y = targetY - shooterPos.y;
		
		bulletVel.nor();
	}
	
	public void calcInterceptTrajectory(Vector2 shooterPos, Vector2 targetPos, Vector2 targetVel, float bulletSpeed, Vector2 bulletVel)
	 
	{		
		float a = ((targetVel.x * targetVel.x) + (targetVel.y * targetVel.y) - (bulletSpeed * bulletSpeed));
		float b = 2.0f * (targetVel.x * (targetPos.x - shooterPos.x) + targetVel.y * (targetPos.y - shooterPos.y));
		float c = ((targetPos.x - shooterPos.x) * (targetPos.x - shooterPos.x)) + ((targetPos.y - shooterPos.y) * (targetPos.y - shooterPos.y));

		// First, calculate the discriminant to see if we can even make the shot
		float disc = b*b - 4.0f*a*c;

		// If the discriminant is <0, there is no chance of hitting the target. Just not gonna happen, not in this universe of real numbers
		if (disc < 0.0f) return;

		if (a==0.0f) return;  // Avoid the case of a divide-by-zero

		// Calculate the possible solutions
		float sqrt = (float) Math.sqrt(disc);
		float twoA = 2*a;
		float t1 = (float) ((-b + sqrt) / twoA);
		float t2 = (float) ((-b - sqrt) / twoA);

		float t = Math.max(t1, t2);
		
		if (t < 0.0f) return; // Time-traveling bullets are, sadly, not allowed

		// We get here, we have a valid time. Use it to extrapolate the target's position at time t, or the impact point

		//Vector2 impactpoint = new Vector2(targetstart.x + t * targetvel.x, targetstart.y + t*targetvel.y);

		// And subtract to obtain the vector along which to shoot
		bulletVel.set(targetPos.x + t * targetVel.x - shooterPos.x, targetPos.y + t*targetVel.y - shooterPos.y);

		// Normalize it to unit length
		float len= (float) Math.sqrt(bulletVel.x * bulletVel.x + bulletVel.y * bulletVel.y);
		bulletVel.x /= len;
		bulletVel.y /= len;			

	}

    @Override
    public void deathToYou() {
        super.deathToYou();
        addDeathParticle(pos.x, pos.y, 6);
    }

    public void deathToYou(int numberOfCoinsToSpawn)
    {
        Static.objHandler.addCoins(pos.x, pos.y, numberOfCoinsToSpawn);
        deathToYou();
    }

    @Override
	public void draw()
	{
		SpriteBatcher batcher = WorldRenderer.batcher;
		TextureShaderProgram texShaderProgram = WorldRenderer.texShaderProgram;
				
		TextureRegion region = Assets.shooter_norm;

        if(state == STATE_DEATH_PHASE)
        {
            batcher.prepareForDrawingAlpha(alpha,texShaderProgram);
        }

		batcher.drawCarefulSprite(pos.x,pos.y, region.drawWidth, region.drawHeight, angle, region, texShaderProgram);

        if(state == STATE_DEATH_PHASE)
        {
            batcher.finalizeDrawingAlpha(texShaderProgram);
        }
		
	}

    private void addDeathParticle(float posX, float posY, int count)
    {
        for(int i=0; i<count; i++) {
            Particle p = Static.objHandler.addParticle();
            //p.setBasics_Alpha(posX, posY, Static.rand.nextFloat() * 0.3f, 1, 1.2f, Assets.bullet_state1);
            if(p != null) {

                p.setBasics_Pos(posX, posY, Static.rand.nextFloat() * 0.6f, 1, Assets.bullet_state1);
                p.setAlphaVaryable(1, 2);
                p.setVelRandomized(4);

            }
        }
    }

    public void interactWithBullet(Bullet bullet) {

        if(standard_CC_CollsionCheck(this,bullet))
        {
            bullet.deathToYou();
            deathToYou(3+Static.rand.nextInt(3));  // rewarded handsomly for this
        }

    }

    public void interactWithBomb(Bomb bomb) {
        if(standard_CC_CollsionCheck(this,bomb))
        {
            bomb.explode();
            deathToYou(1+Static.rand.nextInt(2));
        }
    }

    public interface ShooterEventListener
	{
		public void onShoot(float shooterPosX, float shooterPosY, float velX, float velY);
	}
	
	@Override
	public float getBottomPos(){return pos.y - radius;}


	@Override
	public float getTopPos(){return pos.y + radius;}

}
