package com.antibot.food.interactions;


import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.Bullet;
import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.gameobj.Dumboeba;

public class DumboBullet implements InteractWork
{

	@Override
	public void interact(CollidableObject one, CollidableObject two)
	{
		Dumboeba dumbo;
		Bullet bullet;
		
		
		if(one.objectType == ObjectHandler.INDEX_DUMB_ENEMY)
		{
			dumbo = (Dumboeba )one;
			bullet = (Bullet)two;
		}
		else
		{
			dumbo = (Dumboeba )two;
			bullet = (Bullet)one;
		}
		
		
		dumbo.interactWithBullet(bullet);


	}

}
