package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.Musca;
import com.antibot.poop.gameobj.Food;
import com.antibot.poop.gameobj.CollidableObject;

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