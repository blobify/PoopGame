package com.antibot.food;


import com.antibot.ui.Button;
import com.antibot.ui.FontLabel;
import com.antibot.ui.GenericLayout;
import com.antibot.ui.OvershootInterpolator;
import com.framework.shaderPrograms.TextureShaderProgram;
import com.game.framework.Input;
import com.game.framework.gl.SpriteBatcher;

public class GR_SS_DialogQuit extends GameStateHandler{

    public static final int ENLARGE_DIALOG = 200, ACTIVE = 201, SHRINK_DIALOG_TO_QUIT = 202, SHRINK_DIALOG_TO_CONTINUE = 203;
    GenericLayout layout;
    FontLabel labelHeading;

    Button okButton; FontLabel okLabel;
    Button cancelButton; FontLabel cancelLabel;

    OvershootInterpolator interpolator;

    float alpha = 0;
    private static final float ALPHA_TO_REACH =  0.8f;
    public boolean disablable = false;

    int state;

    public GR_SS_DialogQuit(GameRunningHandler parent)
    {
        super(GameRunningHandler.GR_SS_DIALOG_QUIT,parent);

        createInterpolator();

        createUILayout();
    }

    private void createInterpolator()
    {
        OvershootInterpolator.InterpolationCompleteListener listener = new OvershootInterpolator.InterpolationCompleteListener() {
            @Override
            public void onInterpolationComplete(int id) {
                if(id == ENLARGE_DIALOG)
                {
                    state = ACTIVE;
                    layout.enable();

                }
                else if(id == SHRINK_DIALOG_TO_QUIT)
                {
                    parent.setChangeStateFlag(1, World.MAIN_MENU, MSG_NULL, MSG_NULL); //go to main menu which is 1 level above
                }
                else if(id == SHRINK_DIALOG_TO_CONTINUE)
                {
                    parent.setChangeStateFlag(GameRunningHandler.GR_SS_RUNNING, World.MSG_GR_SS_DIALOG_QUIT, World.MSG_GR_SS_RUNNING);
                }

            }
        };

        interpolator = new OvershootInterpolator(listener);
    }

    private void createUILayout()
    {
        layout = new GenericLayout(Static.TARGET_WIDTH/1.5f, 4.5f, Assets.rect_black);

        labelHeading = new FontLabel(Assets.fnt_playtime,12);
        labelHeading.set("Quit?", true, 0.9f);

        Button.OnClickListener okButtonClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(Button button) {
                interpolator.set(1,0,2,SHRINK_DIALOG_TO_QUIT,0);
            }
        };

        okButton = new Button(2,1.5f,1.1f, Assets.button_generic_mm,okButtonClickListener);
        okLabel = new FontLabel(Assets.fnt_purisa,3);
        okLabel.set("ok",true,1);
        okButton.addElement(okLabel,0,0);

        Button.OnClickListener cancelButtonClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(Button button) {
                interpolator.set(1,0,2,SHRINK_DIALOG_TO_CONTINUE,0);
            }
        };

        cancelButton = new Button(2.2f, 1.5f, 1.1f, Assets.button_generic_mm, cancelButtonClickListener);
        cancelLabel = new FontLabel(Assets.fnt_purisa,6);
        cancelLabel.set("cancel",true,1);
        cancelButton.addElement(cancelLabel,0,0);

        layout.addElement(labelHeading,0,layout.height/4);

        layout.addElement(okButton, -layout.width/2 + okButton.width/2 + 0.2f , -layout.height/2 + okButton.height/2 + 0.2f);

        layout.addElement(cancelButton, layout.width/2 - cancelButton.width/2 - 0.2f , -layout.height/2 + cancelButton.height/2 + 0.2f);


    }

    @Override
    public void update(float deltaTime) {

        if (interpolator.started) {
            layout.setScale(interpolator.getInterpolation(deltaTime));
        }

        if(state == ACTIVE)
        {
            layout.update(deltaTime);
        }

    }

    @Override
    public void draw() {

        SpriteBatcher batcher = WorldRenderer.batcher;
        TextureShaderProgram texShaderProgram = WorldRenderer.texShaderProgram;

        WorldRenderer.presentRunning();

        layout.draw();

        batcher.endBatch(texShaderProgram);
    }

    @Override
    public void onStart(char msg) {
        state = ENLARGE_DIALOG;
        layout.enableAllElements();
        layout.setPos(Static.TARGET_WIDTH_BY_TWO, Static.targetHeightFixerByTwo + Static.cam.bottomPos);

        interpolator.set(0, 1, 3, state, 2);
        layout.setScale(0); //preventing jitter bug?


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
