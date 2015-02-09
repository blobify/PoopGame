package com.antibot.ui;

import com.antibot.food.Assets;
import com.antibot.food.WorldRenderer;
import com.framework.shaderPrograms.TextureShaderProgram;
import com.game.framework.gl.SpriteBatcher;
import com.game.framework.gl.TextureRegion;

public class Slider extends UILayout implements Clickable {


    float sliderStepMin, sliderStepMax, sliderStepVal;
    int stepCount, currentStep;

    float bobMinX, bobStepVal, bobPosX;




    boolean inDragMode;

    float bobScale;

    public Slider( float width, float height, float bobScale, float sliderStepMin, float sliderStepMax, int stepCount)
    {
        super(width,height,Assets.slider_base);

        this.sliderStepMin = sliderStepMin;
        this.sliderStepMax = sliderStepMax;

        this.stepCount = stepCount;

        sliderStepVal = (sliderStepMax - sliderStepMin)/stepCount;

        this.width = width; this.height = height;
        this.bobScale = bobScale;

        bobStepVal = width/stepCount;

    }



    public float calculateSliderValue()
    {
        float sliderVal = sliderStepMin + sliderStepVal*currentStep;

        //simple security measure
        if(sliderVal < sliderStepMin) sliderVal = sliderStepMin;
        else if(sliderVal > sliderStepMax) sliderVal = sliderStepMax;

        return sliderVal;
    }

    public int getCurrentStep()
    {
        return currentStep;
    }


    public void calculateBobPosX(int currentStep)
    {
        this.currentStep  = currentStep;

        if(this.currentStep < 0) this.currentStep  = 0;

        else if(this.currentStep > stepCount) this.currentStep = stepCount;

        calculateBobPosX();

    }

    public void calculateBobPosX()
    {
        bobPosX = bobMinX + currentStep * scale * bobStepVal;
    }

    @Override
    public boolean onTouchDown(float touchX, float touchY) {

        float bobSizeByTwo = bobScale/2;
        float bobRight = bobPosX + bobSizeByTwo;
        float bobLeft = bobPosX - bobSizeByTwo;
        float bobTop = posY + bobSizeByTwo;
        float bobBottom = posY - bobSizeByTwo;

        if(touchX < bobRight && touchX > bobLeft && touchY < bobTop && touchY > bobBottom)
        {
            inDragMode = true;
            return true;
        }

        return false;
    }

    @Override
    public boolean onTouchDrag(float touchX, float touchY) {
        if(inDragMode)
        {
            float delta = touchX - bobPosX;
            float deltaAbs = Math.abs(delta);

            while(deltaAbs > bobStepVal) {

                if (delta < 0)
                {
                    currentStep--;

                    if(currentStep<0)
                    {
                        currentStep = 0;
                    }
                } else
                {
                    currentStep++;

                    if(currentStep > stepCount)
                    {
                        currentStep = stepCount;
                    }
                }

                calculateBobPosX();
                deltaAbs -= bobStepVal;
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean onTouchUp(float touchX, float touchY) {
        if(inDragMode)
        {
            inDragMode = false;

            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void draw() {

        SpriteBatcher batcher = WorldRenderer.batcher;
        TextureShaderProgram textShaderProgram = WorldRenderer.texShaderProgram;

        batcher.drawCarefulSprite(posX, posY, width, height, Assets.slider_base,textShaderProgram);

        TextureRegion bobRegion = (inDragMode)?(Assets.slider_button_down):(Assets.slider_button_up);

        batcher.drawCarefulSprite(bobPosX,posY,bobRegion.drawWidth*bobScale*scale,bobRegion.drawHeight*bobScale*scale,bobRegion,textShaderProgram);

    }

    @Override
    public void draw(float delX, float delY) {
        // not implemented
    }

    @Override
    public void onParentPosSet() {

        this.posX = parent.posX + relX;

        this.posX = parent.posX + relX; this.posY = parent.posY + relY;

        bobMinX = posX - width*scale/2;

        calculateBobPosX();

    }

    @Override
    public void onParentScaleSet() {

       /* scale(parent.currentScale);

        this.posX = parent.posX + relX * parent.currentScale;
        this.posY = parent.posY + relY * parent.currentScale;

        bobMinX = posX - width*scale/2;

        calculateBobPosX(currentStep);
        */


    }

    @Override
    public void popUp() {
        inDragMode = false;
    }

    @Override
    public boolean isClickable() {
        return true;
    }


}
