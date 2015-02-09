package com.antibot.ui;

import com.antibot.poop.WorldRenderer;
import com.game.framework.gl.SpriteBatcher;
import com.game.framework.gl.TextureRegion;

public class ImageView extends UIElement {

    private final float width;
    private final float height;
    private final TextureRegion textureRegion;
    private float currentHeight;
    private float currentWidth;

    public ImageView(float width, float height, TextureRegion textureRegion)
    {
        this.width = currentWidth = width; this.height = currentHeight = height; this.textureRegion = textureRegion;

    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void draw() {

        if(visible)
        {
            SpriteBatcher batcher = WorldRenderer.batcher;

            batcher.drawCarefulSprite(posX,posY,currentWidth,currentHeight,textureRegion,WorldRenderer.texShaderProgram);
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
