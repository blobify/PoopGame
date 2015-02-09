package com.antibot.food.interactions;

import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.Bullet;
import com.antibot.food.gameobj.WallBlock;

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
