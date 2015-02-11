package com.antibot.food;

import java.util.LinkedList;

/**
 * 
 * manages variables per session of the game
 */
public class Session
{
    public static final int MAX_GEM_COUNT = 5;

	public int coinsCollectedInSession, gemsCollectedInSession;

    public int continueGemAmount;
	
	public float distanceTravelledInSession;

	public boolean gameAreaTouched;
	
	public KeyPressHandler keyPressHandler;

    public boolean headStart,protectiveOrb,longerNitro,muchFood;

    public float movementSensi = 1;

	public Session()
	{
		keyPressHandler = new KeyPressHandler();
	}
	

    public void setSessionPerks(boolean headStart, boolean protectiveOrb, boolean longerNitro, boolean muchFood)
    {
        this.headStart = headStart;
        this.protectiveOrb = protectiveOrb;
        this.longerNitro = longerNitro;
        this.muchFood = muchFood;
    }
	
	public void resetSession()
	{
		coinsCollectedInSession = 0;
		distanceTravelledInSession = 0;
        gemsCollectedInSession = 0;
        continueGemAmount = 1;

        headStart = false;
        protectiveOrb = false;
        muchFood = false;
        longerNitro = false;

        keyPressHandler.reset();
	}

    public void increaseGemAmount()
    {
        continueGemAmount++;
        if(continueGemAmount>MAX_GEM_COUNT) continueGemAmount=MAX_GEM_COUNT;
        if(continueGemAmount<1)continueGemAmount=1; //security measure
    }




    public void addDistance(float delY) {
        distanceTravelledInSession += delY;
    }

	
	public class KeyPressHandler
	{
		public boolean leftPressed;
		public boolean rightPressed;

        public void reset()
        {
            leftPressed = false;
            rightPressed = false;
        }
		
	}
	
}
