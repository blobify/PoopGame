package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.Dumboeba;
import com.antibot.poop.gameobj.Shooter;
import com.antibot.poop.gameobj.CollidableObject;

public class DumboShooter implements InteractWork{

    @Override
    public void interact(CollidableObject one, CollidableObject two)
    {
        Dumboeba dumboeba;
        Shooter shooter;

        if(one.objectType == ObjectHandler.INDEX_DUMB_ENEMY)
        {
            dumboeba = (Dumboeba) one;
            shooter = (Shooter) two;
        }
        else {
            dumboeba = (Dumboeba) two;
            shooter = (Shooter) one;
        }
        dumboeba.interactWithShooter(shooter);
    }
}
