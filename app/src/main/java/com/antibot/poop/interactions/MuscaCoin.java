package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.Coin;
import com.antibot.poop.gameobj.CollidableObject;
import com.antibot.poop.gameobj.Musca;

public class MuscaCoin implements InteractWork
{

	@Override
	public void interact(CollidableObject one, CollidableObject two)
	{
		Musca musca;
		Coin coin;
		
		if(one.objectType == ObjectHandler.INDEX_MUSCA)
		{
			musca = (Musca)one;
			coin = (Coin)two;
		}
		else
		{
			coin = (Coin)one;
			musca = (Musca)two;
		}
		
		musca.interactWithCoin(coin);
		
	}

}
