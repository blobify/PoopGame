package com.antibot.food.interactions;

import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.Bomb;
import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.gameobj.Food;

public class FoodBomb implements InteractWork{

    @Override
    public void interact(CollidableObject one, CollidableObject two)
    {
        Food food;
        Bomb bomb;

        if(one.objectType == ObjectHandler.INDEX_FOOD)
        {
            food = (Food) one;
            bomb = (Bomb) two;
        }
        else {
            food = (Food) two;
            bomb = (Bomb) one;
        }
        food.interactWithBomb(bomb);
    }
}
