package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.BigBaddy;
import com.antibot.poop.gameobj.CollidableObject;
import com.antibot.poop.gameobj.Musca;

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
