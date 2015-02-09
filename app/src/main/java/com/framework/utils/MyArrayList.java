package com.framework.utils;

import java.util.ArrayList;

public class MyArrayList<T> extends ArrayList<T>
{
	private static final long serialVersionUID = 1L;
	
	public MyArrayList(int i)
	{
		super(i);
	}
	
	public MyArrayList()
	{
		super();
	}

	public T removeLast()
	{

		return remove(size()-1);
	}

}
