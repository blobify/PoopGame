package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.BigBaddy;
import com.antibot.poop.gameobj.WallBlock;
import com.antibot.poop.gameobj.CollidableObject;

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
