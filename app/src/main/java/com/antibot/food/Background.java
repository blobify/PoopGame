package com.antibot.food;

import static android.opengl.GLES20.GL_TRIANGLES;

import com.game.framework.gl.TextureRegion;
import com.game.framework.gl.Vertices;

public class Background
{
	float[] verts;
	
	float[] temp;
	
	short[] indices;
	
	Vertices strip;
		
	
	public final float U1,U2;
	public static final float HEIGHT = 14;
	public final float slope;
	
	public float leftStartU, leftEndU, rightStartU, rightEndU;
	
	
	ColorInterpolater colorInterpolater;
	
	float bottomCameraPos;
	public Background(TextureRegion region)
	{
		
		this.U1 = region.u1; this.U2 = region.u2;
		
		slope = (U2-U1)/HEIGHT;
		
		leftStartU = U1; leftEndU = U2;
		rightStartU = U1; rightEndU = U2;
		
		
		verts = new float[] {
				
				0,0,region.u1,region.v1,
				Static.TARGET_WIDTH,0,region.u1,region.v2,
				Static.TARGET_WIDTH,HEIGHT,region.u2,region.v2,
				0,HEIGHT,region.u2,region.v1,
				
				
				0,HEIGHT,region.u1,region.v1,
				Static.TARGET_WIDTH,HEIGHT,region.u1, region.v2,
				Static.TARGET_WIDTH,HEIGHT,region.u1,region.v2,
				0,HEIGHT,region.u1,region.v1				
		};
		
		temp = new float[verts.length];
		
		indices = new short[]{0,1,2, 2,3,0, 4,5,6,  6,7,4};
		
		strip = new Vertices(8, 12, false, true);
		
		strip.setIndices(indices, 0, indices.length);
		
		
		colorInterpolater = new ColorInterpolater();
		colorInterpolater.setColorArr(new float[]{0.2f,0.1f,0.1f,   0.3f,0.3f,0.2f,   0.1f,0.2f,0.3f,   0.1f,0.1f,0.2f,   0.2f,0.3f,0.2f}, 0);
	}
	
	public void shift(float bottomCameraPos)
	{		
		float deltaCameraPos = bottomCameraPos - this.bottomCameraPos;
		
		this.bottomCameraPos  =  bottomCameraPos;	
		
		float vertexShift = verts[9] - deltaCameraPos;
		
		if(vertexShift <= 0)
		{
			vertexShift = HEIGHT + vertexShift;		
		}
		else if(vertexShift >= bottomCameraPos + HEIGHT)
		{
			vertexShift = HEIGHT - vertexShift;	
		}
		verts[9] = verts[13] = verts[17] = verts[21] = vertexShift;
		
		float shiftTex = U2 - slope * (verts[9] - verts[1]);
		
		verts[2] = verts[2+4] = verts[26] = verts[26+4] = shiftTex;
		
		
		
		
		
		colorInterpolater.update();
		
	}
	
	
	public void setVerticesAndAttribLocations(int posAttribLoc, int texCoordAttribLoc, float camBottomPos )
	{
		System.arraycopy(verts, 0, temp, 0, verts.length);
			
		for(int i=1; i<temp.length; i+=4)
		{
			temp[i] += camBottomPos;
		}
		
		strip.setVertices(temp, 0, temp.length);
		
		strip.setAttribLocations(posAttribLoc, 0, texCoordAttribLoc);
		
		
	}
	
	public void draw()
	{
		strip.draw(GL_TRIANGLES, 0, 12);
		strip.disableAttribLocations();
	}
	
	public void reset()
	{
		colorInterpolater.reset();
	}
	
	
}
