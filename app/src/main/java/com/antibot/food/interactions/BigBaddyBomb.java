package com.antibot.food.interactions;


import com.antibot.food.gameobj.Bomb;
import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.BigBaddy;

public class BigBaddyBomb implements InteractWork {


    @Override
    public void interact(CollidableObject one, CollidableObject two) {
        BigBaddy bigBaddy;
        Bomb bomb;

        if(one.objectType == ObjectHandler.INDEX_BIG_BADDY)
        {
            bigBaddy = (BigBaddy)one;
            bomb = (Bomb)two;
        }
        else
        {
            bigBaddy = (BigBaddy)two;
            bomb = (Bomb)one;
        }

        bigBaddy.interactWithBomb(bomb);


    }
}
