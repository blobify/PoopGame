package com.antibot.food.interactions;

import com.antibot.food.gameobj.Musca;
import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.Coin;
import com.antibot.food.gameobj.CollidableObject;

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
