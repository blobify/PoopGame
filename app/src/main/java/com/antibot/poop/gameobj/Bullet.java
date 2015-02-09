package com.antibot.poop.gameobj;


import com.antibot.poop.AnimeCounter;
import com.antibot.poop.Assets;
import com.antibot.poop.ObjectHandler;
import com.antibot.poop.Static;
import com.antibot.poop.WorldRenderer;
import com.framework.shaderPrograms.TextureShaderProgram;
import com.game.framework.gl.SpriteBatcher;
import com.game.framework.gl.TextureRegion;
import com.game.math.Vector2;
import com.antibot.poop.AnimeCounter.AnimPackage;

public class Bullet extends CircularCollidableObject
{
	public static final float RADIUS = 0.08f;
	public static final float MAX_DURATION = 3.5f;
	public Vector2 vel;
	public float duration;

    AnimeCounter animeCounter;
	AnimPackage animPackageNormal;
    AnimPackage animPackageBurst;


	public Bullet()
	{
		super( ObjectHandler.INDEX_BULLET, DRAW_PRIORITY_HIGH);
		vel = new Vector2();
        animeCounter = new AnimeCounter();

        animPackageNormal = new AnimPackage(Assets.bullet_state_arr,AnimeCounter.ANIME_REVERSE,-1,2);
        animPackageBurst = new AnimPackage(Assets.bullet_burst_arr,AnimeCounter.ANIME_ONCE,1,8);
	}
	
	public void set(float posX, float posY, float velX, float velY)
	{
		super.set(posX, posY);
		radius = RADIUS;
		vel.set(velX, velY);
		duration = 0;



		animeCounter.setAnime(animPackageNormal);

	}


	
	@Override
	public void update(float deltaTime)
	{
		if(enabled)
		{
            if(state == STATE_ALIVE_AND_KICKING) {
                pos.x += deltaTime * vel.x;
                pos.y += deltaTime * vel.y;


                duration += deltaTime;

                if (duration >= MAX_DURATION) {
                   deathToYou();
                }


            }
            else
            {
                if(animeCounter.animeComplete)
                {
                    disable();
                }
            }
            animeCounter.udpate();
		}
		
	}

    public void interactWithWallBlock(WallBlock wallblock) {
        if(standard_CR_CollisionCheck(this,wallblock))
        {
            deathToYou();
            wallblock.deathToYou(1+ Static.rand.nextInt(2));
        }
    }

    public void interactWithBomb(Bomb bomb) {
        if(standard_CC_CollsionCheck(this,bomb))
        {
            deathToYou();
            bomb.explode();  //kaboom
        }
    }

    public void interactWithBullet(Bullet bullet) {

        if(standard_CC_CollsionCheck(this,bullet))
        {
            deathToYou();
            bullet.deathToYou();
        }

    }

    @Override
    public void deathToYou() {
        super.deathToYou();
        animeCounter.setAnime(animPackageBurst);
    }

    @Override
	public void draw()
	{
		SpriteBatcher batcher = WorldRenderer.batcher;
		TextureShaderProgram texShaderProgram = WorldRenderer.texShaderProgram;
				
		TextureRegion region = animeCounter.getTextureRegion();//Assets.food1; //temporary bullet texture
		
		batcher.drawCarefulSprite(pos.x, pos.y, region.drawWidth, region.drawHeight, region, texShaderProgram);
		
	}
	
	@Override
	public float getBottomPos(){return pos.y - radius;}


	@Override
	public float getTopPos(){return pos.y + radius;}



}
