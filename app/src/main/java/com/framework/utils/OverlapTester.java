package com.framework.utils;

public class OverlapTester
{
	public static boolean pointInRectangle(float pX, float pY, float rectLeft, float rectRight, float rectBottom, float rectTop)
	{
		if (pX > rectLeft && pX < rectRight && pY > rectBottom && pY < rectTop)
		{
			return true;
		}
		return false;
	}
}
