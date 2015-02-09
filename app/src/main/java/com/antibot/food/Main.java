package com.antibot.food;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Configuration;

import com.antibot.poop.R;
import com.game.framework.Screen;
import com.game.framework.gl.TextureRegion;
import com.game.framework.impl.GLGame;

import java.util.Locale;

public class Main extends GLGame
{
    public volatile boolean firstTimeCreate = true;

	@Override
	public Screen getStartScreen()
	{
		return new LoadingScreen(this);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
        Assets.loadTexturesAndShaderPrograms(this);

		if(firstTimeCreate)
		{
            firstTimeCreate = false;
            Static.uiThreadHandler = new UIThreadHandler();
            Static.preferencesHandler = new PreferencesHandler(getSharedPreferences(getString(R.string.shared_pref_file), MODE_PRIVATE));
            localeFix();

            Assets.loadingScreenRegion = new TextureRegion(Assets.atlas, 963, 487, 50, 43);
		}


		
		super.onSurfaceCreated(gl, config);

    }

    private void localeFix()
    {
        Locale locale = new Locale("US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

    }
	
	@Override
	public Context getContext()
	{
		return this;
	}

	@Override
	protected void onDestroy()
	{			
		super.onDestroy();

		getInput().unregisterAccelerometer();

        //optional
        if(Static.scenesFile != null && !Static.LEVEL_GEN_MODE)
            Static.scenesFile.delete();
	}


	
	
}