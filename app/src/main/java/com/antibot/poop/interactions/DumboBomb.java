package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.Bomb;
import com.antibot.poop.gameobj.CollidableObject;
import com.antibot.poop.gameobj.Dumboeba;

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
