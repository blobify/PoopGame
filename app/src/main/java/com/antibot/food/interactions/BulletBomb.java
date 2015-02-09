package com.antibot.food.interactions;

import com.antibot.food.gameobj.Bomb;
import com.antibot.food.gameobj.Bullet;
import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.ObjectHandler;

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
