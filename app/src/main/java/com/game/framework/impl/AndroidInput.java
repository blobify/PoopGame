package com.game.framework.impl;

import java.util.List;

import android.content.Context;

import android.view.View;

import com.antibot.poop.Static;
import com.game.framework.Input;

public class AndroidInput implements Input {    
   
    KeyboardHandler keyHandler;
    TouchHandler touchHandler;
    AccelerometerHandler accelHandler;

    ComboTouchHandler comboTouchHandler;

    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
        
        keyHandler = new KeyboardHandler(view);               
        accelHandler = new AccelerometerHandler(context);

        touchHandler = comboTouchHandler = Static.comboTouchHandler =  new ComboTouchHandler(view, scaleX,scaleY);

        comboTouchHandler.setSingleTouchMode();
        //comboTouchHandler.setMultiTouchMode();
               
    }

    @Override
    public boolean isKeyPressed(int keyCode) {
        return keyHandler.isKeyPressed(keyCode);
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    
    
    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
    
    @Override
    public List<KeyEvent> getKeyEvents() {
        return keyHandler.getKeyEvents();
    }

	@Override
	public float getAccelX()
	{
		return accelHandler.getAccelX();
	}

	@Override
	public float getAccelY()
	{
		return accelHandler.getAccelY();
	}

	@Override
	public float getAccelZ()
	{
		return accelHandler.getAccelZ();
	}
	
	@Override
	public void unregisterAccelerometer()
	{
		accelHandler.unregister();
	}


}
