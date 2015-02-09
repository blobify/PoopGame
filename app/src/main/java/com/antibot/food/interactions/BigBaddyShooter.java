package com.antibot.food.interactions;

import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.BigBaddy;
import com.antibot.food.gameobj.Shooter;

public class BigBaddyShooter implements InteractWork{

    @Override
    public void interact(CollidableObject one, CollidableObject two)
    {
        BigBaddy bigbaddy;
        Shooter shooter;

        if(one.objectType == ObjectHandler.INDEX_BIG_BADDY)
        {
            bigbaddy = (BigBaddy) one;
            shooter = (Shooter) two;
        }
        else {
            bigbaddy = (BigBaddy) two;
            shooter = (Shooter) one;
        }
        bigbaddy.interactWithShooter(shooter);
    }
}
