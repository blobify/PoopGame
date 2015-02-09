package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.CollidableObject;
import com.antibot.poop.gameobj.Dumboeba;
import com.antibot.poop.gameobj.Musca;

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
