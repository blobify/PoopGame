package com.antibot.poop.interactions;

import com.antibot.poop.ObjectHandler;
import com.antibot.poop.gameobj.CollidableObject;
import com.antibot.poop.gameobj.Gem;
import com.antibot.poop.gameobj.Musca;

public class MuscaGem implements InteractWork {
    @Override
    public void interact(CollidableObject one, CollidableObject two) {
        Musca musca;
        Gem gem;

        if(one.objectType == ObjectHandler.INDEX_MUSCA)
        {
            musca = (Musca)one;
            gem = (Gem)two;
        }
        else
        {
            musca = (Musca)two;
            gem = (Gem)one;
        }

        musca.interactWithGem(gem);
    }
}
