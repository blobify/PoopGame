package com.antibot.food.interactions;

import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.BigBaddy;
import com.antibot.food.gameobj.Food;

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
