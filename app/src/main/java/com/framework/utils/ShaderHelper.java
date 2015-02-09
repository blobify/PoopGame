package com.framework.utils;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

import android.util.Log;

public class ShaderHelper
{
	public static String TAG = "ShaderHelper";
	public static String FRAG_SHADER = "Fragment Shader";
	public static String VERT_SHADER = "Vertex_Shader";

	public static int compileFragmentShader(String fragmentShaderSource)
	{
		return compileShader(GL_FRAGMENT_SHADER, fragmentShaderSource);
	}

	public static int compileVertexShader(String vertexShaderSource)
	{
		return compileShader(GL_VERTEX_SHADER, vertexShaderSource);
	}

	private static int compileShader(int type, String source)
	{
		final int shaderObjectId = glCreateShader(type);
		if (shaderObjectId == 0)
		{
			String msg = "Could not create ";
			if (type == GL_FRAGMENT_SHADER)
				Logger.logW(TAG, msg + FRAG_SHADER);
			else if (type == GL_VERTEX_SHADER)
				Logger.logW(TAG, VERT_SHADER);
			return shaderObjectId;
		}

		// pass in the shader source
		glShaderSource(shaderObjectId, source);

		// compile it
		glCompileShader(shaderObjectId);

		// Get the compilation status.
		final int[] compileStatus = new int[1];
		glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

		// Print the shader info log to the Android log output.
		Logger.logV(TAG, "Results of compiling source:" + "\n" + source + "\n:" + glGetShaderInfoLog(shaderObjectId));

		// Verify the compile status.
		if (compileStatus[0] == 0)
		{
			// If it failed, delete the shader object.
			glDeleteShader(shaderObjectId);

			Logger.logW(TAG, "Compilation of shader failed.");

			return 0;
		}

		// Return the shader object ID.
		return shaderObjectId;
	}
	
	public static int linkProgram(int vertexId, int fragmentId)
	{
		int program = glCreateProgram();
		
		if(program == 0)
		{
			Logger.logW(TAG, "Could not create shader program");
			return program;
		}
		
		 // Attach the vertex shader to the program.
        glAttachShader(program, vertexId);
        // Attach the fragment shader to the program.
        glAttachShader(program, fragmentId);
		
		glLinkProgram(program);
		
		// getting link status
		
		int linkStatus[] = new int[1];
		
		glGetProgramiv(program, GL_LINK_STATUS, linkStatus, 0);
		
		Logger.logV(TAG, "Results of Linking Program \n" + glGetProgramInfoLog(program));
		
		if(linkStatus[0] == 0)
		{
			// link failed.. delete object
			glDeleteProgram(program);
			Logger.logW(TAG, "linking of program failed");
			return 0;
		}
		
		return program;
		
	}
	
	public static boolean validateProgram(int programObjectId)
	{
		 glValidateProgram(programObjectId);
			
	        final int[] validateStatus = new int[1];
	        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
	        Logger.logV(TAG, "Results of validating program: " + validateStatus[0]
	            + "\nLog:" + glGetProgramInfoLog(programObjectId));

	        return validateStatus[0] != 0;
	}
	
	public static int buildProgram(String vertexShaderSource, String fragmentShaderSource)
	{
		int program;
		
		int vertexShader = compileVertexShader(vertexShaderSource);
		int fragmentShader = compileFragmentShader(fragmentShaderSource);
		
		program = linkProgram(vertexShader,fragmentShader);
		
		
		validateProgram(program);
		
		return program;
	}
}
