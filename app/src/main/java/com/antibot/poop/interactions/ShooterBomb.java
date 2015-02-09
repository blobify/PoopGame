package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.Shooter;
import com.antibot.poop.gameobj.Bomb;
import com.antibot.poop.gameobj.CollidableObject;

public class ShooterBomb implements InteractWork{

    @Override
    public void interact(CollidableObject one, CollidableObject two)
    {
        Shooter shooter;
        Bomb bomb;

        if(one.objectType == ObjectHandler.INDEX_SHOOTER)
        {
            shooter = (Shooter) one;
            bomb = (Bomb) two;
        }
        else {
            shooter = (Shooter) two;
            bomb = (Bomb) one;
        }
        shooter.interactWithBomb(bomb);
    }
}
