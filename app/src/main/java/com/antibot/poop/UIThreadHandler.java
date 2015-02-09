package com.antibot.poop;

public class UIThreadHandler {

    Runnable notEnoughCoinsToastRunnable;
    Runnable clickAgainToConfirmToastRunnable;

    long timeStamp;

    public UIThreadHandler() {
        notEnoughCoinsToastRunnable = new Runnable() {
            @Override
            public void run() {
                Static.game.gameUIElements.showNotEnoughCoinsToast();
            }
        };

        clickAgainToConfirmToastRunnable = new Runnable() {
            @Override
            public void run() {
                Static.game.gameUIElements.showClickAgainToConfirmToast();
            }
        };


        timeStamp = 0;
    }

    public void showNotEnoughCoinToast() {

        if (System.currentTimeMillis() - timeStamp > 1000)  // 1 second delay between each toast show
            Static.game.handler.post(notEnoughCoinsToastRunnable);
        timeStamp = System.currentTimeMillis();
    }

    public void showClickAgainToConfirmToast()
    {
        if(System.currentTimeMillis() - timeStamp > 1000)
        {
            Static.game.handler.post(clickAgainToConfirmToastRunnable);
            timeStamp = System.currentTimeMillis();
        }
    }
}
