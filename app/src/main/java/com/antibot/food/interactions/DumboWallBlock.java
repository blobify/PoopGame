package com.antibot.food.interactions;

import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.gameobj.Dumboeba;
import com.antibot.food.gameobj.WallBlock;
import com.antibot.food.ObjectHandler;

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
