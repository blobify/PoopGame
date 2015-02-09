package com.antibot.food.interactions;

import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.gameobj.Musca;
import com.antibot.food.ObjectHandler;
import com.antibot.food.gameobj.Gem;

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
