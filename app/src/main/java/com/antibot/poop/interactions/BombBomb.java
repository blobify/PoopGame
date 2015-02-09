package com.antibot.poop.interactions;

import com.antibot.poop.gameobj.Bomb;
import com.antibot.poop.gameobj.CollidableObject;

public class BombBomb implements InteractWork {
    @Override
    public void interact(CollidableObject one, CollidableObject two) {
        Bomb bomb1 = (Bomb)one;
        Bomb bomb2 = (Bomb)two;

        bomb1.interactWithBomb(bomb2);

    }
}
