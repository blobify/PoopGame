package com.antibot.food.interactions;

import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.Bullet;
import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.gameobj.Musca;

public class MuscaBullet implements InteractWork
{

	@Override
	public void interact(CollidableObject one, CollidableObject two)
	{
		Musca musca;
		Bullet bullet;
		
		if(one.objectType == ObjectHandler.INDEX_MUSCA)
		{
			musca = (Musca)one;
			bullet = (Bullet)two;
		}
		else
		{
			musca = (Musca)two;
			bullet = (Bullet)one;
		}
		
		musca.interactWithBullet(bullet);
		
	}

}
