package com.antibot.ui;

import com.game.framework.gl.TextureRegion;

public class RadioButton extends Button {

    TextureRegion textureRegionOff;
    TextureRegion textureRegionOn;

    boolean state;


    public RadioButton(float width, float height, float maxScale, TextureRegion textureRegionOff, TextureRegion textureRegionOn, OnClickListener clickListener) {
        super(width, height, maxScale, textureRegionOff, clickListener);

        state = false;

        this.textureRegionOn = textureRegionOn;
        this.textureRegionOff = textureRegionOff;
    }

    public RadioButton(float maxScale, TextureRegion texRegionUp, TextureRegion textureRegionDown, OnClickListener clickListener)
    {
        this(texRegionUp.drawWidth,texRegionUp.drawHeight,maxScale,texRegionUp,textureRegionDown,clickListener);
    }


    @Override
    protected void click() {
        super.click();

        state = !state;

        if(!state)
        {
            texRegion = textureRegionOff;
        }
        else
        {
            texRegion = textureRegionOn;
        }
    }

    @Override
    public void popUp() {
        super.popUp();

        state = false;
        texRegion = textureRegionOff;
    }

    public void popIn()
    {
        state = true;
        texRegion = textureRegionOn;
    }

    public boolean getState()
    {
        return state;
    }
}
