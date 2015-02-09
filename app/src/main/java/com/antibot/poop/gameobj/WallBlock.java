package com.antibot.poop.gameobj;


import com.antibot.poop.Assets;
import com.antibot.poop.ObjectHandler;
import com.antibot.poop.Static;
import com.antibot.poop.WorldRenderer;
import com.framework.shaderPrograms.TextureShaderProgram;
import com.game.framework.gl.SpriteBatcher;
import com.game.framework.gl.TextureRegion;

public class WallBlock extends RectangularCollidableObject
{

	public static final float LENGTH = 0.72f;
	
    float alpha;
	
	public WallBlock()
	{
		super(ObjectHandler.INDEX_WALL_BLOCK, DRAW_PRIORITY_MED);
		
	}
	
	public void set(float posX, float posY)
	{
		super.set(posX, posY);
		this.width = LENGTH;
        this.height = LENGTH;
        alpha = 1;
	}
	
	@Override
	public void update(float deltaTime)
	{
		if(state == STATE_DEATH_PHASE)
        {
            alpha -= 4*deltaTime;
            if(alpha <= 0) disable();

        }
		
	}
	@Override
	public void draw()
	{
		SpriteBatcher batcher = WorldRenderer.batcher;
		TextureShaderProgram texShaderProgram = WorldRenderer.texShaderProgram;
				
		TextureRegion region = Assets.wallblock;

        if(state == STATE_DEATH_PHASE)
        {
            batcher.prepareForDrawingAlpha(alpha, texShaderProgram);
        }

		batcher.drawCarefulSprite(pos.x, pos.y, width,height, region, texShaderProgram);

        if(state == STATE_DEATH_PHASE)
        {
            batcher.finalizeDrawingAlpha(texShaderProgram);
        }
		
	}

    @Override
    public void deathToYou() {
        super.deathToYou();
        addParticles(6);
    }

    public void deathToYou(int numberOfCoinsToSpawn)
    {
        Static.objHandler.addCoins(pos.x, pos.y, numberOfCoinsToSpawn);
        deathToYou();
    }


    private void addParticles(int count)
    {
        for(int i=0; i<count; i++)
        {
            Particle p = Static.objHandler.addParticle();
            if(p != null) {
                p.setBasics_Alpha(pos.x, pos.y, 0.2f + Static.rand.nextFloat() * 0.3f, 1, 1.3f, Assets.wb_particle_arr[Static.rand.nextInt(Assets.wb_particle_arr.length)]);
                p.setVelRandomized(2);
                p.setAngularVel(0, Static.rand.nextFloat()*200);
            }
        }
    }

    @Override
	public float getBottomPos(){return pos.y - height/2;}


	@Override
	public float getTopPos(){return pos.y + height/2;}

}
