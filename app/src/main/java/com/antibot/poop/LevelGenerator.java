package com.antibot.poop;

import com.antibot.poop.gameobj.DumboebaGroup;
import com.antibot.poop.gameobj.WallBlock;
import com.framework.utils.Logger;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class LevelGenerator
{
    public static final byte CODE_SCENE = 1, CODE_GROUP = 2;
    public static final byte CODE_FOOD = 100, CODE_COIN = 101, CODE_BIG_B = 102, CODE_DUMBO_GROUP = 103, CODE_SHOOTER=104, CODE_WALL_BLOCK = 105, CODE_ORB=110, CODE_BOMB = 111, CODE_GEM = 112;

    public static final byte CODE_SIZE_SMALL = 0, CODE_SIZE_MED = 1, CODE_SIZE_LARGE = 2, CODE_SIZE_RAND = 3;



    SceneLoader sceneLoader;

    float ref;


    float currentSceneHeight;
    private float currentGroupX,currentGroupY,currentGroupProbabilityWidth,currentGroupProbabilityHeight;
    private float topCameraPosition;
    private float tempX, tempY;

    public LevelGenerator()
	{
       sceneLoader = new SceneLoader();

       reset();

	}

    public void reset()
    {
        ref = 0;
        currentSceneHeight = 1f;
    }


    public void deltaY(float delY)
    {
        ref+= delY;
    }

    public void update(float deltaTime)
    {
        if(ref >= currentSceneHeight)
        {
            if(sceneLoader.byteBufferReadyToAccess)
            {

                processScene(sceneLoader.byteBuffer);


                //since filling buffer happens in ui thread, can't access byteBuffer
                sceneLoader.byteBufferReadyToAccess = false;
                sceneLoader.prepareScene(0);

                ref = 0;
            }

        }
    }

    /**
     * processes the scene in the bytebuffer and adds to list
     * @param byteBuffer
     */
    private void processScene(ByteBuffer byteBuffer)
    {
        topCameraPosition = Static.cam.bottomPos + Static.TARGET_HEIGHT + 0.5f;  // 0.5 is padding
        byte scene = byteBuffer.get();

        if (scene == CODE_SCENE) {
            currentSceneHeight = byteBuffer.getFloat();

            while (true) {
                if (byteBuffer.get() == CODE_GROUP)
                    processGroup(byteBuffer);
                else
                    break;
            }
        }

    }

    private void processGroup(ByteBuffer byteBuffer){

        currentGroupX = byteBuffer.getFloat();
        currentGroupY = byteBuffer.getFloat();
        currentGroupProbabilityWidth = Static.rand.nextFloat()*byteBuffer.getFloat();
        currentGroupProbabilityHeight = Static.rand.nextFloat()*byteBuffer.getFloat();




        while (true) {
            byte b = byteBuffer.get();
            if (b == CODE_GROUP)
                break;
            else {
                if (b == CODE_FOOD) {
                    processFood(byteBuffer);
                } else if (b == CODE_COIN) {
                    processCoin(byteBuffer);
                } else if (b == CODE_BIG_B) {
                    processBigB(byteBuffer);
                } else if (b == CODE_DUMBO_GROUP) {
                    processDumboGroup(byteBuffer);
                } else if (b == CODE_SHOOTER) {
                    processShooter(byteBuffer);
                } else if (b == CODE_WALL_BLOCK) {
                    processWallBlock(byteBuffer);
                } else if (b == CODE_ORB) {
                    processOrb(byteBuffer);
                } else if (b == CODE_BOMB) {
                    processBomb(byteBuffer);
                } else if (b == CODE_GEM) {
                    processGem(byteBuffer);
                }



                byteBuffer.get(); // end of element
            }


        }
    }

    private void calculateXandY(ByteBuffer byteBuffer)
    {
        tempX = byteBuffer.getFloat() + currentGroupX + currentGroupProbabilityWidth;
        tempY = byteBuffer.getFloat() + topCameraPosition + currentGroupY + currentGroupProbabilityHeight;
    }


    private void processFood(ByteBuffer byteBuffer) {
        calculateXandY(byteBuffer);

        byte considerGlobal = byteBuffer.get();

        Static.objHandler.addFoodToWaitingList(tempX,tempY);

    }

    private void processCoin(ByteBuffer byteBuffer){
        calculateXandY(byteBuffer);

        Static.objHandler.addCoinToWaitingList(tempX,tempY);
    }


    private void processDumboGroup(ByteBuffer byteBuffer) {

        ArrayList<DumboebaGroup.Node> nodeList = DumboebaGroup.tempNodeList;

        int numberOfNodes = byteBuffer.getInt();



        for (int i = 0; i < numberOfNodes; i++) {

            calculateXandY(byteBuffer);
            byte hasDumboByte = byteBuffer.get();
            boolean hasDumbo = false;
            if(hasDumboByte == 01)
            {
                hasDumbo = true;
            }


            DumboebaGroup.Node node = nodeList.get(i);
            node.posX = tempX;
            node.posY = tempY;
            node.hasDumbo = hasDumbo;

        }

        Static.objHandler.addDumboGroup(numberOfNodes);

    }

    private void processBigB(ByteBuffer byteBuffer) {
        calculateXandY(byteBuffer);
        byte sizeCode = byteBuffer.get();

        Static.objHandler.addBigBaddyToWaitingList(tempX,tempY,sizeCode);

    }

    private void processWallBlock(ByteBuffer byteBuffer) {
        calculateXandY(byteBuffer);

        Static.objHandler.addWallBlockToWaitingList(tempX,tempY);
    }

    private void processShooter(ByteBuffer byteBuffer) {
        calculateXandY(byteBuffer);

        Static.objHandler.addShooterToWaitingList(tempX, tempY);
    }

    private void processOrb(ByteBuffer byteBuffer) {
        calculateXandY(byteBuffer);
        float probability = byteBuffer.getFloat();

        if(probability(probability))
            Static.objHandler.addOrbToWaitingList(tempX,tempY);

    }

    private void processBomb(ByteBuffer byteBuffer)
    {
        calculateXandY(byteBuffer);

        Static.objHandler.addBombToWaitingList(tempX, tempY);
    }

    private void processGem(ByteBuffer byteBuffer)
    {
        calculateXandY(byteBuffer);
        float probability = byteBuffer.getFloat();

        if(probability((probability)))
        {
            Static.objHandler.addGemToWaitingList(tempX,tempY);
        }
    }


    private void genSampleScene()
    {
        float baseY = Static.cam.bottomPos + Static.TARGET_HEIGHT;

        Static.objHandler.addWallBlockToWaitingList(Static.TARGET_WIDTH_BY_TWO - WallBlock.LENGTH, baseY + 1);
        Static.objHandler.addWallBlockToWaitingList(Static.TARGET_WIDTH_BY_TWO, baseY + 1);
        Static.objHandler.addWallBlockToWaitingList(Static.TARGET_WIDTH_BY_TWO + WallBlock.LENGTH, baseY + 1);

        Static.objHandler.addWallBlockToWaitingList(Static.TARGET_WIDTH_BY_TWO - WallBlock.LENGTH, baseY + 1 + WallBlock.LENGTH);
        Static.objHandler.addWallBlockToWaitingList(Static.TARGET_WIDTH_BY_TWO, baseY + 1 + WallBlock.LENGTH);
        Static.objHandler.addWallBlockToWaitingList(Static.TARGET_WIDTH_BY_TWO + WallBlock.LENGTH, baseY + 1 + WallBlock.LENGTH);
    }

    private boolean probability(float p)
    {
       return (Static.rand.nextFloat() <= p);
    }
	
}
