package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.Bullet;
import com.antibot.poop.gameobj.Bomb;
import com.antibot.poop.gameobj.CollidableObject;

public class BulletBomb implements InteractWork{

    @Override
    public void interact(CollidableObject one, CollidableObject two)
    {
        Bullet bullet;
        Bomb bomb;

        if(one.objectType == ObjectHandler.INDEX_BULLET)
        {
            bullet = (Bullet) one;
            bomb = (Bomb) two;
        }
        else {
            bullet = (Bullet) two;
            bomb = (Bomb) one;
        }
        bullet.interactWithBomb(bomb);
    }
}
