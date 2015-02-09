package com.antibot.poop.interactions;


import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.BigBaddy;
import com.antibot.poop.gameobj.Bomb;
import com.antibot.poop.gameobj.CollidableObject;

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
