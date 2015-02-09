package com.framework.utils;

import android.util.Log;

public class Logger
{

	public static boolean onceDone = false;
	
	public static final boolean LOG = true;
	
	
	/*public static void logForce(String tag, String msg)
	{
		Log.d(tag, msg.toString());
	}*/
	
	public static void log(String tag, Object msg)
	{
		if(LOG)
			Log.d(tag, msg.toString());
	}

    public static void log(Object msg)
    {
        if(LOG)
        {
            Log.d(msg.toString(), msg.toString());
        }
    }
	
	public static void logV(String tag, Object msg)
	{
		if(LOG)
			Log.v(tag, msg.toString());
	}
	
	public static void logW(String tag, Object msg)
	{
		if(LOG)
			Log.v(tag, msg.toString());
	}

    public static void print(String msg)
    {
        if(LOG)
        {
            System.out.println(msg);
        }
    }
	
	
}