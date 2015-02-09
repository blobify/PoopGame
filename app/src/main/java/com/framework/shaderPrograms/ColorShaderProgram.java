package com.framework.shaderPrograms;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;

import com.antibot.poop.R;
import com.game.framework.gl.Texture;


import android.content.Context;

public class ColorShaderProgram extends ShaderProgram
{
	// Uniform locations
	private final int uMatrixLocation;
	
	// Attribute locations
	private final int aPositionLocation;
	
	private final int uColorLocation;
	private final int uTextureUnitLocation;
	private final int aTextureCoordinatesLocation;
	
	
	public ColorShaderProgram(Context context)
	{
		super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);
		// Retrieve uniform locations for the shader program.
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
		// Retrieve attribute locations for the shader program.
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
		uColorLocation = glGetUniformLocation(program, U_COLOR);//glGetAttribLocation(program, A_COLOR);
		
		aTextureCoordinatesLocation = 
		        glGetAttribLocation(program, A_TEXTURE_COORDINATES);
	}

	public void setUniforms(float[] matrix, Texture texture, float[] color)
	{
		// Pass the matrix into the shader program.
		glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
		
		glUniform4fv(uColorLocation, 1, color, 0);
		
		texture.setActiveTextureUnitAndBind(uTextureUnitLocation);
	    
	}

	public int getPositionAttribLocation()
	{
		return aPositionLocation;
	}

	/*public int getColorUniformLocation()
	{
		return uColorLocation;
	}*/
	
	public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }
}
