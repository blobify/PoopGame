package com.antibot.food;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_REPEAT;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.orthoM;

import java.util.ArrayList;
import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.gameobj.DumboebaGroup;
import com.antibot.food.gameobj.Particle;
import com.antibot.ui.UIElement;
import com.antibot.ui.UILayout;
import com.framework.shaderPrograms.ColorShaderProgram;
import com.framework.shaderPrograms.TextureShaderProgram;
import com.framework.utils.ArrayIntList;
import com.framework.utils.MyArrayList;
import com.game.framework.Game;
import com.game.framework.gl.SpriteBatcher;

public class WorldRenderer
{
	public static SpriteBatcher batcher = new SpriteBatcher(1000);
	public static TextureShaderProgram texShaderProgram;	
	public static ColorShaderProgram colorShaderProgram;
	
	public static final float projectionMatrix[] = new float[16];
	
	public static void presentMainMenu(UIElement e)
	{
		clearAndSetMatrix();
		
		//drawing background
		colorShaderProgram.useProgram();
		colorShaderProgram.setUniforms(projectionMatrix, Assets.ui_atlas, World.background.colorInterpolater.color);
		
		World.background.setVerticesAndAttribLocations(colorShaderProgram.getPositionAttribLocation(), colorShaderProgram.getTextureCoordinatesAttributeLocation(), Static.cam.bottomPos);
	
		World.background.draw();
		
		texShaderProgram.useProgram();
		texShaderProgram.setUniformsAndBindTexture(projectionMatrix, Assets.ui_atlas);

        texShaderProgram.setAlpha(1);

		batcher.beginBatch();	
		
		e.draw();
		
		batcher.endBatch(texShaderProgram);			
	}

    public static void presentGameSettings(UIElement layout)
    {
        clearAndSetMatrix();

        colorShaderProgram.useProgram();
        colorShaderProgram.setUniforms(projectionMatrix, Assets.ui_atlas, World.background.colorInterpolater.color);

        World.background.setVerticesAndAttribLocations(colorShaderProgram.getPositionAttribLocation(), colorShaderProgram.getTextureCoordinatesAttributeLocation(), Static.cam.bottomPos);

        World.background.draw();

        texShaderProgram.useProgram();
        texShaderProgram.setUniformsAndBindTexture(projectionMatrix, Assets.ui_atlas);
        texShaderProgram.setAlpha(1);

        batcher.beginBatch();

        layout.draw();

        batcher.endBatch(texShaderProgram);


    }

	public static void presentRunning()
	{		
		World world = Static.world;
		clearAndSetMatrix();
		
		//drawing background
		colorShaderProgram.useProgram();
		colorShaderProgram.setUniforms(projectionMatrix, Assets.ui_atlas, World.background.colorInterpolater.color);
		
		World.background.setVerticesAndAttribLocations(colorShaderProgram.getPositionAttribLocation(), colorShaderProgram.getTextureCoordinatesAttributeLocation(), Static.cam.bottomPos );
		
		
		World.background.draw();	
		
		texShaderProgram.useProgram();
		texShaderProgram.setUniformsAndBindTexture(projectionMatrix, Assets.ui_atlas);

        texShaderProgram.setAlpha(1);
		
		batcher.beginBatch();
		
		Font.drawFont(batcher, "GO MUSCA!!", Static.TARGET_WIDTH/2, Static.TARGET_HEIGHT/2, Assets.fnt_playtime, 1 , true);
		
		drawDumboebaPathPoints();
		
		drawThemCollidableObjects();
		
		drawParticles();

        world.nitro.draw();
				
		batcher.endBatch(texShaderProgram);
	}
	
	
	private static void drawDumboebaPathPoints()
	{		
		MyArrayList<DumboebaGroup> dumboGroupList = Static.objHandler.activeDumboebaGroup;
		
		for(int i=0; i<dumboGroupList.size(); i++)
		{
			DumboebaGroup group = dumboGroupList.get(i);
			group.draw();
		}
	}
	
	private static void drawThemCollidableObjects()
	{	
		
		ArrayIntList[] list = Static.objHandler.drawPriority;
		
		for(int i=0; i<list.length; i++)
		{
			for(int j=0; j<list[i].size(); j++)
			{
				CollidableObject obj = Static.objHandler.activeCollidableObjectList.get(list[i].get(j));
				obj.draw();
			}
		}
	}
	
	public static void presentPerkSelection(UILayout layout)
	{


		World world = Static.world;
		clearAndSetMatrix();
		
		texShaderProgram.useProgram();
		texShaderProgram.setUniformsAndBindTexture(projectionMatrix, Assets.ui_atlas);

        texShaderProgram.setAlpha(1);
		
		batcher.beginBatch();
		
		layout.draw();

        world.nitro.draw();

		batcher.endBatch(texShaderProgram);
	}
	


	private static void drawParticles()
	{
		ArrayList<Particle> activeParticleList = Static.objHandler.activeParticleList;
		
		for(int i =0; i<activeParticleList.size(); i++)
		{	
			Particle p = activeParticleList.get(i);
			p.draw();
		}
		
	}
	
	public static void onResume(Game game)
	{

		
		glEnable(GL_BLEND);
		
		glBlendFunc(GL_ONE,  GL_ONE_MINUS_SRC_ALPHA);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        //glClearColor(1f,0.8f,0.8f,1);
	}
	
	public static void onSurfaceChanged(int width, int height)
	{
		glViewport(0, 0, width, height)	;	
		orthoM(projectionMatrix, 0, 0, Static.TARGET_WIDTH , 0,Static.targetHeightFixer, -1, 1);
	}
	
	public static void clearAndSetMatrix()
	{
        //glClearColor(1,0.4f,0.4f,1);

		glClear(GL_COLOR_BUFFER_BIT);
		
		orthoM(projectionMatrix, 0, 0, Static.TARGET_WIDTH ,Static.cam.bottomPos,Static.cam.bottomPos+Static.targetHeightFixer, -1, 1);
		
	}

    public static void reloadShaderPrograms(Game game) {
       texShaderProgram = new TextureShaderProgram(game.getContext());
       colorShaderProgram = new ColorShaderProgram(game.getContext());

    }
}
