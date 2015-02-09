package com.antibot.food.interactions;

import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.gameobj.Food;
import com.antibot.food.gameobj.Musca;

public class MuscaFood implements InteractWork{

    @Override
    public void interact(CollidableObject one, CollidableObject two)
    {
        Musca musca;
        Food food;

        if(one.objectType == ObjectHandler.INDEX_MUSCA)
        {
            musca = (Musca) one;
            food = (Food) two;
        }
        else {
            musca = (Musca) two;
            food = (Food) one;
        }
        musca.interactWithFood(food);
    }
}