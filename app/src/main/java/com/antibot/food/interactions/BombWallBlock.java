package com.antibot.food.interactions;

import com.antibot.food.gameobj.Bomb;
import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.gameobj.WallBlock;
import com.antibot.food.ObjectHandler;

public class BombWallBlock implements InteractWork{

    @Override
    public void interact(CollidableObject one, CollidableObject two)
    {
        Bomb bomb;
        WallBlock wallblock;

        if(one.objectType == ObjectHandler.INDEX_BOMB)
        {
            bomb = (Bomb) one;
            wallblock = (WallBlock) two;
        }
        else {
            bomb = (Bomb) two;
            wallblock = (WallBlock) one;
        }
        bomb.interactWithWallBlock(wallblock);
    }
}
