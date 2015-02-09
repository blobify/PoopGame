package com.antibot.food.interactions;

import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.gameobj.Food;
import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.Bullet;

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
