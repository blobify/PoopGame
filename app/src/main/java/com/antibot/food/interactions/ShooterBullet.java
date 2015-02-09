package com.antibot.food.interactions;

import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.Shooter;
import com.antibot.food.gameobj.Bullet;
import com.antibot.food.gameobj.CollidableObject;

public class ShooterBullet implements InteractWork{

    @Override
    public void interact(CollidableObject one, CollidableObject two)
    {
        Shooter shooter;
        Bullet bullet;

        if(one.objectType == ObjectHandler.INDEX_SHOOTER)
        {
            shooter = (Shooter) one;
            bullet = (Bullet) two;
        }
        else {
            shooter = (Shooter) two;
            bullet = (Bullet) one;
        }
        shooter.interactWithBullet(bullet);
    }
}