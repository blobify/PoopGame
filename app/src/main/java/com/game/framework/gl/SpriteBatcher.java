package com.game.framework.gl;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TRIANGLES;

import com.antibot.poop.Assets;
import com.framework.shaderPrograms.TextureShaderProgram;
import com.framework.utils.Logger;
import com.game.math.Vector2;

public class SpriteBatcher
{
	final float[] verticesBuffer;
	int bufferIndex;
	final Vertices vertices;
	int numSprites;

	public SpriteBatcher(int maxSprites)
	{
		this.verticesBuffer = new float[maxSprites * 4 * 4];
		this.vertices = new Vertices(maxSprites * 4, maxSprites * 6, false, true);
		this.bufferIndex = 0;
		this.numSprites = 0;

		short[] indices = new short[maxSprites * 6];
		int len = indices.length;
		short j = 0;
		for (int i = 0; i < len; i += 6, j += 4)
		{
			indices[i + 0] = (short) (j + 0);
			indices[i + 1] = (short) (j + 1);
			indices[i + 2] = (short) (j + 2);
			indices[i + 3] = (short) (j + 2);
			indices[i + 4] = (short) (j + 3);
			indices[i + 5] = (short) (j + 0);
		}
		vertices.setIndices(indices, 0, indices.length);
	}

	public void beginBatch()
	{
		numSprites = 0;
		bufferIndex = 0;
	}

	public void endBatch(TextureShaderProgram textureProgram)
	{
		if(numSprites > 0)
		{
		vertices.setVertices(verticesBuffer, 0, bufferIndex);
		vertices.setAttribLocations(textureProgram.getPositionAttributeLocation(), 0, textureProgram.getTextureCoordinatesAttributeLocation());		
		
		vertices.draw(GL_TRIANGLES, 0, numSprites * 6);
		
		vertices.disableAttribLocations();
		}
	}

	public void drawSprite(float x, float y, float width, float height, TextureRegion region)
	{

		float halfWidth = width / 2;
		float halfHeight = height / 2;
		float x1 = x - halfWidth;
		float y1 = y - halfHeight;
		float x2 = x + halfWidth;
		float y2 = y + halfHeight;

		float u1 = region.u1; float u2 = region.u2; float v1 = region.v1; float v2 = region.v2;
		if (!region.rotate)
		{			
			fillVerticesBuffer(x1, y1, x2, y1, x2, y2, x1, y2, u1, v2, u2, v2, u2, v1, u1, v1);
		} else
		{
		
			fillVerticesBuffer(x1, y1, x2, y1, x2, y2, x1, y2, u2, v2, u2, v1, u1, v1, u1, v2);
		}
	}	
	

	public void drawCarefulSprite(float x, float y, float width, float height, TextureRegion region, TextureShaderProgram texShaderProgram)
	{
		takeCarefulMeasures(region, texShaderProgram);
		drawSprite(x, y, width, height, region);
	}
	
	/*public void drawCarefulSpriteWithAlphaCheck(float x, float y, float width, float height, TextureRegion region, TextureShaderProgram texShaderProgram, float alpha)
	{
		if(alpha != texShaderProgram.alpha)
		{
			batcher.endBatch
		}
	}*/

	public void drawCarefulSprite(float x, float y, float width, float height, float angle, TextureRegion region, TextureShaderProgram texShaderProgram)
	{
		takeCarefulMeasures(region, texShaderProgram);
		drawSprite(x, y, width, height, angle, region);
	}

	public void drawCarefulHorizontallyInvertedSprite(float x, float y, float width, float height, TextureRegion region, TextureShaderProgram texShaderProgram)
	{
		takeCarefulMeasures(region, texShaderProgram);

		float halfWidth = width / 2;
		float halfHeight = height / 2;
		float x1 = x - halfWidth;
		float y1 = y - halfHeight;
		float x2 = x + halfWidth;
		float y2 = y + halfHeight;
		
		float u1 = region.u1; float u2 = region.u2; float v1 = region.v1; float v2 = region.v2;
		
		if (!region.rotate)
		{
			fillVerticesBuffer(x1, y1, x2, y1, x2, y2, x1, y2, u2, v2, u1, v2, u1, v1, u2, v1);
		}

	}
	
	
	/**
	 * @param pivotX a number between 0 and 1. The sprite is drawn at center of gravity if pivotX and Y are 0.5 each
	 * 
	 */
	public void drawCarefulSpriteWithPivot(float x, float y, float width, float height, float pivotX, float pivotY, TextureRegion region, TextureShaderProgram texShaderProgram)
	{
		takeCarefulMeasures(region, texShaderProgram);
		
		float x1 = x - width*pivotX;
		float x2 = x + width*(1-pivotX);
		float y1 = y - height*pivotY;
		float y2 = y + height*(1-pivotY);
		
		float u1 = region.u1; float u2 = region.u2; float v1 = region.v1; float v2 = region.v2;
		if (!region.rotate)
		{			
			fillVerticesBuffer(x1, y1, x2, y1, x2, y2, x1, y2, u1, v2, u2, v2, u2, v1, u1, v1);
		} else
		{
		
			fillVerticesBuffer(x1, y1, x2, y1, x2, y2, x1, y2, u2, v2, u2, v1, u1, v1, u1, v2);
		}
	}

    public void drawCarefulSpriteWithPivot(float x, float y, float width, float height, float pivotX, float pivotY, float angle, TextureRegion region, TextureShaderProgram texShaderProgram)
    {
        takeCarefulMeasures(region, texShaderProgram);

        float vecLeft = -width*pivotX;
        float vecRight = width*(1-pivotX);
        float vecTop = height*pivotY;
        float vecBottom = -height*(1-pivotY);

        float rad = angle * Vector2.TO_RADIANS;
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);

        float vecLeftIntoCos = vecLeft*cos;
        float vecLeftIntoSin = vecLeft*sin;
        float vecRightIntoCos = vecRight*cos;
        float vecRightIntoSin = vecRight*sin;
        float vecTopIntoCos = vecTop*cos;
        float vecTopIntoSin = vecTop*sin;
        float vecBottomIntoCos = vecBottom*cos;
        float vecBottomIntoSin = vecBottom*sin;

        float x1 = x + (vecLeftIntoCos - vecBottomIntoSin);
        float y1 = y + (vecLeftIntoSin + vecBottomIntoCos);
        float x2 = x + (vecRightIntoCos - vecBottomIntoSin);
        float y2 = y + (vecRightIntoSin + vecBottomIntoCos);
        float x3 = x + (vecRightIntoCos - vecTopIntoSin);
        float y3 = y + (vecRightIntoSin + vecTopIntoCos);
        float x4 = x + (vecLeftIntoCos - vecTopIntoSin);
        float y4 = y + (vecLeftIntoSin + vecTopIntoCos);

        float u1 = region.u1; float u2 = region.u2; float v1 = region.v1; float v2 = region.v2;

        if (!region.rotate)
        {
            fillVerticesBuffer(x1, y1, x2, y2, x3, y3, x4, y4, u1, v2, u2, v2, u2, v1, u1, v1);
        } else
        {

            fillVerticesBuffer(x1, y1, x2, y2, x3, y3, x4, y4, u2, v2, u2, v1, u1, v1, u1, v2);
        }


    }

	private void takeCarefulMeasures(TextureRegion region, TextureShaderProgram texShaderProgram)
	{
		if (texShaderProgram.currentlyBoundTextureActiveUnit != region.parentTextureActiveUnit)
		{
			endBatch(texShaderProgram);
			texShaderProgram.setUniformsAndBindTexture(texShaderProgram.projectionMatrix, Assets.getTextureFromTextureRegion(region));
			beginBatch();

		}
	}
	
	

	public void drawSprite(float x, float y, float width, float height, float angle, TextureRegion region)
	{
		float halfWidth = width / 2;
		float halfHeight = height / 2;

		float rad = angle * Vector2.TO_RADIANS;
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);

		float wIntoc = halfWidth*cos; float wIntos = halfWidth*sin;
		float hIntoc = halfHeight*cos; float hIntos = halfHeight*sin;
		
		float x1 = -wIntoc + hIntos; //halfHeight) * sin;
		float y1 = -wIntos - hIntoc; //-halfWidth * sin + (-halfHeight) * cos;
		float x2 = wIntoc + hIntos; //halfWidth * cos - (-halfHeight) * sin;
		float y2 = wIntos - hIntoc; //halfWidth * sin + (-halfHeight) * cos;
		float x3 = wIntoc - hIntos; //halfWidth * cos - halfHeight * sin;
		float y3 = wIntos + hIntoc; //halfWidth * sin + halfHeight * cos;
		float x4 = -wIntoc - hIntos; //-halfWidth * cos - halfHeight * sin;
		float y4 = -wIntos + hIntoc; //-halfWidth * sin + halfHeight * cos;

		x1 += x;
		y1 += y;
		x2 += x;
		y2 += y;
		x3 += x;
		y3 += y;
		x4 += x;
		y4 += y;
		
		float u1 = region.u1; float u2 = region.u2; float v1 = region.v1; float v2 = region.v2;
		
		if (!region.rotate)
		{			
			fillVerticesBuffer(x1, y1, x2, y2, x3, y3, x4, y4, u1, v2, u2, v2, u2, v1, u1, v1);
		} else
		{
			
			fillVerticesBuffer(x1, y1, x2, y2, x3, y3, x4, y4, u2, v2, u2, v1, u1, v1, u1, v2);
		}
	}
	
	private void fillVerticesBuffer(float x1, float y1,float x2, float y2,float x3, float y3,float x4, float y4, float u1, float v1, float u2, float v2, float u3, float v3, float u4, float v4)
	{
		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = u1;
		verticesBuffer[bufferIndex++] = v1;

		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = u2;
		verticesBuffer[bufferIndex++] = v2;

		verticesBuffer[bufferIndex++] = x3;
		verticesBuffer[bufferIndex++] = y3;
		verticesBuffer[bufferIndex++] = u3;
		verticesBuffer[bufferIndex++] = v3;

		verticesBuffer[bufferIndex++] = x4;
		verticesBuffer[bufferIndex++] = y4;
		verticesBuffer[bufferIndex++] = u4;
		verticesBuffer[bufferIndex++] = v4;

		numSprites++;
	}

    public void prepareForDrawingAlpha(float alpha, TextureShaderProgram texShaderProgram) {
        endBatch(texShaderProgram);
        texShaderProgram.setAlpha(alpha);
        beginBatch();
    }


    public void finalizeDrawingAlpha(TextureShaderProgram texShaderProgram) {
        endBatch(texShaderProgram);
        texShaderProgram.setAlpha(1);
        beginBatch();
    }
}
