package com.game.framework;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;


public interface Game {
    public Input getInput();

    public FileIO getFileIO();

    public Audio getAudio();

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();

    public Screen getStartScreen();
    
  //  public SharedPreferences getPreferences(String fileName, int mode);
		
	public AssetManager getAssetManager();
	
	public Context getContext();

	
}