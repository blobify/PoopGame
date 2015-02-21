package com.antibot.ui;

import com.antibot.food.WorldRenderer;
import com.framework.utils.Logger;
import com.game.framework.gl.SpriteBatcher;
import com.game.framework.gl.TextureRegion;

public class ImageView extends UIElement {

    private float width;
    private float height;
    private TextureRegion textureRegion;
    private float currentHeight;
    private float currentWidth;

    private boolean invertX, invertY;

    public ImageView(float width, float height, TextureRegion textureRegion)
    {
        this.width = currentWidth = width; this.height = currentHeight = height; this.textureRegion = textureRegion;

        invertX = false;
        invertY = false;

    }

    public void setImage(float width, float height, TextureRegion textureRegion)
    {
        this.width = currentWidth = width; this.height = currentHeight = height; this.textureRegion = textureRegion;

        if(parent != null) {
            onParentScaleSet();
        }
    }

    public void invertX()
    {
        invertX = !invertX;
    }
    public void invertY()
    {
        invertY = !invertY;
    }

    public void unsetInvertX(){invertX = false;}
    public void unsetInvertY(){invertY = false;}


    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void draw() {

        if(visible)
        {
            SpriteBatcher batcher = WorldRenderer.batcher;

            if(!invertX && !invertY) {
                batcher.drawCarefulSprite(posX, posY, currentWidth, currentHeight, textureRegion, WorldRenderer.texShaderProgram);
            }
            else {
                batcher.drawCarefulInvertedSprite(posX, posY, currentWidth, currentHeight, invertX, invertY, textureRegion, WorldRenderer.texShaderProgram);
            }
        }

    }

    @Override
    public void draw(float delX, float delY) {

    }

    @Override
    public void onParentPosSet() {
        this.posX = parent.posX + relX;
        this.posY = parent.posY + relY;
    }

    @Override
    public void onParentScaleSet() {

        currentScale = parent.currentScale * scale;
        currentWidth = currentScale * width;
        currentHeight = currentScale * height;

        this.posX = parent.posX + relX * parent.currentScale;
        this.posY = parent.posY + relY * parent.currentScale;
    }

    @Override
    public boolean isClickable() {
        return false;
    }
}
