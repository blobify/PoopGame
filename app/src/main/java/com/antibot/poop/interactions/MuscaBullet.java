package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.Bullet;
import com.antibot.poop.gameobj.CollidableObject;
import com.antibot.poop.gameobj.Musca;

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
