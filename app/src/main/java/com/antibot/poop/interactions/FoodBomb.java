package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.Food;
import com.antibot.poop.gameobj.Bomb;
import com.antibot.poop.gameobj.CollidableObject;

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
