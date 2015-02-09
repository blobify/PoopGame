package com.framework.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.Resources;

public class TextResourceReader
{
	static String TAG = "TextResourceReader";

	public static String readTextFileFromResource(Context context, int resourceId)
	{
		StringBuilder body = new StringBuilder();

		try
		{
			InputStream inputStream = context.getResources().openRawResource(resourceId);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String readLine = "";

			while ((readLine = bufferedReader.readLine()) != null)
			{
				body.append(readLine + '\n');

			}

		} catch (IOException e)
		{
			throw new RuntimeException("Could not open resource: " + resourceId, e);
		} catch (Resources.NotFoundException nfe)
		{
			throw new RuntimeException("Resource not found: " + resourceId, nfe);
		}

		return body.toString();
	}
}
