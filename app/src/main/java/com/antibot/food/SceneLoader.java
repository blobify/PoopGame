package com.antibot.food;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class SceneLoader
{
    FileChannel fileChannel;
    ByteBuffer byteBuffer;

    public volatile boolean byteBufferReadyToAccess;

    SceneLoadRunnable sceneLoadRunnable;

    public SceneLoader()
    {
        try {
            fileChannel = new FileInputStream(Static.scenesFile).getChannel();
            Static.sceneFileNotFound = false;
        } catch (FileNotFoundException e) {

            Static.sceneFileNotFound = true;
            //throw new RuntimeException(e);
        }

        byteBuffer = ByteBuffer.allocate(1024);

        sceneLoadRunnable = new SceneLoadRunnable();

        byteBufferReadyToAccess = true;


    }

    public void prepareScene(int index)
    {
        sceneLoadRunnable.prepare(index);
        Static.game.handler.post(sceneLoadRunnable); // let ui thread handle loading :D

    }


    public class SceneLoadRunnable implements Runnable
    {
        int index;


        @Override
        public void run() {
            byteBufferReadyToAccess = false;

            try {
                fileChannel.position(index);

                byteBuffer.clear();

                fileChannel.read(byteBuffer);

                byteBuffer.flip();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            byteBufferReadyToAccess = true;
        }

        public void prepare(int index)
        {
            this.index = index;
        }
    }



}