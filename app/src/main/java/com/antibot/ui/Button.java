package com.antibot.ui;

import com.antibot.food.Assets;
import com.antibot.food.WorldRenderer;
import com.framework.utils.OverlapTester;
import com.game.framework.gl.TextureRegion;

public class Button extends UILayout {

    private static final float SCALE_VEL = 3;
    private final float maxScale;
    private boolean scaleReached;
    private float scaleToReach;

    protected OnClickListener clickListener;
    protected boolean disableOverlayDrawable;  // for drawing overlay

    private int clickCount;


    public Button(float width, float height, float maxScale, TextureRegion texRegion, OnClickListener clickListener) {

        super(width, height, texRegion);
        this.maxScale = maxScale;

        scaleReached = true;
        enabled = true;
        visible = true;
        this.clickListener = clickListener;

        disableOverlayDrawable = false;

    }

    public Button(float maxScale, TextureRegion textureRegion, OnClickListener clickListener)
    {
        this(textureRegion.drawWidth,textureRegion.drawHeight,maxScale,textureRegion,clickListener);
    }

    public void setDisablableOverlayDrawable()
    {
        disableOverlayDrawable = true;
    }


    @Override
    public boolean onTouchDown(float touchX, float touchY) {


        if (visible && enabled) {
            if (OverlapTester.pointInRectangle(touchX, touchY, posX - width / 2, posX + width / 2, posY - height / 2, posY + height / 2)) {

                isDown = true;

                scaleReached = false;
                scaleToReach = maxScale;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchDrag(float posX, float posY) {
        return false;
    }

    @Override
    public boolean onTouchUp(float touchX, float touchY) {
        boolean toReturn = false;

        if (isDown && visible && enabled) {

            if (OverlapTester.pointInRectangle(touchX, touchY, posX - width / 2, posX + width / 2, posY - height / 2, posY + height / 2)) {

                isDown = false;
                click();
                toReturn = true;
            }

            scaleReached = false;
            scaleToReach = 1;
        }
        return toReturn;
    }

    protected void click() {
        if (clickListener != null)
        clickListener.onClick(this);

        clickCount++;
    }

    @Override
    public void onParentPosSet() {

        //this.parentX = parentX; this.parentY = parentY;
        this.posX = parent.posX + relX;
        this.posY = parent.posY + relY;

        //Logger.log("TAAAG", "parent " + parent.posY + " button " + posY + " scale " + currentScale + " height " + currentHeight);


        for (int i = 0; i < allElements.size(); i++) {
            UIElement e = allElements.get(i);
            e.onParentPosSet();
        }
    }

    @Override
    public void onParentScaleSet() {

        scale(parent.currentScale);

        this.posX = parent.posX + relX * parent.currentScale;
        this.posY = parent.posY + relY * parent.currentScale;

        // call on posSet and on scale setPosAndScale on elements here
        for (int i = 0; i < allElements.size(); i++)
        {
            UIElement e = allElements.get(i);
            e.onParentScaleSet();
        }
    }

    @Override
    public void update(float deltaTime) {
        if (visible) // update button only if it is visible
        {
            if (!scaleReached) {
                if (scale < scaleToReach) {
                    scale += SCALE_VEL * deltaTime;
                    if (scale >= scaleToReach) {
                        scaleReached = true;
                        scale = scaleToReach;
                    }

                } else {
                    scale -= SCALE_VEL * deltaTime;
                    if (scale <= scaleToReach) {
                        scaleReached = true;
                        scale = scaleToReach;
                    }

                }
                scale(parent.scale);
                for (int i = 0; i < allElements.size(); i++)
                {
                    UIElement e = allElements.get(i);
                    e.onParentScaleSet();
                }
            }

        }

        super.update(deltaTime);
    }

    @Override
    public void draw() {
        drawTextureRegion(0,0);
        super.draw();

        if(disableOverlayDrawable && !enabled)
        drawDisabledOverlay(0,0);
    }

    @Override
    public void draw(float delX, float delY) {
        drawTextureRegion(delX,delY);
        super.draw(delX,delY);

        if(disableOverlayDrawable && !enabled)
        {
            drawDisabledOverlay(delX, delY);
        }
    }

    protected void drawDisabledOverlay(float delX, float delY)
    {
        WorldRenderer.batcher.drawCarefulSprite(posX + delX, posY + delY, currentWidth, currentHeight, Assets.button_overlay, WorldRenderer.texShaderProgram);
    }

    public interface OnClickListener
    {
        public void onClick(Button button);
    }

    public void popUp()
    {
        isDown = false;
        scaleReached = false;
        scaleToReach = 1;
    }

    public int getClickCount()
    {
        return clickCount;
    }

    public void setClickCount(int count)
    {
        clickCount = count;
    }

}
