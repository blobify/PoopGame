package com.antibot.poop.interactions;


import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.Bullet;
import com.antibot.poop.gameobj.CollidableObject;
import com.antibot.poop.gameobj.Dumboeba;

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
