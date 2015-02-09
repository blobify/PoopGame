package com.antibot.poop.gameobj;

import com.antibot.poop.AnimeCounter;
import com.antibot.poop.Assets;
import com.antibot.poop.Static;
import com.antibot.poop.UpdateAndDraw;
import com.antibot.poop.World;
import com.antibot.poop.WorldRenderer;
import com.framework.shaderPrograms.TextureShaderProgram;
import com.game.framework.gl.SpriteBatcher;
import com.game.framework.gl.TextureRegion;
import com.game.math.Vector2;

public class Particle implements UpdateAndDraw
{

    public static final int MAX_COUNT = 120;

	public Vector2 pos;
	public Vector2 vel;
    public float angularVel;
    public float scaleVel;
    public float size;
    public float angle;

    public float alpha;
    public float alphaVel;

	public boolean enabled;
	
	//public AnimationCounter animCounter;
	//public AnimeCounter animeCounter;
	



    private boolean sizeVaryable;
    private boolean alphaVaryable;
    private boolean rotatable;
    private boolean velVaryable;

    int updateCount;

    TextureRegion region;

    public Particle()
	{
		pos = new Vector2();
		vel = new Vector2();
		//animeCounter = new AnimeCounter();
	}


	public void setBasics_Alpha(float posX, float posY, float size, float alpha, float alphaVel, TextureRegion region)
    {
        enableAndClean();

        pos.set(posX, posY);
        this.size = size;
        setAlphaVaryable(alpha, alphaVel);
        this.region = region;
    }

    public void setBasics_Pos(float posX, float posY, float size, float scaleVel, TextureRegion region)
    {
        enableAndClean();

        pos.set(posX, posY);

        setSizeVaryable(size, scaleVel);

        this.region = region;
    }

	public void disable()
	{
		enabled = false;
	}

    public void setVel(float velX, float velY)
    {
        vel.set(velX, velY);
        velVaryable = true;
    }

    public void setVelRandomized(float multiplyFactor)
    {
        boolean xInvertible = Static.rand.nextBoolean();
        boolean yInvertible = Static.rand.nextBoolean();

        float xVel = Static.rand.nextFloat()*multiplyFactor;
        if(xInvertible) xVel = -xVel;

        float yVel = Static.rand.nextFloat()*multiplyFactor;
        if(yInvertible) yVel = -yVel;

        setVel(xVel,yVel);
    }

    public void setSizeVaryable(float size, float scaleVel)
    {
        this.size = size;
        this.scaleVel = scaleVel;
        sizeVaryable = true;
    }

    public void setAngularVel(float angle, float angularVel)
    {
        this.angularVel = angularVel;
        this.angle = angle;
        rotatable = true;
    }

    public void setAlphaVaryable(float alpha, float alphaVel)
    {
        this.alpha = alpha;
        this.alphaVel = alphaVel;
        alphaVaryable = true;
    }
	
	@Override
	public void update(float deltaTime)
	{
        updateCount++;
        if(updateCount > MAX_COUNT)
        {
            disable();
            return;
        }

        if(velVaryable) {
            pos.x += vel.x * deltaTime;
            pos.y += vel.y * deltaTime;
        }
        if(rotatable)
        {
            angle += angularVel * deltaTime;
        }
		if(sizeVaryable)
        {
            size -= scaleVel * deltaTime;
            if(size <= 0) disable();
        }
        if(alphaVaryable)
        {
            alpha -= alphaVel * deltaTime;
            if(alpha <= 0 ) disable();
        }

		

	}

	@Override
	public void draw()
	{
		SpriteBatcher batcher = WorldRenderer.batcher;
		TextureShaderProgram texShaderProgram = WorldRenderer.texShaderProgram;

        if(alphaVaryable)
        {
           batcher.prepareForDrawingAlpha(alpha, texShaderProgram);
        }

        if(rotatable)
        {
            batcher.drawCarefulSprite(pos.x, pos.y, size, size, angle, region, texShaderProgram);
        }
        else
        {
            batcher.drawCarefulSprite(pos.x, pos.y, size, size, region, texShaderProgram);
        }

        if(alphaVaryable)
        {
            batcher.finalizeDrawingAlpha(texShaderProgram);
        }
	}

    /*
    this method must be called when setting a new particle
     */
    private void enableAndClean() {
        enabled = true;
        sizeVaryable = false;
        velVaryable = false;
        rotatable = false;
        alphaVaryable = false;

        updateCount = 0;
    }
}
