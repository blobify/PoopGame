package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.Bomb;
import com.antibot.poop.gameobj.WallBlock;
import com.antibot.poop.gameobj.CollidableObject;

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
