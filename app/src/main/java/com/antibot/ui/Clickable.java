package com.antibot.ui;

public interface Clickable {

    public boolean onTouchDown(float touchX, float touchY);
    public boolean onTouchDrag(float touchX, float touchY);
    public boolean onTouchUp(float touchX, float touchY);
}
