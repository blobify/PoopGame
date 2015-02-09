package com.antibot.food;

import com.antibot.food.gameobj.Nitro;
import com.game.framework.Input;

public class World extends GameStateHandlerWithSubStates
{	
	
	public static final int MAIN_MENU = 0, PERK_SELECTION = 1, GAME_RUNNING = 2, GAME_SETTINGS = 3;
	
	public static final int NUMBER_OF_GAME_STATES = 4;

    public static final char MSG_MAIN_MENU = 'm', MSG_PERK = 'k', MSG_GAME_RUNNING = 'g', MSG_SETTINGS = 's', MSG_GR_SS_RUNNING = 'a', MSG_GR_SS_DIALOG = 'd', MSG_GR_SS_PAUSED = 'p';



    //public GameState gameState = GameState.MAIN_MENU;
	
	

	
	//public GameStateHandler[] gameStateHandlerArr;
	//public GameStateHandler currentGameState;

	static Background background = new Background(Assets.background2);


	
	Session session;

    public Nitro nitro;
	
	
	public World()
	{
        super(9999);
		
		createGameStateHandlerArray();
		
		Static.levelGenerator = new LevelGenerator();
        Static.sceneLoader = new SceneLoader();
		
		
		//setting static for easy reference		
		Static.world = this; // the one and only
		Static.session = session = new Session();

        nitro = new Nitro();
	}
	
	private void createGameStateHandlerArray()
	{
		Static.gameStateHandlerArr = subStateArr = new GameStateHandler[NUMBER_OF_GAME_STATES];
		subStateArr[MAIN_MENU] = new MainMenuHandler(this);
        subStateArr[PERK_SELECTION] = new PerkSelectionHandler(this);
		Static.gameRunningHandler = subStateArr[GAME_RUNNING] = new GameRunningHandler(this);
        subStateArr[GAME_SETTINGS] = new GameSettingsHandler(this);


        current = subStateArr[MAIN_MENU];
        current.onStart(MSG_NULL);
	}
	
	
	

	
	public void update(float deltaTime)
	{
        //changeStateIf();

		current.update(deltaTime);

	}
	
	public void draw()
	{
		current.draw();
	}

    @Override
    public void onStart(char msg) {

    }

    public void onResume()
	{
		current.onResume();
	}

    @Override
    public void end(char msg) {

        current.end(msg);

    }

    @Override
    public boolean onKeyPressed(Input.KeyEvent event) {
        return current.onKeyPressed(event);
    }



    public void onPause()
	{
		current.onPause();
	}


    @Override
    public boolean onTouchDown(float touchX, float touchY) {
        return current.onTouchDown(touchX,touchY);
    }

    @Override
    public boolean onTouchDrag(float touchX, float touchY) {
        return current.onTouchDrag(touchX, touchY);
    }

    @Override
    public boolean onTouchUp(float touchX, float touchY) {
        return current.onTouchUp(touchX, touchY);
    }

    @Override
    public void receiveMessage(char someMessage) {

    }
}
