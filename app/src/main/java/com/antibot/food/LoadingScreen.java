package com.antibot.food;

import android.os.Environment;

import com.framework.shaderPrograms.TextureShaderProgram;
import com.game.framework.Screen;
import com.game.framework.gl.SpriteBatcher;
import com.game.framework.impl.GLGame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.Matrix.orthoM;

public class LoadingScreen extends Screen
{
	final GLGame game;

    private StartingScreen startingScreen;

    private long startTime;

    private volatile boolean loaded;

    private Thread loadThread;

    private TextureShaderProgram texShaderProgram;

    public static final int NUMBER_OF_LOAD_BLOBS = 6;
    public static final float DEFAULT_SIZE_OF_LOAD_BLOB = 0.1f;
    public static final float MAX_SIZE_OF_LOAD_BLOB = 0.2f;
    public static final float SEPARATION_BETWEEN_BLOBS = 0.5f;
    public static final float WIDTH_OF_LOAB_BLOBS = (NUMBER_OF_LOAD_BLOBS-1)*SEPARATION_BETWEEN_BLOBS;

    int currentEnlargedBlob;
    private volatile boolean dimensionsSet;

    public LoadingScreen(final GLGame game)
	{
		super(game);
		this.game = game;
		Static.game = game;

        startTime = System.currentTimeMillis();

        startLoadThread();

	}

    private void startLoadThread()
    {
        loaded = false;

        Runnable loader = new Runnable() {
            @Override
            public void run() {

                while(!game.surfaceCreated)
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(Static.LEVEL_GEN_MODE)  // changes, also in Static.sceneFileNotFound.. remove this and that when done
                {
                    Static.scenesFile = new File(Environment.getExternalStorageDirectory(), "antibot/gen/scene");
                }
                else {
                    copySceneFileFromAssetsToPhone();
                }
                Assets.loadObjects(game);
                startingScreen = new StartingScreen(game);



                long sleepTime = startTime + 1000 - System.currentTimeMillis();  // minimum 1 sec loading :D

                if(sleepTime > 0)
                {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                loaded = true;

            }
        };

        loadThread = new Thread(loader);
        loadThread.start();

    }

    private void copySceneFileFromAssetsToPhone() {

        try {

            InputStream inputStream = game.getAssets().open(Static.SCENE_FILE_NAME);

            File scenesFile;

            if (isExternalStorageWritable()) {

                Static.sceneFileStoredInInternalDir = false;

                scenesFile = new File(game.getExternalFilesDir(null), Static.SCENE_FILE_NAME);

            } else {

                    scenesFile = new File(game.getFilesDir(), Static.SCENE_FILE_NAME);
            }

            FileOutputStream fileOutputStream = new FileOutputStream(scenesFile);

            copyFile(inputStream,fileOutputStream);

            Static.scenesFile = scenesFile;



        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    private void copyFile(InputStream src, OutputStream dst) throws IOException
    {
        byte[] buff = new byte[1024];
        int read;
        while((read=src.read(buff)) != -1)
        {
            dst.write(buff, 0, read);
        }

        dst.flush();

        //dont forget to close this
        src.close();
        dst.close();

       // Logger.log("DONE COPYING FILE" , "DONE COPYING FILE");

    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
	public void update(float deltaTime)
	{
        if(!loaded)
        {
            currentEnlargedBlob++;
            if(currentEnlargedBlob >= NUMBER_OF_LOAD_BLOBS)
                currentEnlargedBlob = 0;
        }
        else
        {

            game.setScreen(startingScreen);
            return;
        }


	}

    @Override
	public void present(float deltaTime)
	{
        texShaderProgram.useProgram();
        texShaderProgram.setUniformsAndBindTexture(WorldRenderer.projectionMatrix, Assets.atlas);

        glClear(GL_COLOR_BUFFER_BIT);

        orthoM(WorldRenderer.projectionMatrix, 0, 0, Static.TARGET_WIDTH, 0, Static.targetHeightFixer, -1, 1);

        drawLoadBlobs();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void drawLoadBlobs()
    {
        SpriteBatcher batcher = WorldRenderer.batcher;

        batcher.beginBatch();

        float advancerX = Static.TARGET_WIDTH_BY_TWO - WIDTH_OF_LOAB_BLOBS/2;

        for(int i=0; i<NUMBER_OF_LOAD_BLOBS; i++)
        {
            if(i == currentEnlargedBlob)
            {
                batcher.drawSprite(advancerX,Static.targetHeightFixerByTwo,MAX_SIZE_OF_LOAD_BLOB,MAX_SIZE_OF_LOAD_BLOB,Assets.loadingScreenRegion);
            }
            else
            {
                batcher.drawSprite(advancerX,Static.targetHeightFixerByTwo,DEFAULT_SIZE_OF_LOAD_BLOB,DEFAULT_SIZE_OF_LOAD_BLOB,Assets.loadingScreenRegion);
            }

            advancerX+= SEPARATION_BETWEEN_BLOBS;
        }

        batcher.endBatch(texShaderProgram);
    }

	@Override
	public void pause()
	{
	
	}

	@Override
	public void resume()
	{
        texShaderProgram = new TextureShaderProgram(game.getContext());
    }

	@Override
	public void dispose()
	{
		
	}

	@Override
	public void onSurfaceChanged(int width, int height)
	{
		Static.setAllDimentionalConstants(width, height);

	}

}
