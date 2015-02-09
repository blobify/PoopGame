package com.antibot.poop;

import com.antibot.ui.Clickable;
import com.game.framework.Input.KeyEvent;

public abstract class GameStateHandler implements Clickable
{
    public static final char MSG_NULL = 'z';

	public int objType;

    public GameStateHandlerWithSubStates parent;

    public GameStateHandler(int objType)
    {
        this.objType = objType;
    }

    public GameStateHandler(int objType, GameStateHandlerWithSubStates parent)
    {
        this.objType = objType; this.parent = parent;
    }

	public abstract void update(float deltaTime);
	public abstract void draw();

    public abstract void onStart(char msg);

	public abstract void onPause();
	public abstract void onResume();

    //make sure to make this call its substates end if it with substates
	public abstract void end(char msg);

	//if each of the below methods consume the event, they return true else false

	
	public abstract boolean onKeyPressed(KeyEvent event);
	
	public boolean isBackPressed(KeyEvent event)
	{
		if(event.type == KeyEvent.KEY_UP && event.keyCode == 4)
		{
			return true;
		}
		return false;
	}

}


