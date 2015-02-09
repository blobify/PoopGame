package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.Dumboeba;
import com.antibot.poop.gameobj.WallBlock;
import com.antibot.poop.gameobj.CollidableObject;

public class DumboWallBlock implements InteractWork{

    @Override
    public void interact(CollidableObject one, CollidableObject two)
    {
        Dumboeba dumboeba;
        WallBlock wallblock;

        if(one.objectType == ObjectHandler.INDEX_DUMB_ENEMY)
        {
            dumboeba = (Dumboeba) one;
            wallblock = (WallBlock) two;
        }
        else {
            dumboeba = (Dumboeba) two;
            wallblock = (WallBlock) one;
        }
        dumboeba.interactWithWallBlock(wallblock);
    }
}
