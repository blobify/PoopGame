package com.game.framework.impl;

import java.util.Locale;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.app.ActivityManager;

import android.content.Context;

import android.content.SharedPreferences;
import android.content.pm.ConfigurationInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.antibot.poop.R;
import com.antibot.poop.Static;
import com.framework.utils.Logger;
import com.game.framework.Audio;
import com.game.framework.FileIO;
import com.game.framework.Game;
import com.game.framework.Input;
import com.game.framework.Screen;

public abstract class GLGame extends Activity implements Game, Renderer
{


    enum GLGameState
	{
		Running, Paused
	}

	public GLSurfaceView glView;

	Audio audio;
	Input input;
	FileIO fileIO;

	Screen screen;
	GLGameState state;

	Object stateChanged = new Object();

	public boolean activityQuittableFlag = true;

	public volatile boolean surfaceCreated = false;

	private static final float MAX_DELTA_TIME = 0.025f;

	long startTime = 0;

	boolean renderSet = false;
	
	public Handler handler;
	

    public GameUIElements gameUIElements;


    @Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		glView = new GLSurfaceView(this);

		if (supportsEs2())
		{
			// if(Logger.LOG) // TODO
			{
				//glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
			}
			glView.setEGLContextClientVersion(2);
			glView.setRenderer(this);
			renderSet = true;
		}

		setContentView(glView);

		fileIO = new AndroidFileIO(getAssets());
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, glView, 1, 1);

		handler = new Handler();

        gameUIElements = new GameUIElements(this);

	}

    /*private View inflateAndGetToastView()
    {
       return getLayoutInflater().inflate(R.layout.toast_layout,  (ViewGroup) findViewById(R.id.toast_layout_root));
    }*/

	private boolean supportsEs2()
	{
		// Check if the system supports OpenGL ES 2.0.
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		// Even though the latest emulator supports OpenGL ES 2.0,
		// it has a bug where it doesn't setPosAndScale the reqGlEsVersion so
		// the above check doesn't work. The below will detect if the
		// app is running on an emulator, and assume that it supports
		// OpenGL ES 2.0.
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000
				|| (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 && (Build.FINGERPRINT.startsWith("generic")
						|| Build.FINGERPRINT.startsWith("unknown") || Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") || Build.MODEL
							.contains("Android SDK built for x86")));

		return supportsEs2;
	}

	@Override
	public void onResume()
	{

		super.onResume();



		if (renderSet)
			glView.onResume();

		synchronized (stateChanged)
		{
			if (screen == null)
			{
				screen = getStartScreen();
			}
			state = GLGameState.Running;


		}
	}

	@Override
	public void onBackPressed()
	{
		if (activityQuittableFlag)
			super.onBackPressed();
	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config)
	{
        surfaceCreated = true;
		screen.resume();
	}

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height)
	{
		screen.onSurfaceChanged(width, height);
	}

	@Override
	public void onDrawFrame(GL10 gl)
	{
		GLGameState state = null;

		synchronized (stateChanged)
		{
			state = this.state;
		}

		if (state == GLGameState.Running)
		{
			float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
			startTime = System.nanoTime();

			if (deltaTime > MAX_DELTA_TIME)
			{
				deltaTime = MAX_DELTA_TIME;
			}

			screen.update(deltaTime);
			screen.present(deltaTime);
		}

		else if (state == GLGameState.Paused)
		{
			screen.pause();
			synchronized (stateChanged)
			{
				stateChanged.notifyAll();
			}
		}

	}

	@Override
	public void onPause()
	{
		synchronized (stateChanged)
		{
			state = GLGameState.Paused;
			try
			{
				stateChanged.wait();

			} catch (InterruptedException e)
			{
			}


		}
		if (renderSet )
		{
			glView.onPause();
		}

		super.onPause();
	}

	@Override
	public Input getInput()
	{
		return input;
	}

	@Override
	public FileIO getFileIO()
	{
		return fileIO;
	}

	@Override
	public Audio getAudio()
	{
		return audio;
	}

	@Override
	public void setScreen(Screen screen)
	{
		if (screen == null)
			throw new IllegalArgumentException("Screen must not be null");

		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}

	@Override
	public AssetManager getAssetManager()
	{
		return GLGame.this.getAssets();
		
	}

	@Override
	public Screen getCurrentScreen()
	{
		return screen;
	}

	/*@Override
	public SharedPreferences getPreferences(String fileName, int mode)
	{
		return getSharedPreferences(fileName, mode);
	}*/

	static boolean isTablet(Context context)
	{
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return (metrics.heightPixels >= 728 && metrics.widthPixels >= 728);
	}	
}
