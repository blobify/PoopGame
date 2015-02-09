package com.antibot.food.gameobj;

import com.antibot.food.WorldRenderer;
import com.antibot.food.Assets;
import com.antibot.food.Static;
import com.antibot.food.UpdateAndDraw;
import com.framework.shaderPrograms.TextureShaderProgram;
import com.game.framework.gl.SpriteBatcher;


public class Nitro implements UpdateAndDraw
{
	public static final float VARY_VELOCITY = 2;
	public static final float DRAW_WIDTH = 0.1f;
    public static final float DEPLETION_RATE_NORMAL = 0.5f, DEPLETION_RATE_REDUCED = 0.3f;

    public static final int MAX_NUMBER_OF_CAPACITY_UPGRAGES = 20;

    public static final int MIN_CAPACITY = 2;

    public float maxCapacity;
    public float bottomPadding;



	public float amount, amountDrawable; //the amount which is drawn on screen
    float absoluteAmount;  // varies from 0 to absoluteCapacity

    private float ratio; // or morsel amount

	public float capacity, capacityDrawable;
    int absoluteCapacity; // varies from 0 to MAX_NUMBER_OF_CAPACITY_UPGRADES
	

	
	public float depletionRate;
	
	boolean varyTheAmountDrawable;
    boolean varyCapacity;
	

    public Nitro()
    {
        resetNitro();
    }

    public void resetNitro()
    {
        maxCapacity = 0.9f * Static.targetHeightFixer; // 90% of targetHeight of screen
        bottomPadding = 0.05f * Static.targetHeightFixer; // 5% of targetHeight of screen


        ratio = maxCapacity/MAX_NUMBER_OF_CAPACITY_UPGRAGES;

        setNitroCapacity();

        amount = 0;
        absoluteAmount = 0;
        amountDrawable = 0;

        varyCapacity = varyTheAmountDrawable = false;
    }

    private void setNitroCapacity()
    {
        absoluteCapacity = Static.preferencesHandler.retrieveNitroCapacity();
        if(absoluteCapacity>MAX_NUMBER_OF_CAPACITY_UPGRAGES){ absoluteCapacity = MAX_NUMBER_OF_CAPACITY_UPGRAGES;}
        capacity = capacityDrawable = absoluteCapacity * ratio;
    }
	
	public boolean addMorsel()
	{
        boolean returnable = true;
        absoluteAmount++;

        if(absoluteAmount > absoluteCapacity){ absoluteAmount = absoluteCapacity; returnable = false;}

        amount = absoluteAmount * ratio;

		varyTheAmountDrawable = true;

        return returnable;
	}

    public boolean addCapacity() {

        boolean returnable = true;

        absoluteCapacity = Static.preferencesHandler.retrieveNitroCapacity(); // security measure

        absoluteCapacity++;
        if(absoluteCapacity > MAX_NUMBER_OF_CAPACITY_UPGRAGES) { absoluteCapacity = MAX_NUMBER_OF_CAPACITY_UPGRAGES; returnable = false;}

        Static.preferencesHandler.setNitroCapacity(absoluteCapacity);

        capacity = absoluteCapacity * ratio;

        varyCapacity = true;

        return returnable;
    }
	
	
	
	public void decreaseFraction(float deltaTime)
	{


        absoluteAmount -= depletionRate * deltaTime;

        if(absoluteAmount < 0) absoluteAmount = 0;

        amount = absoluteAmount * ratio;



		varyTheAmountDrawable = true;
	}
	
	@Override
	public void update(float deltaTime)
	{
		if(varyTheAmountDrawable)
		{
			if(amount > amountDrawable)
			{
				amountDrawable += VARY_VELOCITY*deltaTime;
				if(amountDrawable > amount)
				{
					amountDrawable = amount;
					varyTheAmountDrawable = false;
				}
			}
			
			else if(amount < amountDrawable)
			{
				amountDrawable -= VARY_VELOCITY*deltaTime;
				if(amountDrawable < amount)
				{
					amountDrawable = amount;
					varyTheAmountDrawable = false;
				}
			}		
		}

        if(varyCapacity)
        {
            if(capacityDrawable < capacity)
            {
                capacityDrawable += VARY_VELOCITY*deltaTime;
                if(capacityDrawable >= capacity)
                {
                    capacityDrawable = capacity;
                    varyCapacity = false;
                }
            }
        }


	}

    @Override
    public void draw() {

       SpriteBatcher batcher = WorldRenderer.batcher;
       TextureShaderProgram texShaderProgram = WorldRenderer.texShaderProgram;

       batcher.drawCarefulSpriteWithPivot(0.2f, bottomPadding + Static.cam.bottomPos, Nitro.DRAW_WIDTH, capacityDrawable, 0.5f, 0, Assets.nitro_empty, texShaderProgram);
       batcher.drawCarefulSpriteWithPivot(0.2f, bottomPadding + Static.cam.bottomPos, Nitro.DRAW_WIDTH, amountDrawable, 0.5f, 0, Assets.nitro_full, texShaderProgram);
    }


    public static int calculateNitroCapacityCost() {

        int x = Static.preferencesHandler.retrieveNitroCapacity();

        return (x - 1)*150;

    }

    public boolean capacityFull() {

        if(absoluteCapacity == MAX_NUMBER_OF_CAPACITY_UPGRAGES) return true;

        return false;
    }
}
