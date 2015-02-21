package com.antibot.ui;

public abstract class UIElement
{

    public float relX, relY, scale = 1, currentScale = 1;

    public float posX, posY;

    public boolean enabled = true, visible = true;

    public UILayout parent;

	public abstract void update(float deltaTime);
	public abstract void draw();
    public abstract void draw(float delX, float delY);

    public void setRel(float relX, float relY)
    {
        this.relX = relX; this.relY = relY;
    }

    public void disable()
    {
        enabled = false;
    }
    public void enable() { enabled = true; }
    public void unHide(){ visible = true; }
    public void hide()
    {
        visible = false;
    }

    protected abstract void onParentPosSet();

    protected abstract void onParentScaleSet();

    protected abstract boolean isClickable();  // or isLayout()



}