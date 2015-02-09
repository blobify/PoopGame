package com.antibot.food.interactions;

import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.BigBaddy;
import com.antibot.food.gameobj.Dumboeba;

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
