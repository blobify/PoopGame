package com.framework.shaderPrograms;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;

import com.antibot.poop.R;
import com.game.framework.gl.Texture;

import android.content.Context;

public class TextureShaderProgram extends ShaderProgram {
    // Uniform locations
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;
    private final int uAlphaLocation;
    
    // Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;
    
    //private Texture texture;
    public int currentlyBoundTextureActiveUnit;

    public float[] projectionMatrix;
    
    public float alpha;
    
    public TextureShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader,
            R.raw.texture_fragment_shader);

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
        uAlphaLocation = glGetUniformLocation(program, U_ALPHA);
        
        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);

    }

    public void setUniformsAndBindTexture(float[] matrix, Texture texture)
    {
    	this.projectionMatrix = matrix; // this is used for carefulDrawSprite in SpriteBatcher
    	
    	// Pass the matrix into the shader program.
    	glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);  
    	
    	//set alpha value for rendered texture
    	//glUniform1f(uAlphaLocation,alpha);
    	
        texture.setActiveTextureUnitAndBind(uTextureUnitLocation); 
        
        this.currentlyBoundTextureActiveUnit = texture.activeTextureUnit;
       
    }
    
    public void setAlpha(float alpha)
    {
    	this.alpha = alpha;
    	glUniform1f(uAlphaLocation, alpha);
    }
    
    

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }
}
