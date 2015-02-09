package com.antibot.poop;

import java.util.List;

import com.framework.utils.FPSCounter;
import com.framework.utils.Logger;
import com.game.framework.Input;
import com.game.framework.Screen;
import com.game.framework.Input.KeyEvent;
import com.game.framework.impl.GLGame;

public class StartingScreen extends Screen
{
	
	//WorldRenderer worldRenderer;
	World world;
	FPSCounter fpsCounter;
	GLGame game;
	
	float touchX, touchY;
	
	public StartingScreen(GLGame game)
	{		
		super(game); 
		this.game = game;
		//worldRenderer = new WorldRenderer(1500);
		world = new World();
		fpsCounter = new FPSCounter();
		
		
	}	

	@Override
	public void update(float deltaTime)
	{
		world.update(deltaTime);

		touchify();
				
		keyify();



    }

    public void touchify()
    {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++)
        {
            Input.TouchEvent event = touchEvents.get(i);

            touchX = event.x * Static.touchXCalculationReducer;	//(event.x / (float) Static.GameStateHandlerWidth) * Static.TARGET_WIDTH;
            touchY = (Static.screenHeight - event.y) * Static.touchYCalculationReducer; //(1 - event.y / (float) Static.GameStateHandlerHeight) * Static.targetHeightFixer; //  //

            if(event.type == Input.TouchEvent.TOUCH_DOWN)
            {
                //mainMenuHandler.onTouchDown(touchX, touchY);
                world.onTouchDown(touchX, touchY);

            }
            else if(event.type == Input.TouchEvent.TOUCH_DRAGGED)
            {
                world.onTouchDrag(touchX, touchY);
            }
            else if(event.type == Input.TouchEvent.TOUCH_UP)
            {
                world.onTouchUp(touchX, touchY);
            }

        }



    }

	private void keyify()
	{
		List<KeyEvent> keyEvents = game.getInput().getKeyEvents();
		
		int len = keyEvents.size();
		for(int i=0; i<len; i++)
		{
			KeyEvent event = keyEvents.get(i);
			world.onKeyPressed(event);
		}
	}

	@Override
	public void present(float deltaTime)
	{

		world.draw();

	}
		

	@Override
	public void pause()
	{
		world.onPause();
	}

	@Override
	public void resume()
	{
		WorldRenderer.onResume(game);		
		world.onResume();		
	}

	@Override
	public void dispose()
	{
		
	}

	@Override
	public void onSurfaceChanged(int width, int height)
	{		
		WorldRenderer.onSurfaceChanged(width, height);
	}

	
	
	
}
