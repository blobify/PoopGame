package com.antibot.ui;


import com.game.framework.gl.TextureRegion;

public class GenericLayout extends UILayout
{
    public GenericLayout(float width, float height, TextureRegion region)
    {
        super(width, height, region);
    }



    @Override
    public boolean onTouchDown(float touchX, float touchY) {

        if(enabled) {

            if(touchedElement == null)  // to prevent multiple elements going down at same time
            {

                for (int i = 0; i < uiLayoutElements.size(); i++) {
                    UILayout l = uiLayoutElements.get(i);
                    boolean b = l.onTouchDown(touchX, touchY);
                    if (b == true) {
                        touchedElement = l;
                        return true;
                    }

                }

            }
        }
        return false;
    }

    @Override
    public boolean onTouchDrag(float touchX, float touchY) {

        if(enabled) {
            for (int i = 0; i < uiLayoutElements.size(); i++) {
                UILayout l = uiLayoutElements.get(i);
                if(l.onTouchDrag(touchX, touchY))
                {
                    return true;
                }
            }
        }
        return false;


    }

    @Override
    public boolean onTouchUp(float touchX, float touchY) {

        if(touchedElement != null)
        {
            boolean toReturn =  touchedElement.onTouchUp(touchX, touchY);
            touchedElement = null;
            return toReturn;
        }
        return false;
    }

    @Override
    public void onParentPosSet() {

    }

    @Override
    public void onParentScaleSet() {

    }


    public void setPos(float posX, float posY)
    {
        this.posX = posX; this.posY = posY;

        for(int i=0; i<allElements.size(); i++)
        {
            UIElement e = allElements.get(i);
            e.onParentPosSet();
        }
    }

    public void setPosX(float posX)
    {
        this.posX = posX;

        for(int i=0; i<allElements.size(); i++)
        {
            UIElement e = allElements.get(i);
            e.onParentPosSet();
        }
    }

    public void setScale(float scale)
    {
        //this.currentScale = this.scale * scale;
        scale(scale);

        for(int i=0; i<allElements.size(); i++)
        {
            UIElement e = allElements.get(i);
            e.onParentScaleSet();
        }

    }

    @Override
    public void draw() {
        drawTextureRegion(0,0);
        super.draw();
    }

    @Override
    public void draw(float delX, float delY) {
        drawTextureRegion(delX,delY);
        super.draw(delX, delY);
    }

    @Override
    public void popUp() {

        for(int i=0; i<uiLayoutElements.size(); i++)
        {
            UILayout layout = uiLayoutElements.get(i);
            layout.popUp();
        }


        /*if(touchedElement != null)
        {
            touchedElement.popUp();
            touchedElement = null;
        }*/
    }


}
