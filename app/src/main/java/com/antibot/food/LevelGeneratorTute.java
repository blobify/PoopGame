package com.antibot.food;

import com.antibot.food.gameobj.CollidableObject;
import com.antibot.food.gameobj.Food;
import com.antibot.food.gameobj.WallBlock;
import com.antibot.ui.ImageFontLabelLayout;
import com.antibot.ui.ImageView;
import com.antibot.ui.OvershootInterpolator;
import com.antibot.ui.PeriodicInterpolator;
import com.framework.shaderPrograms.TextureShaderProgram;

import com.game.framework.gl.SpriteBatcher;
import com.game.framework.gl.TextureRegion;

public class LevelGeneratorTute implements LevelGenerator.LevelGeneratorInterface , DrawWork{

    ImageFontLabelLayout imageFontLabelLayout1, imageFontLabelLayout2;
    //int imageFontLabelPoolIndex1, imageFontLabelPoolIndex2;

    static float ALPHA_MIN = 0.6f, ALPHA_MAX = 1;
    static int MODE_MOVE = 100, MODE_FOOD = 101, MODE_WALL_BURST = 102, MODE_GOOD_LUCK = 103, MODE_END = 104;
    static int SUBMODE_FADE_IN = 201, SUBMODE_STABLE = 202, SUBMODE_FADE_OUT = 203;

    static String STR_TILT_TO_MOVE = "tilt to move", STR_EAT = "eat to gain energy", STR_TOUCH = "touch to use energy", STR_COLLIDE = "collide with energy!", STR_GOOD_LUCK = "good luck!";


    int mode, subMode;

    OvershootInterpolator fadeInFadeOutInterpolator;

    PeriodicInterpolator alphaInterpolator;


    Food.FoodEatenListener foodEatenListener;
    Food food;
    float foodX,foodY, wallBlockY;

    float alpha;
    float refY;

    float SIX = 6f;
    float SCENE_HEIGHT_MAXY = Static.TARGET_HEIGHT + SIX + 2;  // +2 to get enuf time to display "good luck"
    float SCENE_HEIGHT_ACTUAL = Static.TARGET_HEIGHT + SIX;




    AnimeCounter animeCounter;
    AnimeCounter.AnimPackage animPackage;

    public LevelGeneratorTute()
    {
        imageFontLabelLayout1 = new ImageFontLabelLayout(1,1,null);
        imageFontLabelLayout2 = new ImageFontLabelLayout(1,1,null);


        OvershootInterpolator.InterpolationCompleteListener interpolationCompleteListener = new OvershootInterpolator.InterpolationCompleteListener() {
            @Override
            public void onInterpolationComplete(int id) {
                if(id == SUBMODE_FADE_IN)
                {
                    subMode = SUBMODE_STABLE;
                }
                else if(id == SUBMODE_FADE_OUT)
                {
                    mode++; // from move to food to wallburst to goodluck

                    setSubModeAndInitiateFade(SUBMODE_FADE_IN);

                    if(mode == MODE_FOOD)
                    {
                        setupFoodLayout();
                    }
                    else if(mode == MODE_WALL_BURST)
                    {
                        setupWallBurstLayout();
                    }
                    else if(mode == MODE_GOOD_LUCK)
                    {
                        setupGoodLuckLayout();
                    }
                    else if(mode == MODE_END)
                    {
                        imageFontLabelLayout1.hide();
                        imageFontLabelLayout2.hide();
                    }
                }
            }
        };

        fadeInFadeOutInterpolator = new OvershootInterpolator(interpolationCompleteListener);

        foodEatenListener = new Food.FoodEatenListener() {
            @Override
            public void onFoodEaten(CollidableObject theThingThatAteMe) {
                if(theThingThatAteMe.objectType == ObjectHandler.INDEX_MUSCA)
                {
                    setSubModeAndInitiateFade(SUBMODE_FADE_OUT);
                }
            }
        };



        animeCounter = new AnimeCounter();
        animPackage = new AnimeCounter.AnimPackage(Assets.indicator_phone_arr, AnimeCounter.ANIME_REVERSE, 3, 12);
        animeCounter.setAnime(animPackage);
        animeCounter.setReverseCompleteListener(new AnimeCounter.ReverseCompleteListener() {
            @Override
            public void onReverseComplete() {
                ImageView indicatorMobileView = imageFontLabelLayout1.giveImage1ForWork();
                indicatorMobileView.invertX();
            }
        });


        alphaInterpolator = new PeriodicInterpolator();
        alphaInterpolator.set(ALPHA_MIN,ALPHA_MAX,60);
    }

    @Override
    public int update(float deltaTime) {

        if(subMode == SUBMODE_STABLE) {
            alphaInterpolator.update(deltaTime);
            alpha = alphaInterpolator.getInterpolation();
        }
        else {
            alpha = fadeInFadeOutInterpolator.getInterpolation(deltaTime);
        }

        if(mode == MODE_MOVE)
        {
            animeCounter.udpate();
            TextureRegion indicatorPhoneRegion = animeCounter.getTextureRegion();
            imageFontLabelLayout1.giveImage1ForWork().setImage(indicatorPhoneRegion.drawWidth,indicatorPhoneRegion.drawHeight,indicatorPhoneRegion);
            imageFontLabelLayout1.addImage1BackToLayout(0,0.2f);

            if(animeCounter.animeComplete)
            {
                setSubModeAndInitiateFade(SUBMODE_FADE_OUT);
            }

        }
        else if(mode == MODE_FOOD)
        {

        }
        else if(mode == MODE_END)
        {
            return 1; // change it biatch
        }




        return 0;
    }

    private void setSubModeAndInitiateFade(int subModeToSet)
    {
        if(subModeToSet == this.subMode) return;

        this.subMode = subModeToSet;
        if(subModeToSet == SUBMODE_FADE_IN)
        {
            fadeInFadeOutInterpolator.set(0,ALPHA_MAX,3,subMode,0);
        }
        else if(subModeToSet == SUBMODE_FADE_OUT)
        {

            fadeInFadeOutInterpolator.set(alpha,0,3,subMode,0);
        }
    }

    @Override
    public void onStart(float userProgress) {

        Static.gameRunningHandler.addToAdditionalDrawList(this);



       // GenericPool<ImageFontLabelLayout> pool = NicePool.imageFontLabelLayoutGenericPool;
        //imageFontLabelPoolIndex1 = pool.allocateObject();
        //imageFontLabelLayout1 = pool.getObject(imageFontLabelPoolIndex1);

        //imageFontLabelPoolIndex2 = pool.allocateObject();
        //imageFontLabelLayout2 = pool.getObject(imageFontLabelPoolIndex2);
        imageFontLabelLayout1.unHide(); imageFontLabelLayout2.unHide();  // since we might have hidden it
        imageFontLabelLayout1.removeEverySingleElement();
        imageFontLabelLayout2.removeEverySingleElement();  //cleaning

        setupMoveLayout();

        animeCounter.resetAnime();

        generateTuteLevel();

    }


    private void setupMoveLayout()
    {
        imageFontLabelLayout1.setPos(Static.TARGET_WIDTH_BY_TWO,Static.cam.bottomPos + 2f);
        imageFontLabelLayout1.giveImage1ForWork().setImage(Assets.indicator_phone_1.drawWidth,Assets.indicator_phone_1.drawHeight,Assets.indicator_phone_1);
        imageFontLabelLayout1.addImage1BackToLayout(0,0.2f);

        imageFontLabelLayout1.giveLabel1ForWork().set(STR_TILT_TO_MOVE,true,0.9f);
        imageFontLabelLayout1.addLabel1BackToLayout(0,-0.4f);
    }

    private void setupFoodLayout()
    {
        TextureRegion texArrow = Assets.indicator_arrow;
        imageFontLabelLayout1.setPos(foodX,foodY - Food.RADIUS*3.2f);
        ImageView imgView = imageFontLabelLayout1.giveImage1ForWork();
        imgView.unsetInvertX();  //since we might have inverted it while showing tilt anime

        imgView.setImage(texArrow.drawWidth,texArrow.drawHeight,texArrow);

        boolean isFoodLeftOfScreen = (foodX < Static.TARGET_WIDTH_BY_TWO);
        float relX = -texArrow.drawWidth;

        if(isFoodLeftOfScreen)
        {
            imgView.invertX();
            relX = -relX;
        }

        imageFontLabelLayout1.addImage1BackToLayout(relX,0.2f);

        imageFontLabelLayout1.giveLabel1ForWork().set(STR_EAT,true,0.9f);
        imageFontLabelLayout1.addLabel1BackToLayout(0,-0.3f);
    }

    private void setupWallBurstLayout(){

        TextureRegion touchRegion = Assets.indicator_touch;
        imageFontLabelLayout1.setPos(Static.TARGET_WIDTH_BY_TWO, foodY + SIX /2);

        ImageView imageView = imageFontLabelLayout1.giveImage1ForWork();
        imageView.unsetInvertX();
        imageView.setImage(touchRegion.drawWidth, touchRegion.drawHeight, touchRegion);
        imageFontLabelLayout1.addImage1BackToLayout(0,0.2f);

        imageFontLabelLayout1.giveLabel1ForWork().set(STR_TOUCH,true,0.9f);
        imageFontLabelLayout1.addLabel1BackToLayout(0,-0.3f);


        imageFontLabelLayout2.setPos(Static.TARGET_WIDTH_BY_TWO,wallBlockY-WallBlock.LENGTH);
        imageFontLabelLayout2.giveLabel1ForWork().set(STR_COLLIDE,true,0.8f);
        imageFontLabelLayout2.addLabel1BackToLayout(0,0);




    }

    private void setupGoodLuckLayout()
    {
        imageFontLabelLayout1.hide();

        imageFontLabelLayout2.setPos(Static.TARGET_WIDTH_BY_TWO,Static.cam.bottomPos+Static.targetHeightFixer/2);
        imageFontLabelLayout2.giveLabel1ForWork().set(STR_GOOD_LUCK,true,1);
        imageFontLabelLayout2.addLabel1BackToLayout(0,0);
    }

    private void generateTuteLevel()
    {
        refY = 0;
        mode = MODE_MOVE;

        setSubModeAndInitiateFade(SUBMODE_FADE_IN);


        foodX = Static.TARGET_WIDTH/3 ;
        if(Static.rand.nextBoolean()) foodX += foodX;
        foodY = Static.cam.bottomPos+Static.targetHeightFixer;

        food = Static.objHandler.addFoodToWaitingList(foodX, foodY);
        food.setFoodEatenListener(foodEatenListener);
        //adding wallblocks

        wallBlockY = SCENE_HEIGHT_ACTUAL - 2*WallBlock.LENGTH;
        for(float i=0; i< WallBlock.WALL_BLOCKS_PER_LENGTH_OF_SCREEN; i+=WallBlock.LENGTH)
        {
            Static.objHandler.addWallBlockToWaitingList(i,wallBlockY);
        }


    }

    @Override
    public void onEnd()
    {
        Static.gameRunningHandler.removeFromAdditionalDrawList(this);
       // GenericPool<ImageFontLabelLayout> pool = NicePool.imageFontLabelLayoutGenericPool;



        //imageFontLabelLayout1 = pool.unallocateObject(imageFontLabelPoolIndex1);
        //imageFontLabelLayout2 = pool.unallocateObject(imageFontLabelPoolIndex2);
       // Logger.log("END of TUTE");

        food.unsetFoodEatenListener();
    }

    @Override
    public void deltaY(float delY)
    {
        //scale = scale-0.05f;
        //if(scale < 0) scale = 1;
        //imageFontLabelLayout1.setScale(scale);
        //imageFontLabelLayout1.setPos(imageFontLabelLayout1.posX, imageFontLabelLayout1.posY+delY);

        refY += delY;

        /*if(refY >= SCENE_HEIGHT)
        {
            mode = MODE_END;
        }*/

        if(refY > SCENE_HEIGHT_MAXY && mode < MODE_END)
        {
            setSubModeAndInitiateFade(SUBMODE_FADE_OUT);
        }

         if(refY > wallBlockY+WallBlock.LENGTH/2 && mode < MODE_GOOD_LUCK)
        {
            setSubModeAndInitiateFade(SUBMODE_FADE_OUT);
        }


        if(mode == MODE_MOVE ||  mode == MODE_WALL_BURST)
        {
            imageFontLabelLayout1.setPos(imageFontLabelLayout1.posX, imageFontLabelLayout1.posY+delY);
        }

    }


    @Override
    public void draw() {
        SpriteBatcher batcher = WorldRenderer.batcher;
        TextureShaderProgram texShaderProgram = WorldRenderer.texShaderProgram;

        batcher.prepareForDrawingAlpha(alpha,texShaderProgram);

        imageFontLabelLayout1.draw();
        imageFontLabelLayout2.draw();

        batcher.finalizeDrawingAlpha(texShaderProgram);
    }
}
