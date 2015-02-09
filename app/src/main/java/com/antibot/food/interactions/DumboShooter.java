package com.antibot.food.interactions;

import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.Dumboeba;
import com.antibot.food.gameobj.Shooter;

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
