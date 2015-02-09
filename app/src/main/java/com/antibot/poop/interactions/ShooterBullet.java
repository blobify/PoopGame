package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.Shooter;
import com.antibot.poop.gameobj.Bullet;
import com.antibot.poop.gameobj.CollidableObject;

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