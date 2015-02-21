package com.antibot.food;



import com.antibot.ui.Button;
import com.antibot.ui.FontLabel;
import com.antibot.ui.GenericLayout;
import com.antibot.ui.ImageView;
import com.antibot.ui.OvershootInterpolator;
import com.framework.shaderPrograms.TextureShaderProgram;

import com.game.framework.Input;
import com.game.framework.gl.SpriteBatcher;

/**
 * pops up when musca is killed
 */
public class GR_SS_DialogEnd extends GameStateHandlerWithSubStates {

    public static final int DIALOG_INTERMEDIATE = 0, DIALOG_END = 1;

    public static final float MAX_OVERLAY_ALPHA = 0.8f;

    public static final char MSG_SHRINK_STARTED = 'x', MSG_SHRINK_COMPLETE_RETURN_TO_MAIN = 'y', MSG_SHRINK_AND_CONTINUE_GAME = 'z';
    public static final int STATE_ACTIVE = 2, STATE_DEATH_PHASE = 3;  //death phase wehre alpha reduces to 0

    float overlayAlpha;

    int state;
    char msgReceived;

    public GR_SS_DialogEnd(GameRunningHandler parent) {

        super(GameRunningHandler.GR_SS_DIALOG_END, parent);

        subStateArr = new GameStateHandler[2];
        subStateArr[DIALOG_INTERMEDIATE] = new DialogIntermediateHandler(this);
        subStateArr[DIALOG_END] = new DialogEndHandler(this);


    }

    private void createUILayouts() {

      /*  //creating game end dialog materials

        labelDistanceTravelled = new FontLabel2(Assets.fnt_purisa, 30);
        labelCoinsCollected = new FontLabel2(Assets.fnt_purisa, 30);

        Button.OnClickListener exitButtonListener = new Button.OnClickListener() {
            @Override
            public void onClick(Button button) {
                layoutGameEnd.popUp();
                layoutGameEnd.disable();
                subState = DIALOG_END_END;
                interpolator.set(1, 0, 3, subState, 0);
            }
        };


        exitButton = new Button(2, 1.2f, 1.1f, Assets.button_generic, exitButtonListener);

        layoutGameEnd = new GenericLayout(Static.TARGET_WIDTH / 1.25f, 4, Assets.rect_white);
        layoutGameEnd.addElement(labelDistanceTravelled, 0, 1);
        layoutGameEnd.addElement(labelCoinsCollected, 0, 0);
        layoutGameEnd.addElement(exitButton, 0, -layoutDialogIntermediate.height / 2 + exitButton.height);*/

    }


    @Override
    public void update(float deltaTime) {

        //changeStateIf();

        current.update(deltaTime);

        if(state == STATE_ACTIVE)
        {
            if(overlayAlpha < MAX_OVERLAY_ALPHA)
            {
                overlayAlpha += 2*deltaTime;

                if(overlayAlpha > MAX_OVERLAY_ALPHA) overlayAlpha = MAX_OVERLAY_ALPHA;
            }
        }
        else if(state == STATE_DEATH_PHASE)
        {
            overlayAlpha -= 2*deltaTime;

            if(overlayAlpha <= 0 )
            {
                overlayAlpha = 0;

                if(msgReceived == MSG_SHRINK_COMPLETE_RETURN_TO_MAIN) {
                    parent.setChangeStateFlag(1, World.MAIN_MENU, MSG_NULL, MSG_NULL); //go to main menu which is 1 level above
                }
                else if(msgReceived == MSG_SHRINK_AND_CONTINUE_GAME) {

                   // Logger.log("CONTINUEING CONTINUELYING", "CONTINUE CONTINUE CONTINUE");
                    //setChangeStateFlag(1, GameRunningHandler.GR_SS_RUNNING, World.MSG_GR_SS_DIALOG_END, World.MSG_GR_SS_RUNNING);
                    parent.setChangeStateFlag(GameRunningHandler.GR_SS_RUNNING, World.MSG_GR_SS_DIALOG_END, World.MSG_GR_SS_RUNNING);
                }
            }
        }
    }

    @Override
    public void draw() {



        SpriteBatcher batcher = WorldRenderer.batcher;
        TextureShaderProgram texShaderProgram = WorldRenderer.texShaderProgram;


        batcher.prepareForDrawingAlpha(overlayAlpha,texShaderProgram);
        batcher.drawCarefulSprite(Static.TARGET_WIDTH_BY_TWO,Static.cam.bottomPos + Static.targetHeightFixerByTwo,Static.TARGET_WIDTH,Static.targetHeightFixer,Assets.rect_black,texShaderProgram);
        batcher.finalizeDrawingAlpha(texShaderProgram);

        current.draw();

        batcher.endBatch(texShaderProgram);
    }

    @Override
    public void onStart(char msg) {

        //Logger.log("DIALOG START");

        current = subStateArr[DIALOG_INTERMEDIATE];
        current.onStart(msg);

        overlayAlpha = 0;
        state = STATE_ACTIVE; msgReceived = MSG_NULL;


    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void end(char msg) {
       // Logger.log("DIALOG END");
        current.end(msg);
    }

    @Override
    public boolean onKeyPressed(Input.KeyEvent event) {
        return true; // consume keyevents
    }

    @Override
    public boolean onTouchDown(float touchX, float touchY) {
        return current.onTouchDown(touchX, touchY);
    }

    @Override
    public boolean onTouchDrag(float touchX, float touchY) {
        return false;
    }

    @Override
    public boolean onTouchUp(float touchX, float touchY) {
        return current.onTouchUp(touchX, touchY);
    }

    @Override
    public void receiveMessage(char someMessage) {

       msgReceived = someMessage; // store the message since it will be processed in update()

       if(msgReceived == MSG_SHRINK_STARTED)
       {
           state = STATE_DEATH_PHASE;
       }
    }






    /*@Override
    public void changeStateIf() {

        if (stateChangeFlag) {
            if (indexToChange < 0) {
                if (indexToChange == -2) {
                    parent.setChangeStateFlag(-1);
                }
            } else {
                super.changeStateIf();
            }
            stateChangeFlag = false;
        }
    }*/












    public class DialogIntermediateHandler extends GameStateHandler {
        public static final int TIMER_MAX = 4;
        public static final int ENLARGE_DIALOG = 200, ACTIVE = 201, SHRINK_DIALOG_TO_END_STATE = 202, SHRINK_DIALOG_TO_CONTINUE = 203;

        GenericLayout layout;  // if continue or not thingy

        FontLabel continueTimerLabel;
        FontLabel youDiedLabel;
        Button continueButton;
        ImageView imageViewGem;
        FontLabel fontLabelGemCost;

        ImageView imageViewGemAvailable;
        FontLabel fontLabelNumberOfGemAvailable;



        int state;
        float timerCount;
        OvershootInterpolator interpolator;

        boolean timeUp;

        public DialogIntermediateHandler(GR_SS_DialogEnd parent) {

            super(GR_SS_DialogEnd.DIALOG_INTERMEDIATE, parent);

            createInterpolator();

            createUILayout();
        }

        private void createInterpolator() {
            OvershootInterpolator.InterpolationCompleteListener listener = new OvershootInterpolator.InterpolationCompleteListener() {
                @Override
                public void onInterpolationComplete(int id) {
                    if (id == ENLARGE_DIALOG) {
                        state = ACTIVE;
                        layout.enable();
                    } else if (id == SHRINK_DIALOG_TO_END_STATE) {
                        parent.setChangeStateFlag(GR_SS_DialogEnd.DIALOG_END);
                    }
                    else if(id == SHRINK_DIALOG_TO_CONTINUE)
                    {

                        //parent.setChangeStateFlag(1,GameRunningHandler.GR_SS_RUNNING,World.MSG_GR_SS_DIALOG_END,World.MSG_GR_SS_RUNNING);
                        parent.receiveMessage(MSG_SHRINK_AND_CONTINUE_GAME);
                    }
                }
            };

            interpolator = new OvershootInterpolator(listener);
        }

        private void createUILayout() {
            layout = new GenericLayout(Static.TARGET_WIDTH / 1.5f, 4.5f, Assets.rect_black);

            youDiedLabel = new FontLabel(Assets.fnt_playtime, 10);
            youDiedLabel.set("you died", true, 1);

            continueTimerLabel = new FontLabel(Assets.fnt_purisa, 2);


            Button.OnClickListener continueButtonClickListener = new Button.OnClickListener() {
                @Override
                public void onClick(Button button) {

                        Static.preferencesHandler.setGemAmount(Static.preferencesHandler.retrieveGemAmount() - Static.session.continueGemAmount);
                        Static.session.increaseGemAmount();
                        Static.session.gemsCollectedInSession = 0;

                        state = SHRINK_DIALOG_TO_CONTINUE;
                        interpolator.set(1, 0, 2, SHRINK_DIALOG_TO_CONTINUE, 0);
                        layout.popUp();
                        layout.disable();

                        parent.receiveMessage(MSG_SHRINK_STARTED);

                }
            };

            continueButton = new Button(2.6f, 1.6f, 1.1f, Assets.button_generic_mm, continueButtonClickListener);
            continueButton.setDisablableOverlayDrawable();

            FontLabel continueLbl = new FontLabel(Assets.fnt_purisa, 10);
            continueLbl.set("continue?", false, 0.8f);
            continueButton.addElement(continueLbl, -continueButton.width / 2 + 0.3f,0);

            imageViewGem = new ImageView(0.5f,0.5f,Assets.gem);
            imageViewGemAvailable = new ImageView(0.5f,0.5f,Assets.gem);

            fontLabelGemCost = new FontLabel(Assets.fnt_purisa,3);
            fontLabelNumberOfGemAvailable = new FontLabel(Assets.fnt_purisa,8);

            continueButton.addElement(imageViewGem,0,-0.5f);
            continueButton.addElement(fontLabelGemCost,0.5f,-0.5f);


            layout.addElement(continueButton, 0, -layout.height / 2 + continueButton.height);
            layout.addElement(youDiedLabel, 0, 1f);
            layout.addElement(continueTimerLabel, 0, 0.5f);

            layout.addElement(imageViewGemAvailable,-layout.width/2+0.3f,layout.height/2-0.3f);
            layout.addElement(fontLabelNumberOfGemAvailable,-layout.width/2+0.3f+0.4f, layout.height/2-0.3f);

        }

        @Override
        public void update(float deltaTime) {

            if (interpolator.started) {
                layout.setScale(interpolator.getInterpolation(deltaTime));
            }

            if (state == ACTIVE) {
                float temp = timerCount;
                timerCount -= deltaTime;

                if (timerCount <= 0) {
                    timeUp = true;
                    state = SHRINK_DIALOG_TO_END_STATE;
                    layout.popUp();
                    layout.disable();
                    interpolator.set(1, 0, 4, state, 0);
                }

                if (temp > timerCount) {
                    continueTimerLabel.set((int) temp + "", true, 1);
                }

                layout.update(deltaTime);
            }
        }

        @Override
        public void draw() {

            layout.draw();

        }

        @Override
        public void onStart(char msg) {

            state = ENLARGE_DIALOG;
            timerCount = TIMER_MAX;
            layout.enableAllElements();
            layout.setPos(Static.TARGET_WIDTH_BY_TWO, Static.targetHeightFixerByTwo + Static.cam.bottomPos);



            continueTimerLabel.set(TIMER_MAX + "", true, 1);

            interpolator.set(0, 1, 3, state, 2);
            layout.setScale(0); //preventing jitter bug?

            timeUp = false;


            //updating total gem amount;
            Static.preferencesHandler.setGemAmount(Static.preferencesHandler.retrieveGemAmount()+Static.session.gemsCollectedInSession);
            fontLabelNumberOfGemAvailable.set("x" + Static.preferencesHandler.retrieveGemAmount(),false,0.7f);
            fontLabelGemCost.set("x" + Static.session.continueGemAmount,false,0.6f);

            continueButton.enable();  //check if this call is redundant

            if(Static.preferencesHandler.retrieveGemAmount() < Static.session.continueGemAmount)
            {
                continueButton.disable();
            }

            //Logger.log("DIALOG intermediate START");
        }

        @Override
        public void onPause() {

        }

        @Override
        public void onResume() {

        }

        @Override
        public void end(char msg) {

            layout.popUp();

            //Logger.log("DIALOG INTERMEDIATE END");

        }

        @Override
        public boolean onKeyPressed(Input.KeyEvent event) {
            return false;
        }

        @Override
        public boolean onTouchDown(float touchX, float touchY) {
            final float correctedTouchY = touchY + Static.cam.bottomPos;
            return layout.onTouchDown(touchX, correctedTouchY);
        }

        @Override
        public boolean onTouchDrag(float touchX, float touchY) {
            return false;
        }//we don't consume drag events

        @Override
        public boolean onTouchUp(float touchX, float touchY) {
            final float correctedTouchY = touchY + Static.cam.bottomPos;
            return layout.onTouchUp(touchX, correctedTouchY);
        }
    }


    //
    public class DialogEndHandler extends GameStateHandler {

        public static final int ENLARGE_DIALOG = 200, ACTIVE = 201, SHRINK_DIALOG = 202;
        public static final int INTERPOLATE_DISTANCE = 300, INTERPOLATE_COIN = 301;


        FontLabel labelDistanceTravelled;
        FontLabel labelCoinsCollected;
        GenericLayout layout;
        Button exitButton;
        OvershootInterpolator interpolator;

        OvershootInterpolator interpolator2;

        int state;


        public DialogEndHandler(GR_SS_DialogEnd parent) {
            super(GR_SS_DialogEnd.DIALOG_END, parent);

            createInterpolator();

            createUILayout();
        }

        private void createInterpolator() {
            interpolator = new OvershootInterpolator(new OvershootInterpolator.InterpolationCompleteListener() {
                @Override
                public void onInterpolationComplete(int id) {
                    if (id == ENLARGE_DIALOG) {
                        state = ACTIVE;

                        interpolator2.set(0, (int)Static.session.distanceTravelledInSession, 0.7f, INTERPOLATE_DISTANCE, 0);


                    } else if (id == SHRINK_DIALOG) {
                        //end(null);
                        //parent.setChangeStateFlag(2,World.MAIN_MENU, MSG_NULL, MSG_NULL); //go to main menu which is 2 levels above

                        parent.receiveMessage(MSG_SHRINK_COMPLETE_RETURN_TO_MAIN);
                    }

                }
            });

            interpolator2 = new OvershootInterpolator(new OvershootInterpolator.InterpolationCompleteListener() {
                @Override
                public void onInterpolationComplete(int id) {

                    if (id == INTERPOLATE_DISTANCE) {
                        //Logger.log("TAG", "SETTING TO INTERPOLOATE COIN!");
                        interpolator2.set(0, Static.session.coinsCollectedInSession, 1f, INTERPOLATE_COIN, 0);
                    } else if (id == INTERPOLATE_COIN) {
                        layout.enable();
                    }
                }
            });
        }

        private void createUILayout() {
            labelDistanceTravelled = new FontLabel(Assets.fnt_purisa, 30);
            labelCoinsCollected = new FontLabel(Assets.fnt_purisa, 30);

            Button.OnClickListener exitButtonListener = new Button.OnClickListener() {
                @Override
                public void onClick(Button button) {
                    layout.popUp();
                    layout.disable();
                    state = SHRINK_DIALOG;
                    interpolator.set(1, 0, 3, state, 0);

                    parent.receiveMessage(MSG_SHRINK_STARTED);  //start reducing alpha
                }
            };


            exitButton = new Button( 1.1f, Assets.button_generic_ss, exitButtonListener);

            FontLabel exLbl = new FontLabel(Assets.fnt_playtime, 5);
            exLbl.set("exit", true, 1);

            exitButton.addElement(exLbl, 0, 0);

            layout = new GenericLayout(Static.TARGET_WIDTH / 1.25f, 4.5f, Assets.rect_black);
            layout.addElement(labelDistanceTravelled, 0, 1);
            layout.addElement(labelCoinsCollected, 0, 0);
            layout.addElement(exitButton, 0, -layout.height / 2 + exitButton.height);
        }

        @Override
        public void update(float deltaTime) {

            if (interpolator.started) {
                layout.setScale(interpolator.getInterpolation(deltaTime));
            }

            if (interpolator2.started) {
                if (interpolator2.id == INTERPOLATE_DISTANCE) {
                    labelDistanceTravelled.set("dist : " + (int)interpolator2.getInterpolation(deltaTime), true, 1);
                } else if (interpolator2.id == INTERPOLATE_COIN) {
                    labelCoinsCollected.set("coins : " + (int) interpolator2.getInterpolation(deltaTime), true, 1);

                }
            }

            if (state == ACTIVE) {
                layout.update(deltaTime);
            }

        }

        @Override
        public void draw()
        {
            layout.draw();
        }

        @Override
        public void onStart(char msg) {
            state = ENLARGE_DIALOG;

            layout.setPos(Static.TARGET_WIDTH_BY_TWO, Static.targetHeightFixerByTwo + Static.cam.bottomPos);
            interpolator.set(0, 1, 3, state, 2);
            layout.setScale(0); // preventing that jitter bug

            layout.disable();

            //Logger.log("DIALOG END STARTS");


        }

        @Override
        public void onPause() {

        }

        @Override
        public void onResume() {

        }

        @Override
        public void end(char msg) {
            layout.disable();
            layout.popUp();
            //set coin amount
            Static.preferencesHandler.setCoinAmount(Static.preferencesHandler.retrieveCoinAmount() + Static.session.coinsCollectedInSession);

        }

        @Override
        public boolean onKeyPressed(Input.KeyEvent event) {
            return false;
        }

        @Override
        public boolean onTouchDown(float touchX, float touchY) {
            final float correctedTouchY = touchY + Static.cam.bottomPos;
            return layout.onTouchDown(touchX, correctedTouchY);
        }

        @Override
        public boolean onTouchDrag(float touchX, float touchY) {
            return false;
        }

        @Override
        public boolean onTouchUp(float touchX, float touchY) {
            final float correctedTouchY = touchY + Static.cam.bottomPos;
            return layout.onTouchUp(touchX, correctedTouchY);
        }
    }


}
