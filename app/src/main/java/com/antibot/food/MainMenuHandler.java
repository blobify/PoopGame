package com.antibot.food;

import com.antibot.ui.Button;
import com.antibot.ui.GenericLayout;
import com.antibot.ui.OvershootInterpolator;
import com.antibot.ui.RadioButton;
import com.game.framework.Input.KeyEvent;

public class MainMenuHandler extends GameStateHandler
{
	public static final int  STARTED = 1, ENDED = 2, REWIND = 3;

	public int mainMenuAnimState;
	
	//public TextLabel label;
	//public TextLabel startLabel;
	public OvershootInterpolator interpolator;

	//UILayout2 uiLayout;
    GenericLayout genericLayout;

    public static final float EXTREME_LEFT_POS = -3, WORKING_POS = Static.TARGET_WIDTH_BY_TWO;

    int stateChangeTarget;




	public MainMenuHandler(World parent)
	{
        super(World.MAIN_MENU,parent);
		
		createInterpolator();		
		
		constructUILayout();
		
		
		
	}

    private void constructUILayout()
    {
        Button button;

        //UILayout2.OnClickListener onStartButtonClickListener = new UILayout2.OnClickListener()
        Button.OnClickListener onStartButtonClickListener = new Button.OnClickListener()
        {
            @Override
            public void onClick(Button b)
            {
                startStateChangeAnimation();
                stateChangeTarget = World.PERK_SELECTION;

            }
        };



        button = new Button(1.1f,Assets.button_play,onStartButtonClickListener);

        //FontLabel label = new FontLabel(Assets.fnt_purisa,30);
        //label.set("start",true,0.7f);



        genericLayout = new GenericLayout(Static.TARGET_WIDTH, Static.targetHeightFixer, Assets.rect_white);
        genericLayout.setPos(Static.TARGET_WIDTH_BY_TWO, Static.targetHeightFixerByTwo);
        genericLayout.addElement(button,0,-2);

        //button.addElement(label,0,0);

        //PerkButton perkButton = new PerkButton(2,2,null,100);
        //genericLayout.addElement(perkButton,2,2);

       // perkButton.setStrings("yow",0.5f,0.8f,"do it\nfegit",0f,0.5f);


        Button.OnClickListener onTestButtonClickListener = new Button.OnClickListener() {

            @Override
            public void onClick(Button button) {

                Static.preferencesHandler.setGemAmount(50);  // delete this asap

                Static.preferencesHandler.setCoinAmount(1000);

            }
        };

        RadioButton testButton = new RadioButton(0.4f,0.4f,1.1f,Assets.radio_button_off,Assets.radio_button_on,onTestButtonClickListener);

        genericLayout.addElement(testButton,0,2.5f);


        Button.OnClickListener onSettingsButtonClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(Button button) {
                startStateChangeAnimation();
                stateChangeTarget = World.GAME_SETTINGS;
            }
        };

        Button buttonSettings = new Button(0.5f,0.5f,1.1f,Assets.button_settings,onSettingsButtonClickListener);
        genericLayout.addElement(buttonSettings, -2f, -Static.targetHeightFixerByTwo + 0.7f);


    }

    private void startStateChangeAnimation()
    {
        mainMenuAnimState = STARTED;
        genericLayout.disable();
        interpolator.set(WORKING_POS, EXTREME_LEFT_POS, 5,STARTED,2);
        Assets.coin.play(1);
    }


    @Override
	public boolean onTouchDown(float touchX, float touchY)
	{
		return genericLayout.onTouchDown(touchX, touchY);
	}
	
	@Override
	public boolean onTouchUp(float touchX, float touchY)
	{
		return genericLayout.onTouchUp(touchX, touchY);
	}
	
	private void createInterpolator()
	{
		interpolator = new OvershootInterpolator(new OvershootInterpolator.InterpolationCompleteListener()
		{			
			@Override
			public void onInterpolationComplete(int animId)
			{
				mainMenuAnimState = ENDED;
				
				if(animId == STARTED)
				{
					genericLayout.disable();

                    parent.setChangeStateFlag(stateChangeTarget);

				}
				else if(animId == REWIND)
				{					
					genericLayout.enable();
				}
			}
		});


	}
	
	
		
	public void update(float deltaTime)
	{
		genericLayout.update(deltaTime);

		if(mainMenuAnimState == STARTED || mainMenuAnimState == REWIND)
		{
            final float interpolation = interpolator.getInterpolation(deltaTime);

			genericLayout.setPosX(interpolation);
		}
		//Logger.log("IN MAIN MENU", "IN MAIN MENUUU!!");
	}
	
	

	@Override
	public void draw()
	{
		WorldRenderer.presentMainMenu(genericLayout);
	}

	

	@Override
	public void onPause()
	{
		//do nothing :D
		
	}

	
	
	@Override
	public void onStart(char msg)
	{
		Static.game.activityQuittableFlag = true;
		
		mainMenuAnimState = REWIND;

        genericLayout.setPosX(EXTREME_LEFT_POS);
		interpolator.set(EXTREME_LEFT_POS,WORKING_POS,3,REWIND,2);
        genericLayout.unHide();

        
	}

	@Override
	public void end(char msg)
	{
		Static.game.activityQuittableFlag = false;
		genericLayout.hide();
        genericLayout.disable();
	}

	@Override
	public void onResume()
	{
		// nothing to do as of now
	}

	@Override
	public boolean onTouchDrag(float touchX, float touchY)
	{
		
		return false;
	}

	@Override
	public boolean onKeyPressed(KeyEvent event)
	{		
		if(isBackPressed(event))
		{
			
		}
		return false;
	}

}
