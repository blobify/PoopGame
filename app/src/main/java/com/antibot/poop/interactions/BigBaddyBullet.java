package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.BigBaddy;
import com.antibot.poop.gameobj.Bullet;
import com.antibot.poop.gameobj.CollidableObject;

public class BigBaddyBullet implements InteractWork
{

	@Override
	public void interact(CollidableObject one, CollidableObject two)
	{
		BigBaddy bigBaddy;
		Bullet bullet;
		
		if(one.objectType == ObjectHandler.INDEX_BIG_BADDY)
		{
			bigBaddy = (BigBaddy)one;
			bullet = (Bullet)two;
		}
		else
		{
			bigBaddy = (BigBaddy)two;
			bullet = (Bullet)one;
		}
		
		bigBaddy.interactWithBullet(bullet);
		
	}

}
