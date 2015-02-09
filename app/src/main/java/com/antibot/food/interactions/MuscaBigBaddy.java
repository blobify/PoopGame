package com.antibot.food.interactions;

import com.antibot.food.gameobj.Musca;
import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.BigBaddy;
import com.antibot.food.gameobj.CollidableObject;

public class MuscaBigBaddy implements InteractWork
{

	@Override
	public void interact(CollidableObject one, CollidableObject two)
	{
		Musca musca;
		BigBaddy bigBaddy;
		
		if(one.objectType == ObjectHandler.INDEX_MUSCA)
		{
			musca = (Musca)one;
			bigBaddy = (BigBaddy)two;
		}
		
		else
		{
			musca = (Musca)two;
			bigBaddy = (BigBaddy)one;
		}
		
		musca.interactWithBigBaddy(bigBaddy);
		
	}

}
