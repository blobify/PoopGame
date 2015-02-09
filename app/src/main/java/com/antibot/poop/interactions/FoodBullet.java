package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.Food;
import com.antibot.poop.gameobj.Bullet;
import com.antibot.poop.gameobj.CollidableObject;

public class FoodBullet implements InteractWork{

    @Override
    public void interact(CollidableObject one, CollidableObject two)
    {
        Food food;
        Bullet bullet;

        if(one.objectType == ObjectHandler.INDEX_FOOD)
        {
            food = (Food) one;
            bullet = (Bullet) two;
        }
        else {
            food = (Food) two;
            bullet = (Bullet) one;
        }
        food.interactWithBullet(bullet);
    }
}
