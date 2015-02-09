package com.antibot.food.interactions;

import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.BigBaddy;
import com.antibot.food.gameobj.WallBlock;

public class BigBaddyWallBlock implements InteractWork{

    @Override
    public void interact(CollidableObject one, CollidableObject two)
    {
        BigBaddy bigbaddy;
        WallBlock wallblock;

        if(one.objectType == ObjectHandler.INDEX_BIG_BADDY)
        {
            bigbaddy = (BigBaddy) one;
            wallblock = (WallBlock) two;
        }
        else {
            bigbaddy = (BigBaddy) two;
            wallblock = (WallBlock) one;
        }
        bigbaddy.interactWithWallBlock(wallblock);
    }
}
