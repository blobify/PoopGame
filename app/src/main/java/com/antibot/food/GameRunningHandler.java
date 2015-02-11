package com.antibot.food;

import com.antibot.food.gameobj.Musca;
import com.antibot.food.gameobj.Nitro;
import com.framework.utils.Logger;
import com.game.framework.Input.KeyEvent;

public class GameRunningHandler extends GameStateHandlerWithSubStates
{

	public ObjectHandler objectHandler;
	
	public Camera cam;	
	
	//private GameStateHandler[] subStateArr;
	public static final int GR_SS_RUNNING = 0, GR_SS_PAUSED = 1, GR_SS_DIALOG_QUIT = 2 , GR_SS_DIALOG_END = 3;// GR_SS = GAME RUNNING SUB STATE
	private static final int NO_OF_SUB_STATES = 4;


	//private GameStateHandler currentSubState;



	public GameRunningHandler(final World parent)
	{
        super(World.GAME_RUNNING,parent);
		Static.objHandler = objectHandler = new ObjectHandler();
		Static.cam = cam = new Camera();



        Static.musca.setMuscaDeathListener(new Musca.MuscaDeathListener() {
            @Override
            public void onMuscaDeath() {
                setChangeStateFlag(GR_SS_DIALOG_END,World.MSG_GR_SS_DIALOG_END,World.MSG_GR_SS_DIALOG_END);
            }
        });

		
		createSubStateArr();
	}
	
	private void createSubStateArr()
	{
		subStateArr = new GameStateHandler[NO_OF_SUB_STATES];
		
		subStateArr[GR_SS_RUNNING] = current = new GR_SS_Running(this,cam);
		subStateArr[GR_SS_PAUSED] = new GR_SS_Paused(this);
		subStateArr[GR_SS_DIALOG_END] = new GR_SS_DialogEnd(this);
        subStateArr[GR_SS_DIALOG_QUIT] = new GR_SS_DialogQuit(this);
		
	}
	
	@Override
	public void update(float deltaTime)
	{
        //changeStateIf();


        if(current.objType != GR_SS_PAUSED && current.objType != GR_SS_DIALOG_QUIT)  //dont update when paused :|
            updateObjectsAndCamera(deltaTime);

		current.update(deltaTime);

        Static.world.nitro.update(deltaTime);
	}


    public void updateObjectsAndCamera(float deltaTime) {

        Static.objHandler.update(deltaTime);
        cam.update(deltaTime, Static.musca.pos.y);
        World.background.shift(cam.bottomPos);

    }


	@Override
	public void draw()
	{
		current.draw();
	}

	@Override
	public void onStart(char msg)
	{
        Static.session.movementSensi = Static.preferencesHandler.getMovementSensiAmount();

        setPerks();

		current = subStateArr[GR_SS_RUNNING];

        current.onStart(World.MSG_GAME_RUNNING);

        Static.session.resetSession();

        Static.levelGenerator.reset();

    }

    private void setPerks()
    {
        Musca musca = Static.musca;

        Session session = Static.session;
        if(session.protectiveOrb) {
            Static.musca.enableOrb(0);  //TODO unset

        }

        if(session.muchFood) {}
        if(session.longerNitro) {Static.world.nitro.depletionRate = Nitro.DEPLETION_RATE_REDUCED;}
        else {Static.world.nitro.depletionRate = Nitro.DEPLETION_RATE_NORMAL;}

        if(session.headStart)
        {
            musca.setSpeedUpdateHeadStart();
        }
        else
        {
            musca.setSpeedUpdateNormal();
        }
    }

	@Override
	public void onPause()
	{		
		current.onPause();
				
	}

	@Override
	public void end(char msg)
	{
		current.end(msg);


		//Static.musca.reset(Static.TARGET_WIDTH/2,0);
		World.background.bottomCameraPos = 0;

		cam.bottomPos = 0;

        //if(msg == World.MSG_MAIN_MENU) {
        cam.reset();
        //}


		objectHandler.disableObjects();

		World.background.reset(); //delete this or do something about this

        Static.world.nitro.resetNitro();
        Static.musca.clean(Static.TARGET_WIDTH / 2, 0);


	}

	@Override
	public boolean onTouchDown(float touchX, float touchY)
	{
		return current.onTouchDown(touchX, touchY);
		
	}

	@Override
	public boolean onTouchUp(float touchX, float touchY)
	{
		return current.onTouchUp(touchX, touchY);
		
	}

	@Override
	public void onResume()
	{
		
	}

	@Override
	public boolean onTouchDrag(float touchX, float touchY)
	{
		
		return false;
	}


	@Override
	public boolean onKeyPressed(KeyEvent event)
	{
		/*if(current.onKeyPressed(event))
		{
			return true;
		}
		else
		{


			if(isBackPressed(event) && current.objType == GR_SS_RUNNING)
			{
                //parent.setChangeStateFlag(World.MAIN_MENU, World.MSG_MAIN_MENU, World.MSG_MAIN_MENU);
                //^dont quit game directly but
                setChangeStateFlag(GR_SS_DIALOG_QUIT,World.MSG_GR_SS_DIALOG_QUIT,World.MSG_GR_SS_DIALOG_QUIT);
                //^do this instead

				return true;
			}
		}*/


        return current.onKeyPressed(event);

		//return false;
	}

    @Override
    public void receiveMessage(char someMessage) {

    }




    /*@Override
    public void changeStateIf() {

        if(stateChangeFlag)
        {
            if(indexToChange < 0)
            {
                if(indexToChange == -1)
                {
                    parent.setChangeStateFlag(World.MAIN_MENU);
                }
            }
            else
            {
                super.changeStateIf();
            }
            stateChangeFlag = false;
        }

    }*/
}
