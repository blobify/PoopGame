package com.antibot.food.interactions;

import com.antibot.food.gameobj.Bomb;
import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.gameobj.Dumboeba;
import com.antibot.food.ObjectHandler;

public class DumboBomb implements InteractWork {

    @Override
    public void interact(CollidableObject one, CollidableObject two) {

        Dumboeba dumboeba;
        Bomb bomb;

        if(one.objectType == ObjectHandler.INDEX_DUMB_ENEMY)
        {
            dumboeba = (Dumboeba)one;
            bomb = (Bomb)two;
        }
        else
        {
            dumboeba = (Dumboeba)two;
            bomb = (Bomb)one;
        }

        dumboeba.interactWithBomb(bomb);



    }
}
