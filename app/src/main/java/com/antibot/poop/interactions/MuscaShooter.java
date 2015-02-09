package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.CollidableObject;
import com.antibot.poop.gameobj.Musca;
import com.antibot.poop.gameobj.Shooter;

public class MuscaShooter implements InteractWork
{
	@Override
	public void interact(CollidableObject one, CollidableObject two)
	{
		Musca musca;
		Shooter shooter;
		
		if(one.objectType == ObjectHandler.INDEX_MUSCA)
		{
			musca = (Musca)one;
			shooter = (Shooter)two;
		}
		else
		{
			musca = (Musca)two;
			shooter = (Shooter)one;
		}
		
		
		musca.interactWithShooter(shooter);
	}
}
