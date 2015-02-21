package com.antibot.food;

import java.util.ArrayList;
import java.util.Arrays;


public class GenericPool<T>
{
    private static final byte UNASSIGNED = 100, ASSIGNED = 111;

    private ArrayList<T> poolObjectList;
    private int[] poolObjectStatusList;
    private int maxElements;

    public GenericPool(T[] array)
    {
        this.maxElements = array.length;

        poolObjectList = new ArrayList<T>(Arrays.asList(array));

        poolObjectStatusList = new int[maxElements];

        unAssignEveryObjects();
    }


    public void unAssignEveryObjects()
    {
        for(int i=0; i<poolObjectStatusList.length; i++)
        {
            poolObjectStatusList[i] = UNASSIGNED;
        }
    }

    public int allocateObject()
    {
        for(int i=0; i<poolObjectStatusList.length; i++)
        {
            if(poolObjectStatusList[i] == UNASSIGNED)
            {
                poolObjectStatusList[i] = ASSIGNED;
                return i;
            }
        }
        return -1;  // allocation failed
    }

    public T getObject(int index)
    {
        performIndexCheck(index);

        return poolObjectList.get(index);
    }

    public T unallocateObject(int index)
    {
        performIndexCheck(index);

        poolObjectStatusList[index] = UNASSIGNED;

        return null;  // always returns null

    }

    private void performIndexCheck(int index)
    {
        if(index < 0 || index > poolObjectStatusList.length || poolObjectStatusList[index] == UNASSIGNED)
        {
            throw new RuntimeException("unallocation failed at index " + index);
        }
    }


}