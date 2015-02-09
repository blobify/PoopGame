package com.game.framework.gl;

import static android.opengl.GLES20.GL_REPEAT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLES20.glUniform1i;

import com.antibot.poop.Assets;
import com.framework.utils.Logger;

public class Texture
{
	public int width;
	public int height;

	public int textureId;
	public final int activeTextureUnit;  // each texture gets separate activeTextureUnit

	public boolean firstTimeBind;

	public Texture(int width, int height, int textureId, int glTextureUnit)
	{
		this.width = width;
		this.height = height;
		this.textureId = textureId;
		this.activeTextureUnit = glTextureUnit;

        firstTimeBind = true;

      //  glActiveTexture(activeTextureUnit);
        // bind texture to the active texture place holder in graphic card memory or whatever
     //   glBindTexture(GL_TEXTURE_2D, textureId);

	}


	public void setActiveTextureUnitAndBind(int uTextureUnitLocation) {


        if (firstTimeBind)  // bind only once.. don't bind it again and again!
        {
            glActiveTexture(activeTextureUnit);

            // bind texture to the active texture place holder in graphic card memory or whatever
            glBindTexture(GL_TEXTURE_2D, textureId);
            Logger.log("TEXTURE", "Texture of id  " + textureId + " bound to " + activeTextureUnit);
            firstTimeBind = false;
        }

		// Tell the texture uniform sampler to use this texture in the
		// shader by
		// telling it to read from activeTextureUnit

		glUniform1i(uTextureUnitLocation, activeTextureUnit - GL_TEXTURE0);
	}

}
