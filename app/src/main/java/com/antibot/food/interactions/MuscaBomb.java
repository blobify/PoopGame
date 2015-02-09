package com.antibot.food.interactions;


import com.antibot.food.gameobj.Bomb;
import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.gameobj.Musca;
import com.antibot.food.ObjectHandler;

public class MuscaBomb implements InteractWork {

    Musca musca;
    Bomb bomb;


    @Override
    public void interact(CollidableObject one, CollidableObject two) {
        if(one.objectType == ObjectHandler.INDEX_MUSCA)
        {
            musca = (Musca)one;
            bomb = (Bomb)two;
        }
        else
        {
            musca = (Musca)two;
            bomb = (Bomb)one;
        }

        musca.interactWithBomb(bomb);
    }
}
