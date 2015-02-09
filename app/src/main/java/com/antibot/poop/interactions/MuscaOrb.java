package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.CollidableObject;
import com.antibot.poop.gameobj.Musca;
import com.antibot.poop.gameobj.ProtectiveOrb;

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
