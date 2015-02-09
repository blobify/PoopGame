package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.CollidableObject;
import com.antibot.poop.gameobj.Musca;
import com.antibot.poop.gameobj.WallBlock;

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
