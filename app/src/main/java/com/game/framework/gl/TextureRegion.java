package com.game.framework.gl;

import com.game.framework.gl.Texture;

public class TextureRegion
{
	public final float u1, v1;
	public final float u2, v2;
	
	public final float drawWidth, drawHeight;
	
	public final int parentTextureActiveUnit;
	
	public boolean rotate = false;
	public TextureRegion(Texture texture, float x, float y, float width,
			float height)
	{
		this.u1 = x / texture.width;
		this.v1 = y / texture.height;
		this.u2 = this.u1 + width / texture.width;
		this.v2 = this.v1 + height / texture.height;

		this.drawHeight = height/100f;
		this.drawWidth = width/100f;

		this.parentTextureActiveUnit = texture.activeTextureUnit;
	} 
 
	// for rotated
	public TextureRegion(Texture texture, float x, float y, float width,
			float height, boolean rotate)
	{
        this.u1 = x / texture.width;
        this.v1 = y / texture.height;
        this.u2 = this.u1 + width / texture.width;
        this.v2 = this.v1 + height / texture.height;

        if(!rotate) {
            this.drawHeight = height / 100f;
            this.drawWidth = width / 100f;
        }
        else
        {
            this.rotate = true;
            this.drawHeight = width / 100f;
            this.drawWidth = height / 100f;
        }

        this.parentTextureActiveUnit = texture.activeTextureUnit;
		
	}	
	
}