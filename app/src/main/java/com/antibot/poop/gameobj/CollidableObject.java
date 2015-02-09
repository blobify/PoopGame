package com.antibot.poop.gameobj;

import com.antibot.poop.Static;
import com.antibot.poop.UpdateAndDraw;
import com.game.math.Vector2;

public abstract class CollidableObject implements UpdateAndDraw
{
	public static final int DRAW_PRIORITY_HIGH = 0, DRAW_PRIORITY_MED = 1, DRAW_PRIORITY_LOW = 2;

    public static final int BOUNDING_BOX_CIRCLE = 0, BOUNDING_BOX_RECTANGLE = 1, BOUNDING_BOX_MESH = 2;

    public static final int STATE_DEATH_PHASE = 1, STATE_ALIVE_AND_KICKING = 2;
    public int state;

	public boolean collidable;
	public boolean enabled;
	
	public Vector2 pos;
	//public float radius;
	//public float radiusSquared;
	
	
	public final int drawPriority;


	//public enum ObjectType{Musca,Food,DumbEnemy,BigBaddy,Shooter,Bullet}

	public final int objectType;

    public final int boundingBoxType;
	
	public CollidableObject( int objectType, int drawPriority, int boundingBoxType)
	{
		pos = new Vector2();
		this.objectType = objectType;		
		this.drawPriority = drawPriority;

        this.boundingBoxType = boundingBoxType;
	}
	
	public void set(float posX, float posY)
	{
			
		pos.set(posX,posY);
		
		enable();

	}
	
			
	public void enable()
	{
		enabled = true;
		collidable = true;
        state = STATE_ALIVE_AND_KICKING;
	}

    /*
    after this method, the enabled boolean will be set to false and will be returned to pool with next update() call
     */
	public void disable()
	{
		
		enabled = false;
		collidable = false;

	}

    /*
    this method indicates the object is eligible for disabling. Can use disableOverlayDrawable boolean to do death animations
     */
	public void deathToYou()
	{
		collidable = false;

        state = STATE_DEATH_PHASE;
	}
	
	
	public void teleportify(float len, boolean isRadius)
	{	
		final float temp = pos.x - Static.TARGET_WIDTH;
				
		if(!isRadius)
		{			
			len = len/2;
		}
		
		if(temp > 0)
		{
			if(temp - len > 0)
			{
				pos.x -= len*2 + Static.TARGET_WIDTH;
			}
		}
		else if(pos.x < 0)
		{
			if( pos.x+len< 0)
			{
				pos.x += len*2 + Static.TARGET_WIDTH;
			}
		}
		
	}

    public void setPosYToBase(float threshold)
    {
        pos.y -= threshold;
    }
	
	public static boolean checkCircleCircleCollision(Vector2 pos1, float rad1, Vector2 pos2, float rad2)
	{
		float distanceSquared = pos1.distSquared(pos2);
		
		float radiusSum = rad1 + rad2;
		
		return distanceSquared <= radiusSum*radiusSum;
	}
	
	public static boolean checkCircleRectCollision(Vector2 pos1, float rad, Vector2 pos2, float width, float height)
	{
		float closestX = pos1.x;
		float closestY = pos1.y;
		
		float widthByTwo = width/2;
		float heightByTwo = height/2;
				
		if(closestX < pos2.x - widthByTwo)
		{
			closestX = pos2.x - widthByTwo;
		}
		else if(closestX > pos2.x + widthByTwo)
		{
			closestX = pos2.x + widthByTwo;
		}
		
		if(closestY < pos2.y - heightByTwo)
		{
			closestY = pos2.y - heightByTwo;
		}
		else if(closestY > pos2.y + heightByTwo)
		{
			closestY = pos2.y + heightByTwo;
		}

		
		float distanceSquared = pos1.distSquared(closestX, closestY);
		
		return distanceSquared < rad*rad;
	
	}


    protected static boolean standard_CC_CollsionCheck(CircularCollidableObject one, CircularCollidableObject two)
    {
        if(one.collidable && two.collidable)
        {
            if(checkCircleCircleCollision(one.pos,one.radius,two.pos,two.radius))
                return true;
        }
        return false;
    }

    protected static boolean standard_CR_CollisionCheck(CircularCollidableObject circObj, RectangularCollidableObject rectObj)
    {
        if(circObj.collidable && rectObj.collidable)
        {
            if(checkCircleRectCollision(circObj.pos,circObj.radius,rectObj.pos,rectObj.width,rectObj.height))
            {
                return true;
            }
        }
        return false;
    }
	
	public abstract float getBottomPos();
	public abstract float getTopPos();
}
