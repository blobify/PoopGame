package com.antibot.ui;

import com.antibot.food.Assets;
import com.antibot.food.WorldRenderer;
import com.game.framework.gl.TextureRegion;

public class PerkButton extends Button {

    FontLabel headLabel;
    FontLabel footLabel;
    FontLabel costLabel;
    ImageView coinImage;

    public boolean equipped;
    public int coinCost;

    public PerkButton(float width, float height, OnClickListener clickListener, int coinCost) {

        super(width, height, 1.1f, Assets.button_generic_sm, clickListener);
        this.coinCost = coinCost;

    }

    // call this after PerkButton is setPosAndScale to parent
    public void setStrings(String headStr, float headRelY, float headScale, String footStr, float footRelY, float footScale)
    {
        headLabel = new FontLabel(Assets.fnt_playtime,headStr.length());
        headLabel.set(headStr,true,headScale);
        footLabel = new FontLabel(Assets.fnt_purisa,footStr.length());
        footLabel.set(footStr,true,footScale);


        costLabel = new FontLabel(Assets.fnt_purisa,20);
        costLabel.set("x"+coinCost,false,0.7f);

        coinImage = new ImageView(0.4f,0.4f,Assets.coin_region);


        addElement(headLabel, 0, headRelY);
        addElement(footLabel,0,footRelY);
        addElement(costLabel,-width/2+0.5f,-height/2+0.26f);
        addElement(coinImage,-width/2+0.26f,-height/2+0.26f);
    }



    public void setCoinCostAndLabel(int coinCost)
    {
        this.coinCost = coinCost;
        costLabel.set("x" + coinCost,false,costLabel.scale);
    }

    @Override
    public void draw() {
        super.draw();
        if(equipped)
        {
            TextureRegion eqp = Assets.label_equipped;
            WorldRenderer.batcher.drawCarefulSprite(posX,posY,eqp.drawWidth,eqp.drawHeight,25,eqp,WorldRenderer.texShaderProgram);
        }
    }


    public boolean checkOut()
    {
        boolean toReturn = equipped;
        equipped = false;
        return toReturn;
    }


}
