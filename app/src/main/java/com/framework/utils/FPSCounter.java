package com.framework.utils;

public class FPSCounter
{
	long startTime = System.nanoTime();
	int frames = 0;
	static final String TAG = "FPSCounter";
	
	public void logFrame()
	{
		frames++;
		if (System.nanoTime() - startTime >= 1000000000)
		{
			Logger.log(TAG, "fps: " + frames);
			
			frames = 0;
			startTime = System.nanoTime();
		}
	}
}