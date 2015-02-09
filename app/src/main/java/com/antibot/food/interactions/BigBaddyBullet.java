package com.antibot.food.interactions;

import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.BigBaddy;
import com.antibot.food.gameobj.Bullet;

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
