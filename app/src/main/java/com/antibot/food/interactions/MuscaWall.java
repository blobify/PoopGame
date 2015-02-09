package com.antibot.food.interactions;

import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.gameobj.Musca;
import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.WallBlock;

public class MuscaWall implements InteractWork
{

	@Override
	public void interact(CollidableObject one, CollidableObject two)
	{
		Musca musca;
		WallBlock block;
		
		if(one.objectType == ObjectHandler.INDEX_MUSCA)
		{
			musca = (Musca)one;
			block = (WallBlock)two;
		}
		else
		{
			musca = (Musca)two;
			block = (WallBlock)one;
		}
		
		musca.interactWithWallBlock(block);
		
	}
	
}
