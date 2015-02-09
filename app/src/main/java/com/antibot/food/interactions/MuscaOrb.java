package com.antibot.food.interactions;

import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.gameobj.Musca;
import com.antibot.food.gameobj.ProtectiveOrb;

public class MuscaOrb implements InteractWork
{

	@Override
	public void interact(CollidableObject one, CollidableObject two)
	{
		Musca musca;
		ProtectiveOrb orb;
		
		if(one.objectType == ObjectHandler.INDEX_MUSCA)
		{
			musca = (Musca)one;
			orb = (ProtectiveOrb)two;
		}
		else
		{
			musca = (Musca)two;
			orb = (ProtectiveOrb)one;
		}
		
		musca.interactWithOrb(orb);
	}

}
