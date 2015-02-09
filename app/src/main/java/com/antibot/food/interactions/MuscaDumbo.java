package com.antibot.food.interactions;

import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.gameobj.Dumboeba;
import com.antibot.food.gameobj.Musca;

public class MuscaDumbo implements InteractWork
{

	@Override
	public void interact(CollidableObject one, CollidableObject two)
	{
		Musca musca;
		Dumboeba  dumbo;
		
		if(one.objectType == ObjectHandler.INDEX_MUSCA)
		{
			musca = (Musca)one;
			dumbo = (Dumboeba )two;
		}
		else
		{
			musca = (Musca)two;
			dumbo = (Dumboeba )one;
		}
		
		musca.interactWithDumbEnemy(dumbo);
		
	}
	
}
