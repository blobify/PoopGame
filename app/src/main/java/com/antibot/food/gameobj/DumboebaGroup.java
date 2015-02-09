package com.antibot.food.gameobj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.antibot.food.WorldRenderer;
import com.antibot.food.Assets;
import com.antibot.food.Static;
import com.antibot.food.UpdateAndDraw;
import com.framework.shaderPrograms.TextureShaderProgram;
import com.framework.utils.MyArrayList;
import com.game.framework.gl.SpriteBatcher;
import com.game.framework.gl.TextureRegion;
import com.game.math.Vector2;

/**
 * 
 * Handles the group of dumboebas and shares path, direction, angle data and
 * also maintains path-point list
 */

public class DumboebaGroup implements UpdateAndDraw
{
    public static final int MAX_NODES_PER_GROUP = 16;

	public MyArrayList<Dumboeba> dumboList;

	int pathPointIndex; // responsible for animation of path-points

	int routineChecker = 0; // responsible for removing disabled dumboebas from
							// list

	public MyArrayList<PathPoint> pointList;

    public static final ArrayList<Node> tempNodeList = createTempNodeList();

	public float[] pathData, dirData;


	public boolean enabled, disablable;

    float alpha;



    private static ArrayList<Node> createTempNodeList()
    {
        ArrayList<Node> toReturn = new ArrayList<Node>(MAX_NODES_PER_GROUP);
        for(int i=0; i<MAX_NODES_PER_GROUP; i++)
        {
            toReturn.add(new Node());
        }
        return toReturn;
    }

	public DumboebaGroup()
	{
		dumboList = new MyArrayList<Dumboeba>();

		pointList = new MyArrayList<PathPoint>();

		pathData = new float[MAX_NODES_PER_GROUP * 2];
		dirData = new float[MAX_NODES_PER_GROUP * 2];

	}

	public void setPathAndDir(float x, float y, int numberOfNodes)
	{
		enabled = true;
		setPathData(x, y,numberOfNodes);
		setDirData();


        disablable = false;
        alpha = 1;
	}

    public void init()
    {
        enabled = true;
        disablable = false;
        alpha = 1;
    }

	public void setDumbosUp()
	{
		int index = 0;

		for (Dumboeba dumboeba : dumboList)
		{
			dumboeba.setToGroup(this, index);

			index = (index + 2) % pathData.length;
		}

		// Logger.log("SIZE of dumboList ", dumboList.size());
	}


    private void setPathData(int numberOfNodes)
    {
        for(int i=0; i<numberOfNodes; i++)
        {
            Node node = tempNodeList.get(i);

            pathData[i*2] = node.posX;
            pathData[i*2 + 1] = node.posY;

        }
    }

	private void setPathData(float x, float y, int numberOfNodes)
	{

		Random rand = Static.rand;

		this.pathData[0] = x;
		this.pathData[1] = y;
		this.pathData[2] = x + rand.nextFloat() * 5;
		this.pathData[3] = y + rand.nextFloat() * 5;
		this.pathData[4] = x + rand.nextFloat() * 5;
		this.pathData[5] = y + rand.nextFloat() * 5;

		if (rand.nextBoolean())
		{
			this.pathData[6] = Float.NaN;

		}

		else
		{
			this.pathData[6] = x + rand.nextFloat() * 5;
			this.pathData[7] = y + rand.nextFloat() * 5;
			this.pathData[8] = Float.NaN;
		}

	}

	/**
	 * fill dir array so u don't have to calculate it for every change in path
	 * for each dumboebas
	 */
	public void setDirData()
	{
		boolean loopBreakable = false;

		for (int i = 0; i < pathData.length; i += 2)
		{
			int indexX = i % (pathData.length);
			int indexY = (indexX + 1);
			int nextIndexX = (indexX + 2) % (pathData.length);
			int nextIndexY = nextIndexX + 1;

			if (Float.isNaN(pathData[nextIndexX]))
			{
				nextIndexX = 0;
				nextIndexY = nextIndexX + 1;
				loopBreakable = true;

			}

			float X = pathData[indexX];
			float Y = pathData[indexY];
			float nextX = pathData[nextIndexX];
			float nextY = pathData[nextIndexY];

			float dX = nextX - X;
			float dY = nextY - Y;

			float len = (float) Math.sqrt(dX * dX + dY * dY);

			dirData[indexX] = dX / len;
			dirData[indexY] = dY / len;

			// angleData[indexX] =

			if (loopBreakable)
				break;
		}

	}

	public void setPathPoints(MyArrayList<PathPoint> pointListPool)
	{
		boolean loopBreakable = false;

		Vector2 temp = Static.tempVector;

		// calculate points and add it to pointList from pool list
		for (int i = 0; i < pathData.length; i += 2)
		{
			int indexX = i % (pathData.length);
			int indexY = (indexX + 1);
			int nextIndexX = (indexX + 2) % (pathData.length);
			int nextIndexY = nextIndexX + 1;

			if (Float.isNaN(pathData[nextIndexX]))
			{
				nextIndexX = 0;
				nextIndexY = nextIndexX + 1;
				loopBreakable = true;
			}

			temp.x = pathData[indexX];
			temp.y = pathData[indexY];
			float tX = pathData[nextIndexX];
			float tY = pathData[nextIndexY];

			float dirX = dirData[indexX];
			float dirY = dirData[indexY];

			int quadrant = determineQuadrant(dirX, dirY);

			// traveling the line
			boolean breakable = false;
			while (true)
			{

				temp.x += dirX * 0.25f;
				temp.y += dirY * 0.25f;

				if (checkIfLineTravelComplete(temp, tX, tY, quadrant))
				{
					breakable = true;
				}

				PathPoint p = null;
				if (pointListPool.size() > 0)
				{
					p = pointListPool.removeLast();
				}

				if (p == null)
					return;

				p.set(temp.x, temp.y);
				pointList.add(p);

				if (breakable)
					break;

			}

			if (loopBreakable)
				break;

		}
	}

	public static int determineQuadrant(float dirX, float dirY)
	{
		int toReturn;
		if (dirX > 0)
		{
			if (dirY > 0)
			{
				toReturn = 1;
			} else
			{
				toReturn = 4;
			}
		} else
		{
			if (dirY > 0)
			{
				toReturn = 2;
			} else
			{
				toReturn = 3;
			}
		}
		return toReturn;
	}

	public static boolean checkIfLineTravelComplete(Vector2 currentPos, float tX, float tY, int quad)
	{
		boolean toReturn = false;
		if (quad == 1 || quad == 4)
		{
			if (currentPos.x >= tX)
			{
				toReturn = true;
			}
		} else if (quad == 2) // dx <= 0 and dy > 0
		{
			if (currentPos.y >= tY)
			{
				toReturn = true;
			}
		} else if (quad == 3) // dx might be <= 0 and dy might be <= 0
		{
			if (currentPos.y <= tY && currentPos.x <= tX)
			{
				toReturn = true;
			}
		}

		if (toReturn)
		{
			currentPos.x = tX;
			currentPos.y = tY;
		}

		return toReturn;
	}

	@Override
	public void update(float deltaTime)
	{		
		if (!disablable)
		{

			if (dumboList.size() == 0)
			{
                disablable = true;
                return;
			}

			// routineCheck of dumboebas in the list

			routineChecker++;

			if (routineChecker >= 5)
			{
				routineChecker = 0;

				removeDisabledDumbosFromGroup();
			}

			pathPointIndex++;

			if (pathPointIndex >= 16)
			{
				pathPointIndex = 0;
			}
		}
        else
        {
            alpha -= deltaTime*2;
            if(alpha < 0)
            {
                alpha = 0;
                enabled = false;
            }
        }

	}

	private void removeDisabledDumbosFromGroup()
	{
		Iterator<Dumboeba> iter = dumboList.iterator();

		while (iter.hasNext())
		{
			Dumboeba dumbo = iter.next();
			if (!dumbo.enabled)
			{
				iter.remove();
			}
		}
	}

	@Override
	public void draw()
	{
		SpriteBatcher batcher = WorldRenderer.batcher;
		TextureShaderProgram texShaderProgram = WorldRenderer.texShaderProgram;

        final boolean alphaLessThanOne = alpha < 1;

        if(alphaLessThanOne)
        {
            batcher.prepareForDrawingAlpha(alpha, texShaderProgram);
        }

        TextureRegion pathPointArr[] = Assets.path_arr;
        int index = pathPointIndex;
        int len = pathPointArr.length;
        for(PathPoint p : pointList)
        {
            TextureRegion region = pathPointArr[index];
            batcher.drawCarefulSprite(p.posX, p.posY, 0.2f, 0.2f, region, texShaderProgram);
            index = (index + 1) % len;
        }

        if(alphaLessThanOne) {

            batcher.finalizeDrawingAlpha(texShaderProgram);
        }
    }

    public void addDumboeba(Dumboeba dumboeba, int index) {

        dumboList.add(dumboeba);

        dumboeba.setToGroup(this, index);
    }

    public void setDirDataAndPathPoints(MyArrayList<PathPoint> pointListPool) {
        setDirData();
        setPathPoints(pointListPool);
    }

    public void setPosYBackToBase(float threshold) {
        /*for(int i=1; i<lastPathPointIndex; i+=2)
        {
            pathData[i] -= threshold;
        }*/
        for(int i=1; i<pathData.length; i+=2)
        {
            pathData[i] -= threshold;
        }
        for(int i=0; i<pointList.size(); i++)
        {
            pointList.get(i).posY-=threshold;
        }
    }

    public void setLastPathPointIndex(int i) {
        pathData[i] = Float.NaN;
    }


    public static class Node
    {
        public float posX, posY;
        public boolean hasDumbo;

        public void set(float posX, float posY, boolean hasDumbo)
        {
            this.posX = posX; this.posY = posY; this.hasDumbo = hasDumbo;
        }
    }
}
