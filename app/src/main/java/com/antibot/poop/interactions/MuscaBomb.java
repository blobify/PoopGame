package com.antibot.poop.interactions;


import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.Bomb;
import com.antibot.poop.gameobj.CollidableObject;
import com.antibot.poop.gameobj.Musca;

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
