package com.antibot.poop;



import com.antibot.poop.gameobj.Nitro;
import com.antibot.ui.FontLabel;
import com.antibot.ui.PerkButton;
import com.antibot.ui.Button;
import com.antibot.ui.GenericLayout;
import com.antibot.ui.OvershootInterpolator;

import com.game.framework.Input.KeyEvent;

public class PerkSelectionHandler extends GameStateHandler {
    public static final int NIL = 0, ENDED = 1, STARTED = 2, REWIND = 3, REWIND_AND_EXIT = 4;


    public int animState = NIL;
    public static final float LAYOUT_HEIGHT = 15;

    //Button getCoins;
    //PerkButton perkButtonHeadStart;
    public static final float MIN_TOUCH_DRAG = 0.1f;

    public World world;
    public float minBottomPos;


    //UILayout uiLayout;
    OvershootInterpolator interpolator;
    FontLabel coinLabel;
    Button startGameButton;

    PerkButton perkButtonHeadStart;
    PerkButton perkButtonMoreFood;
    PerkButton perkButtonLongerNitro;
    PerkButton perkButtonProtectiveOrb;

    PerkButton perkButtonFillNitro;
    PerkButton perkButtonIncreaseNitroCapacity;

    Button buttonBuyCoins;


    GenericLayout layout;

    boolean dragMode;
    float prevTouchY;

    private int coinInHand;
    private static final float EXTREME_RIGHT = Static.TARGET_WIDTH + 4;
    private static final float WORKING_POS = Static.TARGET_WIDTH_BY_TWO;

    public PerkSelectionHandler(final World parent) {

        super(World.PERK_SELECTION,parent);
        this.objType = World.PERK_SELECTION;

        interpolator = new OvershootInterpolator(new OvershootInterpolator.InterpolationCompleteListener() {

            @Override
            public void onInterpolationComplete(int id) {
                if (id == STARTED) {
                    layout.enable();
                } else if (id == REWIND) {
                    parent.setChangeStateFlag(World.GAME_RUNNING, World.MSG_GAME_RUNNING, World.MSG_GAME_RUNNING);
                    return;
                } else if (id == REWIND_AND_EXIT) {
                    parent.setChangeStateFlag(World.MAIN_MENU, World.MSG_MAIN_MENU, World.MSG_MAIN_MENU);
                    return;
                }
                animState = ENDED;

            }
        });

        createUILayout();


        minBottomPos = Static.targetHeightFixer - LAYOUT_HEIGHT;
    }

    private void createUILayout()
    {
        coinLabel = new FontLabel(Assets.fnt_purisa,30);

        FontLabel selectPerksLabel = new FontLabel(Assets.fnt_playtime, 15);
        selectPerksLabel.set("Select Perks",true,1);


        Button.OnClickListener buyClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(Button button) {
               setCoinInHandValueAndChangeLabel(coinInHand + 100); //add +100 coins
               //Static.preferencesHandler.setNitroCapacity(2);
            }
        };

        buttonBuyCoins = new Button(1,0.7f,1.2f,Assets.button_generic_ss,buyClickListener);
        FontLabel buyLabel = new FontLabel(Assets.fnt_playtime,4);
        buyLabel.set("Buy",true,0.75f);
        buttonBuyCoins.addElement(buyLabel,0,0);


        //delete these two lines
        buttonBuyCoins.setDisablableOverlayDrawable();
        buttonBuyCoins.disable();


        Button.OnClickListener fillNitroListener = new Button.OnClickListener() {
            @Override
            public void onClick(Button button) {
                PerkButton b = (PerkButton)button;
                if(b.coinCost > coinInHand)
                {
                    Static.uiThreadHandler.showNotEnoughCoinToast();
                }
                else
                {
                    if(Static.world.nitro.addMorsel())
                    {
                        setCoinInHandValueAndChangeLabel(coinInHand - b.coinCost);
                    }
                }
            }
        };

        perkButtonFillNitro = new PerkButton(2.5f,2.5f,fillNitroListener,5);
        perkButtonFillNitro.setStrings("Fill Energy",0.7f,0.8f,"fill energy by\none unit",0,0.68f);




        Button.OnClickListener increaseNitroCapacityListener = new Button.OnClickListener() {
            @Override
            public void onClick(Button button) {
                PerkButton b = (PerkButton)button;
                if(b.coinCost > coinInHand)
                {
                    Static.uiThreadHandler.showNotEnoughCoinToast();
                    b.setClickCount(0);
                }
                else
                {
                    if(b.getClickCount() == 1)
                    {
                        Static.uiThreadHandler.showClickAgainToConfirmToast();
                    }
                    else if(b. getClickCount() == 2) {

                        b.setClickCount(0);

                        Nitro nitro = Static.world.nitro;

                        if (nitro.addCapacity()) {
                            setCoinInHandValueAndChangeLabel(coinInHand - b.coinCost);

                            Static.preferencesHandler.setCoinAmount(coinInHand); // because the change is permanent

                            b.setCoinCostAndLabel(Nitro.calculateNitroCapacityCost());

                            if (nitro.capacityFull()) {
                                button.disable();
                            }
                        }
                    }
                }
            }
        };

        perkButtonIncreaseNitroCapacity = new PerkButton(2.5f,2.5f,increaseNitroCapacityListener, Nitro.calculateNitroCapacityCost());
        perkButtonIncreaseNitroCapacity.setStrings("Increase Energy\nCapacity",0.4f,0.62f,"",0,0.7f);
        perkButtonIncreaseNitroCapacity.setDisablableOverlayDrawable();

        Button.OnClickListener startButtonClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(Button button) {
                if(!Static.sceneFileNotFound) {
                    animState = REWIND;
                    layout.disable();
                    interpolator.set(interpolator.currentPos, EXTREME_RIGHT, 4, REWIND, 2);
                }
            }
        };

        startGameButton = new Button(1,0.7f,1.2f,Assets.button_generic_ss,startButtonClickListener);

        Button.OnClickListener perkButtonListener = new Button.OnClickListener() {
            @Override
            public void onClick(Button button) {
                PerkButton b = (PerkButton)button;
                if(b.equipped)
                {
                    b.equipped = false; // deequip
                    setCoinInHandValueAndChangeLabel(coinInHand + b.coinCost); //reacquire coins
                }
                else
                {
                    if(b.coinCost > coinInHand)
                    {
                        Static.uiThreadHandler.showNotEnoughCoinToast();
                    }
                    else
                    {
                        b.equipped = true;
                        setCoinInHandValueAndChangeLabel(coinInHand - b.coinCost);
                    }
                }
            }
        };


        perkButtonHeadStart = new PerkButton(2.5f,2.5f,perkButtonListener, 5);
        perkButtonHeadStart.setStrings("Head Start",0.6f,0.7f,"blahblah\nblah",0,0.7f);
        perkButtonMoreFood = new PerkButton(2.5f,2.5f,perkButtonListener,5);
        perkButtonMoreFood.setStrings("More Food",0.6f,0.67f,"occurance of\nfood increased",0,0.66f);
        perkButtonLongerNitro = new PerkButton(2.5f,2.5f,perkButtonListener,5);
        perkButtonLongerNitro.setStrings("Longer Nitro",0.6f,0.65f,"nitro lasts\nlonger",0,0.7f);
        perkButtonProtectiveOrb = new PerkButton(2.5f,2.5f,perkButtonListener,5);
        perkButtonProtectiveOrb.setStrings("Protective\nOrb",0.65f,0.55f,"start with\nprotective orb",-0.2f,0.6f);


        layout = new GenericLayout(Static.TARGET_WIDTH,Static.targetHeightFixer,Assets.rect_black);
        layout.setPos(Static.TARGET_WIDTH_BY_TWO,Static.targetHeightFixerByTwo);

        layout.addElement(startGameButton,0, -6f );
        layout.addElement(coinLabel,-Static.TARGET_WIDTH_BY_TWO + 0.5f, Static.targetHeightFixer/2 - 0.5f);




        float relXLeft = -Static.TARGET_WIDTH_BY_TWO + 2;


        layout.addElement(buttonBuyCoins,-0.5f,Static.targetHeightFixer/2 - buttonBuyCoins.height/2 - 0.3f);
        layout.addElement(selectPerksLabel,0,0.8f);

        layout.addElement(perkButtonFillNitro,relXLeft,2.5f);
        layout.addElement(perkButtonIncreaseNitroCapacity,-relXLeft,2.5f);

        layout.addElement(perkButtonHeadStart,relXLeft,-1);
        layout.addElement(perkButtonMoreFood,-relXLeft,-1);
        layout.addElement(perkButtonLongerNitro,relXLeft,-4);
        layout.addElement(perkButtonProtectiveOrb,-relXLeft,-4);


    }

    private void setCoinInHandValueAndChangeLabel(int newValue)
    {
        coinInHand = newValue;
        coinLabel.set("x"+coinInHand,false, 1);
    }



    public boolean onTouchDown(float touchX, float touchY) {
        float correctedTouchY = touchY + Static.cam.bottomPos;
        //getCoins.onTouchDown(touchX, correctedTouchY);
        //perkButtonHeadStart.onTouchDown(touchX, correctedTouchY);

        boolean toReturn = layout.onTouchDown(touchX, correctedTouchY);

        prevTouchY = touchY;

        return toReturn;
    }

    public boolean onTouchUp(float touchX, float touchY) {
        float correctedTouchY = touchY + Static.cam.bottomPos;
        dragMode = false;

        return layout.onTouchUp(touchX, correctedTouchY);
    }

    @Override
    public boolean onTouchDrag(float touchX, float touchY) {
        if (animState == ENDED) {
            float deltaTouchY = touchY - prevTouchY;
            prevTouchY = touchY;

            if (!dragMode) {
                if (Math.abs(deltaTouchY) >= MIN_TOUCH_DRAG) {
                    dragMode = true;
                    layout.popUp();
                }
            }

            if (dragMode) {
                float camBottom = Static.cam.bottomPos - deltaTouchY;

                if (camBottom < minBottomPos) {
                    camBottom = minBottomPos;
                } else if (camBottom > 0) {
                    camBottom = 0;
                }

                Static.cam.bottomPos = camBottom;



                return true;
            }
        }
        return false;
    }



    public void update(float deltaTime) {


        layout.update(deltaTime);
        Static.world.nitro.update(deltaTime);

        //if(animState == STARTED || animState == REWIND || animState == REWIND_AND_EXIT)
        if (animState > ENDED)  //Optimized version than above
        {
            layout.setPosX(interpolator.getInterpolation(deltaTime));
        }
    }


    @Override
    public void draw() {
        WorldRenderer.presentPerkSelection(layout);

    }

    @Override
    public void onStart(char msg) {

        animState = STARTED;

        layout.disable();
        layout.unHide();
        layout.setPosX(EXTREME_RIGHT);
        interpolator.set(EXTREME_RIGHT,WORKING_POS,3,STARTED,2);

        coinInHand = Static.preferencesHandler.retrieveCoinAmount();
        coinLabel.set("x"+coinInHand,false,1);

        //Static.objHandler.printOutCollidableListPool();

        // disable and draw overlay on add capacity button if nitro capacity is already full
        if(Static.world.nitro.capacityFull())
        {
            perkButtonIncreaseNitroCapacity.disable();
        }

    }

    @Override
    public void onPause() {
        //nothing to do yet

    }

    @Override
    public void end(char msg) {

        if (msg == World.MSG_MAIN_MENU) {
            World.background.bottomCameraPos = 0;
            Static.cam.bottomPos = 0;

            Static.world.nitro.resetNitro();
        }
        else if(msg == World.MSG_GAME_RUNNING)  //checkout coins
        {
            // Static.session.setSessionPerks(perkButtonHeadStart.equipped,perkButtonProtectiveOrb.equipped,perkButtonLongerNitro.equipped,perkButtonMoreFood.equipped);
            Static.preferencesHandler.setCoinAmount(coinInHand);
            // Setting perk data
            Session session = Static.session;
            session.setSessionPerks(perkButtonHeadStart.checkOut(),perkButtonProtectiveOrb.checkOut(),perkButtonLongerNitro.checkOut(),perkButtonMoreFood.checkOut());
        }

        layout.hide();
    }


    @Override
    public void onResume() {
        //nothing to do as of yet :D
    }

    @Override
    public boolean onKeyPressed(KeyEvent event) {
        if (isBackPressed(event)) {
            //rewind and exit
            animState = REWIND_AND_EXIT;
            interpolator.set(interpolator.currentPos,EXTREME_RIGHT,5,REWIND_AND_EXIT,2);

            return true;
        }
        return false;
    }


}
