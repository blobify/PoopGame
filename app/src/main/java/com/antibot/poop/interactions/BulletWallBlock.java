package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.Bullet;
import com.antibot.poop.gameobj.WallBlock;
import com.antibot.poop.gameobj.CollidableObject;

public class BulletWallBlock implements InteractWork{

    @Override
    public void interact(CollidableObject one, CollidableObject two)
    {
        Bullet bullet;
        WallBlock wallblock;

        if(one.objectType == ObjectHandler.INDEX_BULLET)
        {
            bullet = (Bullet) one;
            wallblock = (WallBlock) two;
        }
        else {
            bullet = (Bullet) two;
            wallblock = (WallBlock) one;
        }
        bullet.interactWithWallBlock(wallblock);
    }
}
