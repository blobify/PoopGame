package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.BigBaddy;
import com.antibot.poop.gameobj.Dumboeba;
import com.antibot.poop.gameobj.CollidableObject;

public class BigBaddyDumbo implements InteractWork{

    @Override
    public void interact(CollidableObject one, CollidableObject two)
    {
        BigBaddy bigbaddy;
        Dumboeba dumboeba;

        if(one.objectType == ObjectHandler.INDEX_BIG_BADDY)
        {
            bigbaddy = (BigBaddy) one;
            dumboeba = (Dumboeba) two;
        }
        else {
            bigbaddy = (BigBaddy) two;
            dumboeba = (Dumboeba) one;
        }
        bigbaddy.interactWithDumboeba(dumboeba);
    }
}
