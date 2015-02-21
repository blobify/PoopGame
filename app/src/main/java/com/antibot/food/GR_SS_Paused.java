package com.antibot.food;

import com.antibot.ui.FontLabel;
import com.framework.shaderPrograms.TextureShaderProgram;
import com.game.framework.Input.KeyEvent;
import com.game.framework.gl.SpriteBatcher;

public class GR_SS_Paused extends GameStateHandler
{
	FontLabel label;
	
	float alpha = 0;
	private static final float ALPHA_TO_REACH =  0.8f;
	
	public boolean disablable = false;
	
	public GR_SS_Paused(GameRunningHandler parent)
    {
        super(GameRunningHandler.GR_SS_PAUSED,parent);

		//label = new FontLabel(Assets.fnt_playtime, 30);
        label = new FontLabel(Assets.fnt_playtime,12);
        label.set("Game Paused",true,1);
		
	}

	@Override
	public void update(float deltaTime)
	{
		if(!disablable)
		{
			if(alpha != ALPHA_TO_REACH)
			{
				alpha += 2*deltaTime;
				if(alpha >= ALPHA_TO_REACH)
				{
					alpha = ALPHA_TO_REACH;
				}
			}
		}
		else
		{
			if(alpha <= 0 )
			{
                parent.setChangeStateFlag(GameRunningHandler.GR_SS_RUNNING, World.MSG_GR_SS_PAUSED, World.MSG_GR_SS_RUNNING);
				return;
			}
			alpha -= 2*deltaTime;
		}
	}

	@Override
	public void draw()
	{


        SpriteBatcher batcher = WorldRenderer.batcher;
        TextureShaderProgram texShaderProgram = WorldRenderer.texShaderProgram;
		
		//draw overlay thingy
		//change alpha

		batcher.prepareForDrawingAlpha(alpha, texShaderProgram);

        batcher.drawCarefulSprite(Static.TARGET_WIDTH_BY_TWO, Static.cam.bottomPos + Static.targetHeightFixerByTwo, Static.TARGET_WIDTH+1, Static.targetHeightFixer+1, Assets.rect_black, texShaderProgram);

        batcher.finalizeDrawingAlpha(texShaderProgram);

		label.draw(Static.TARGET_WIDTH_BY_TWO,Static.cam.bottomPos + Static.targetHeightFixerByTwo);
		batcher.endBatch(texShaderProgram);
		
	}

	@Override
	public void onStart(char msg)
	{
		disablable = false;
		alpha = 0;
		//label.setPosAndScale("Game Paused", Static.TARGET_WIDTH_BY_TWO,Static.cam.bottomPos + Static.targetHeightFixerByTwo, true, 1f);

	}

	@Override
	public void onPause()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void end(char msg)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onTouchDown(float touchX, float touchY)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouchUp(float touchX, float touchY)
	{
		
		disablable = true;		
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
		if(isBackPressed(event))
		{			
			disablable  = true;  // state will be changed soon
			return true;
		}
		return false;
	}

}
