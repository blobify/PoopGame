package com.antibot.food;

import com.antibot.ui.Button;
import com.antibot.ui.GenericLayout;
import com.game.framework.Input.KeyEvent;

public class GR_SS_Running extends GameStateHandler
{

	Camera cam;

	GenericLayout layout;

    public static final float RESET_TO_BASE_THRESHOLD = 1024;
	
	public GR_SS_Running(GameRunningHandler parent, Camera cam)
	{
        super(GameRunningHandler.GR_SS_RUNNING,parent);

		this.cam = cam;


		
		createUILayout();
	}

	
	private void createUILayout()
	{
		Button.OnClickListener listener = new Button.OnClickListener()
		{
			
			@Override
			public void onClick(Button b)
			{

                parent.setChangeStateFlag(GameRunningHandler.GR_SS_PAUSED);
			}
		};
		
		//Button pauseButton = new Button(0.7f, 0.7f, 23, 1.1f, listener, Assets.pause_button);
        Button pauseButton = new Button(0.6f,0.6f,1.1f,Assets.pause_button,listener);


		
		layout = new GenericLayout(Static.TARGET_WIDTH,Static.targetHeightFixer,null);
        layout.setPos(Static.TARGET_WIDTH_BY_TWO,Static.targetHeightFixerByTwo);
        layout.addElement(pauseButton,Static.TARGET_WIDTH_BY_TWO-0.33f,-Static.targetHeightFixerByTwo+0.33f);

	}
	
	@Override
	public void update(float deltaTime)
	{		


        /*GameRunningHandler gameRunningHandler = (GameRunningHandler)parent;

        gameRunningHandler.updateObjectsAndCamera(deltaTime);
        */

		
		if(cam.bottomPos > RESET_TO_BASE_THRESHOLD )
		{
			cam.bottomPos -= RESET_TO_BASE_THRESHOLD;
            Static.objHandler.setEverySingleObjectBackToBase(RESET_TO_BASE_THRESHOLD);
			World.background.bottomCameraPos = cam.bottomPos;
		}

        Static.levelGenerator.update(deltaTime);
		
		layout.update(deltaTime);
	}

	@Override
	public void draw()
	{
		WorldRenderer.presentRunning();
		
		WorldRenderer.batcher.beginBatch(); //TODO optimizable
		
		layout.draw(0, Static.cam.bottomPos);

		WorldRenderer.batcher.endBatch(WorldRenderer.texShaderProgram); 
	}

	@Override
	public void onStart(char msg)
	{
        if(msg != World.MSG_GR_SS_PAUSED)  // if starting from dialog or starting from parent itself
        {


            World.background.bottomCameraPos = 0;


            Static.objHandler.reset();

            Static.cam.bottomPos = 0;
            Static.cam.reset();


        }

        if(msg == World.MSG_GR_SS_DIALOG)  // if continue was clicked in dialog,
        {
            //Logger.log("MUSCA CLEANED!", "MUSCA CLEANED!!");
            Static.musca.clean(Static.TARGET_WIDTH / 2, 0);
        }



	}

	@Override
	public void onPause()
	{
        parent.setChangeStateFlag(GameRunningHandler.GR_SS_PAUSED);
	}

	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void end(char msg)
	{
        Static.session.gameAreaTouched = false;

        if(msg == World.MSG_GR_SS_DIALOG)
        {
            Static.objHandler.killAllObjectsFromAllLists();
        }

	}

	@Override
	public boolean onTouchDown(float touchX, float touchY)
	{
		if(!layout.onTouchDown(touchX, touchY))
		{
		Static.session.gameAreaTouched = true;
		}
		return true;
	}

	@Override
	public boolean onTouchUp(float touchX, float touchY)
	{
		if(!layout.onTouchUp(touchX, touchY)) {
            Static.session.gameAreaTouched = false;
        }
		
		//objectHandler.addFood(Static.randomAccessFile.nextFloat()*Static.TARGET_WIDTH, cam.bottomPos + Static.TARGET_HEIGHT - Static.randomAccessFile.nextFloat()*4);
		//Static.objHandler.addFood(Static.randomAccessFile.nextFloat()*Static.TARGET_WIDTH, cam.bottomPos + Static.TARGET_HEIGHT - Static.randomAccessFile.nextFloat()*4);
		
		//Static.objHandler.addDumboGroup(Static.TARGET_WIDTH/3,cam.bottomPos + Static.TARGET_HEIGHT - Static.randomAccessFile.nextFloat()*4);
	
		//for(int u=0; u<2; u++)
		//Static.objHandler.addBigBaddy(Static.randomAccessFile.nextFloat()*Static.TARGET_WIDTH,cam.bottomPos + Static.TARGET_HEIGHT - Static.randomAccessFile.nextFloat()*4);
		//Static.objHandler.createCoin(Static.randomAccessFile.nextFloat()*Static.TARGET_WIDTH,cam.bottomPos + Static.TARGET_HEIGHT - Static.randomAccessFile.nextFloat()*4);
		//Static.objHandler.addShooter(Static.randomAccessFile.nextFloat()*Static.TARGET_WIDTH,cam.bottomPos + Static.TARGET_HEIGHT - Static.randomAccessFile.nextFloat()*4);
		//objectHandler.addOrb(Static.randomAccessFile.nextFloat()*Static.TARGET_WIDTH,cam.bottomPos + Static.TARGET_HEIGHT - Static.randomAccessFile.nextFloat()*4);
		//objectHandler.addWallBlock(Static.randomAccessFile.nextFloat()*Static.TARGET_WIDTH,cam.bottomPos + Static.TARGET_HEIGHT - Static.randomAccessFile.nextFloat()*4);
	
		return true;
	}

	@Override
	public boolean onTouchDrag(float touchX, float touchY)
	{
		// TODO Auto-generated method stub
		return false;
	}

	

	@Override
	public boolean onKeyPressed(KeyEvent event)
	{
		if(event.keyCode == 29)
		{
			if(event.type == KeyEvent.KEY_DOWN)
			{
				Static.session.keyPressHandler.leftPressed = true;
			}
			else
			{
				Static.session.keyPressHandler.leftPressed = false;
			}
			return true;
		}
		else if(event.keyCode == 32)
		{
			if(event.type == KeyEvent.KEY_DOWN)
			{
				Static.session.keyPressHandler.rightPressed = true;
			}
			else
			{
				Static.session.keyPressHandler.rightPressed = false;
			}
			return true;
		}
		
		
		return false;
	}


}
