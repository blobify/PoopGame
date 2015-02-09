package com.antibot.poop;

import com.antibot.poop.gameobj.Musca;
import com.antibot.ui.OvershootInterpolator;

public class Camera
{
	/* public static final float BOTTOM_LIMIT = 0;
	public static final float ACCELERATION = 6;
	public static final float NORM_VEL = 4f;
	public static final float BOTTOM_NORM = 1.2f;*/


	
	public static final float GAP_MIN = 1;
	public static final float GAP_MAX = 1.3f;
    public static final int ID_GAP_EXPAND = 100;
    public static final int ID_GAP_COMPRESS = 50;

    float gap = 0;
    boolean minGapReached = false;

	public float bottomPos = 0.0f;

    OvershootInterpolator gapInterpolator;

    public Camera()
    {
        gapInterpolator = new OvershootInterpolator(new OvershootInterpolator.InterpolationCompleteListener() {
            @Override
            public void onInterpolationComplete(int id) {
                //nothing to do
            }
        });

        reset();
    }

    public void reset()
    {
        gap = GAP_MIN;
        minGapReached = false;
    }

    public void update(float deltaTime,float muscaPos)
    {
       if(!minGapReached)
       {
           if(muscaPos - bottomPos > GAP_MIN)
           {
               minGapReached = true;
           }
       }
       else
       {
           if(gapInterpolator.started)
           {
                gap = gapInterpolator.getInterpolation(deltaTime);
           }
           bottomPos = muscaPos - gap;
       }
    }

	/*public void update(float deltaTime, final Musca musca)
	{
		float diff = musca.pos.y - bottomPos;

		if (diff > GAP_MAX)
		{
			float deltaPos = diff - GAP_MAX; // the distance to catch up

			bottomPos += deltaPos;
		}
		
		
		
	}*/

    public int getInterpolatorId()
    {
        return gapInterpolator.id;
    }

    public void expandGap(float muscaPos)
    {
        gapInterpolator.set(gap,GAP_MAX,1,ID_GAP_EXPAND,0);
    }

    public void compressGap(float muscaPos)
    {
        gapInterpolator.set(gap,GAP_MIN,1,ID_GAP_COMPRESS,0);
    }



}
