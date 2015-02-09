package com.antibot.ui;

import com.antibot.food.WorldRenderer;
import com.game.framework.gl.TextureRegion;

import java.util.ArrayList;

public abstract class UILayout extends UIElement implements Clickable
{
    public ArrayList<UILayout> uiLayoutElements;
    public ArrayList<UIElement> allElements;
    //public ArrayList<Vector2> relativePositions;

    public float width, height;
    public float currentWidth, currentHeight;

    public UILayout touchedElement;

    TextureRegion texRegion;

    boolean isDown;



    public UILayout(float width, float height, TextureRegion texRegion)
    {
        this.width = currentWidth = width;
        this.height = currentHeight = height;
        this.texRegion = texRegion;

        scale = currentScale = 1;

        allElements = new ArrayList<UIElement>();
        uiLayoutElements = new ArrayList<UILayout>();
        // relativePositions = new ArrayList<Vector2>();

    }



    public void addElement(UIElement e, float relX, float relY)
    {
        allElements.add(e);
        // relativePositions.add(new Vector2(relX,relY));

        e.setRel(relX, relY);
        e.parent = this;
        e.onParentPosSet();

        if(e.isClickable()) {
            uiLayoutElements.add((UILayout) e);
        }
    }


    public abstract void popUp();



    @Override
    public void unHide()
    {
        super.unHide();
        for(int i=0; i<allElements.size(); i++)
        {
            UIElement e = allElements.get(i);
            e.unHide();
        }
    }

    @Override
    public void hide()
    {
        super.hide();
        for(int i=0; i<allElements.size(); i++)
        {
            UIElement e = allElements.get(i);
            e.hide();
        }
    }

    @Override
    public boolean isClickable() {
        return true;
    }

    public void enableAllElements()
    {
        enable();
        for(int i=0; i<allElements.size(); i++)
        {
            UIElement e = allElements.get(i);
            e.enable();
        }
    }


    public void disableAllElments()
    {
        disable();
        for(int i=0; i<allElements.size(); i++)
        {
            UIElement e = allElements.get(i);
            e.disable();
        }
    }


    public void draw()
    {
        if(visible)
            for (int i = 0; i < allElements.size(); i++)
            {
                UIElement e = allElements.get(i);
                e.draw();
            }

    }

    @Override
    public void draw(float delX, float delY) {
        if(visible)
        {
            for(int i=0; i<allElements.size(); i++)
            {
                UIElement e = allElements.get(i);
                e.draw(delX,delY);
            }
        }
    }

    protected void scale(float parentScale)
    {
        currentScale = parentScale * scale;
        currentWidth = currentScale * width;
        currentHeight = currentScale * height;
    }

    protected void drawTextureRegion(float delX, float delY)
    {
        if(texRegion != null)
            WorldRenderer.batcher.drawCarefulSprite(posX + delX, posY + delY, currentWidth, currentHeight,texRegion,WorldRenderer.texShaderProgram);
    }


    public void update(float deltaTime)
    {
        // if(enabled)  this was commented out because let them update if needed
        for (int i = 0; i < allElements.size(); i++)
        {
            UIElement e = allElements.get(i);
            e.update(deltaTime);
        }
    }



}
