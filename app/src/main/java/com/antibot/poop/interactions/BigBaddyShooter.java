package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.BigBaddy;
import com.antibot.poop.gameobj.Shooter;
import com.antibot.poop.gameobj.CollidableObject;

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
