package com.antibot.food;

import java.io.File;
import java.util.Random;

import com.antibot.food.gameobj.Musca;
import com.game.framework.impl.ComboTouchHandler;
import com.game.framework.impl.GLGame;
import com.game.math.Vector2;

public class Static
{
	public static final float TARGET_HEIGHT = 12.8f;
	public static final float TARGET_WIDTH = 7.2f;
	public static final float TARGET_WIDTH_BY_TWO = TARGET_WIDTH/2;
	
	public static int screenWidth;
	public static int screenHeight;
	
	
	public static float targetHeightFixer;
	
	public static float touchXCalculationReducer;
	public static float touchYCalculationReducer;
	
	
	
	public static Random rand = new Random();
	public static Vector2 tempVector = new Vector2();
	public static float targetHeightFixerByTwo;
	
	
	//public static final float slightVariations[] = {0.26165256f, 0.056761283f, -0.08361581f, 0.08262708f, -0.102715716f, -0.19544418f, -0.27788067f, -0.2519098f, 0.21122395f, -0.13209786f, 0.038633913f, -0.03941576f, -0.056062568f, 0.12834458f, -0.10440252f, -0.10995827f, 0.101197176f, 0.2591278f, -0.077955455f, -0.198417f, 0.16681512f, -0.08609092f, -0.062432583f, -0.22444159f, -0.09542755f, 0.050787415f, -0.14706148f, -0.25895488f, 0.16411558f, 0.14167368f};
	

	public static GLGame game;
	public static World world;
	public static ObjectHandler objHandler;
	public static Camera cam;
	public static GameStateHandler[] gameStateHandlerArr;
	public static Session session;
	public static UIThreadHandler uiThreadHandler;  // initialized in onSurfaceCreated of Main
	public static PreferencesHandler preferencesHandler;
    public static GameStateHandler gameRunningHandler;
    public static Musca musca;

    private volatile static boolean dimensionsSet = false;




    public static LevelGenerator levelGenerator;
    public static SceneLoader sceneLoader;
    public static final String SCENE_FILE_NAME = "scenes";

    public static volatile boolean sceneFileStoredInInternalDir = true;
    public static File scenesFile;
    public static boolean sceneFileNotFound = true;
    public static final boolean LEVEL_GEN_MODE = true;  //dev mode

    public static ComboTouchHandler comboTouchHandler;

    public static void setAllDimentionalConstants(int width, int height)
	{
        if(!dimensionsSet) {

            dimensionsSet = true;

            screenWidth = width;
            screenHeight = height;

            targetHeightFixer = (TARGET_WIDTH * height / width);
            targetHeightFixerByTwo = targetHeightFixer / 2;

            touchXCalculationReducer = (float) TARGET_WIDTH / screenWidth;    // multiply with event.x
            touchYCalculationReducer = targetHeightFixer / screenHeight;    // multiply with screenHeight-event.y

        }
	}
	

	
}
