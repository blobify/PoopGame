package com.antibot.food.interactions;


import com.antibot.food.gameobj.Bullet;
import com.antibot.food.gameobj.CollidableObject;

public class BulletBullet implements InteractWork{

    @Override
    public void interact(CollidableObject one, CollidableObject two)
    {
        Bullet bullet1 = (Bullet)one;
        Bullet bullet2 = (Bullet)two;


        bullet1.interactWithBullet(bullet2);
    }
}