package com.antibot.food.interactions;

import com.antibot.food.gameobj.Bomb;
import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.Shooter;

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
