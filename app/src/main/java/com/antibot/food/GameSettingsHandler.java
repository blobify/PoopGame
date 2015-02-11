package com.antibot.food;

import com.antibot.ui.Button;
import com.antibot.ui.FontLabel;
import com.antibot.ui.GenericLayout;
import com.antibot.ui.OvershootInterpolator;

import com.antibot.ui.Slider;
import com.framework.utils.Logger;
import com.game.framework.Input;

public class GameSettingsHandler extends GameStateHandler {

    public static final int ENDED = 1, STARTED = 2, REWIND_AND_EXIT = 3;

    public static final float SENSI_MIN_VALUE = 0.4f, SENSI_MAX_VALUE = 2f;
    public static final int SENSI_STEP_COUNT = 40;

    public int animeState;

    public OvershootInterpolator interpolator;

    GenericLayout genericLayout;
    Button buttonAccelMode;
    Button buttonTouchMode;

    Slider slider;



    boolean movementMethod;

    public static final float EXTREME_RIGHT_POS = Static.TARGET_WIDTH + 4, WORKING_POS = Static.TARGET_WIDTH_BY_TWO;

    public GameSettingsHandler(World parent)
    {
        super(World.GAME_SETTINGS,parent);

        createInterpolator();

        constructUILayout();
    }

    private void createInterpolator()
    {
        interpolator = new OvershootInterpolator(new OvershootInterpolator.InterpolationCompleteListener() {

            @Override
            public void onInterpolationComplete(int id) {
                if (id == STARTED) {
                    genericLayout.enable();
                }
                else if (id == REWIND_AND_EXIT) {
                    parent.setChangeStateFlag(World.MAIN_MENU);
                    return;
                }
                animeState = ENDED;

            }
        });


    }

    private void constructUILayout()
    {
        FontLabel labelChangeBG = new FontLabel(Assets.fnt_purisa,18);
        labelChangeBG.set("Change Background",true,0.65f);

        Button buttonChangeBG = new Button(1.1f,Assets.button_generic_ms,null);
        buttonChangeBG.addElement(labelChangeBG,0,0);


        FontLabel labelChooseMovementMethod = new FontLabel(Assets.fnt_playtime,23);
        labelChooseMovementMethod.set("Choose Movement Method", true, 0.7f);


        Button.OnClickListener buttonAccelModeClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(Button button) {


                movementMethod = false;
                buttonTouchMode.popUp();

            }
        };

        Button.OnClickListener buttonTouchModeClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(Button button) {


                movementMethod = true;
                buttonAccelMode.popUp();

            }
        };




        buttonAccelMode = new Button(1.1f,Assets.button_generic_ms,buttonAccelModeClickListener);
        buttonTouchMode = new Button(1.1f,Assets.button_generic_ms,buttonTouchModeClickListener);




        FontLabel labelAccelerometer = new FontLabel(Assets.fnt_purisa,14);
        labelAccelerometer.set("Accelerometer",true,0.7f);

        FontLabel labelTouch = new FontLabel(Assets.fnt_purisa,6);
        labelTouch.set("Touch",true,0.8f);

        buttonAccelMode.addElement(labelAccelerometer,0,0);
        buttonTouchMode.addElement(labelTouch,0,0);

        FontLabel labelSetMovementSensi = new FontLabel(Assets.fnt_playtime,24);
        labelSetMovementSensi.set("Set Movement Sensitivity",true,0.65f);

        slider = new Slider(Assets.slider_base.drawWidth, Assets.slider_base.drawHeight,1,SENSI_MIN_VALUE,SENSI_MAX_VALUE,SENSI_STEP_COUNT);


        genericLayout =  new GenericLayout(Static.TARGET_WIDTH,Static.targetHeightFixer,Assets.rect_white);
        genericLayout.setPos(Static.TARGET_WIDTH_BY_TWO,Static.targetHeightFixerByTwo);
        genericLayout.addElement(buttonChangeBG,0,Static.targetHeightFixerByTwo - 1f);

        genericLayout.addElement(labelChooseMovementMethod, 0, Static.targetHeightFixerByTwo - 2.1f);


        genericLayout.addElement(buttonAccelMode, 0, Static.targetHeightFixerByTwo - 3f);

        genericLayout.addElement(buttonTouchMode, 0, Static.targetHeightFixerByTwo - 4f);

        genericLayout.addElement(labelSetMovementSensi, 0, Static.targetHeightFixerByTwo - 5f);

        genericLayout.addElement(slider,0, Static.targetHeightFixerByTwo - 5.8f);

    }



    @Override
    public void update(float deltaTime) {
        genericLayout.update(deltaTime);


        if(animeState > ENDED)
        {
            genericLayout.setPosX(interpolator.getInterpolation(deltaTime));
        }

    }

    @Override
    public void draw() {

        WorldRenderer.presentGameSettings(genericLayout);
    }

    @Override
    public void onStart(char msg) {

        genericLayout.disable();

        genericLayout.setPosX(EXTREME_RIGHT_POS);


        animeState = STARTED;
        interpolator.set(EXTREME_RIGHT_POS,WORKING_POS,3,STARTED,2);



        //set the slider initial value from preferences here

        ///slider.calculateBobPosX(Static.preferencesHandler.retrieveKeyAmount(PreferencesHandler.KEY_MOVEMENT_SENSI_AMOUNT, 15));  //default caluclated such that sensi val is 1 initially
        //change this^
        slider.setStepFromVal(Static.preferencesHandler.getMovementSensiAmount());  //changed version

        // set the prefered movement method here

        movementMethod = Static.preferencesHandler.retrieveKeyBoolean(PreferencesHandler.KEY_MOVEMENT_MODE,false); // false for accel mode
        if(movementMethod == false)
        {

            //buttonTouchMode.popUp();
        }
        else
        {
            //buttonTouchMode.popIn();
            //buttonAccelMode.popUp();
        }

        Static.comboTouchHandler.setMultiTouchMode();


    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void end(char msg) {

        /////////////
        genericLayout.popUp();

        final float movementSensi = slider.calculateSliderValue();

        Static.preferencesHandler.setMovementSensiAmount( movementSensi );

        //Logger.log("movement sensi set to " + Static.preferencesHandler.getMovementSensiAmount() + " calc value " + movementSensi);

        Static.comboTouchHandler.setSingleTouchMode();

    }


   /* public float calculateSensiValue(int currentStep)
    {
        float sliderVal = SENSI_MIN_VALUE + (SENSI_MAX_VALUE - SENSI_MIN_VALUE)*currentStep/SENSI_STEP_COUNT;

        //simple security measure
        if(sliderVal < SENSI_MIN_VALUE) sliderVal = SENSI_MIN_VALUE;
        else if(sliderVal > SENSI_MAX_VALUE) sliderVal = SENSI_MAX_VALUE;

        return sliderVal;
    }*/

    @Override
    public boolean onKeyPressed(Input.KeyEvent event) {
        if (isBackPressed(event)) {
            //rewind and exit
            animeState = REWIND_AND_EXIT;
            interpolator.set(interpolator.currentPos,EXTREME_RIGHT_POS,5,REWIND_AND_EXIT,2);

            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchDown(float touchX, float touchY) {
        return genericLayout.onTouchDown(touchX,touchY);
    }

    @Override
    public boolean onTouchDrag(float touchX, float touchY) {
        return genericLayout.onTouchDrag(touchX, touchY);
    }

    @Override
    public boolean onTouchUp(float touchX, float touchY) {
        return genericLayout.onTouchUp(touchX, touchY);
    }
}
