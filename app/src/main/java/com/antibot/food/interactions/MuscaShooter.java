package com.antibot.food.interactions;

import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.gameobj.Musca;
import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.Shooter;

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
