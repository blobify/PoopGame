package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.BigBaddy;
import com.antibot.poop.gameobj.CollidableObject;
import com.antibot.poop.gameobj.Food;

public class BigBaddyFood implements InteractWork
{
	@Override
	public void interact(CollidableObject one, CollidableObject two)
	{
		BigBaddy bigBaddy;
		Food food;
		
		if(one.objectType == ObjectHandler.INDEX_BIG_BADDY)
		{
			bigBaddy = (BigBaddy)one;
			food = (Food)two;
		}
		else
		{
			bigBaddy = (BigBaddy)two;
			food = (Food)one;
		}
		
		bigBaddy.interactWithFood(food);
		
	}
}
