package com.antibot.poop;

import com.game.framework.gl.TextureRegion;

public class AnimeCounter {
    public static final boolean ANIME_ONCE = true, ANIME_REVERSE = false;

    public AnimPackage animePack;

    public int stepSize;
    int currentStep;
    int repeatCount;

    int currentCount;

    int nextIndexVector;
    public int currentIndex;
    int endIndex, startIndex;
    boolean animeMode;
    public boolean animeComplete;

    public TextureRegion[] arr;
    private TextureRegion textureRegion;

    public void setAnime(AnimPackage animePack) {
        if (this.animePack == animePack) // no overwriting
        {
            return;
        }

        this.arr = animePack.arr;

        this.animePack = animePack;

        animeMode = animePack.animMode;
        startIndex = 0;
        endIndex = animePack.arr.length - 1;

        stepSize = animePack.stepSize;
        repeatCount = animePack.repeatCount;
        currentCount = 0;

        animeComplete = false;

        reset(0);
    }

    public void resetAnime()
    {
        currentCount = 0;

        animeComplete = false;

        reset(0);
    }

    private void reset(int currentIndex) {
        nextIndexVector = 1;
        currentStep = 0;
        this.currentIndex = currentIndex;

    }

    public void setStepSize(int stepSize) {
        //if(this.stepSize != stepSize)
        //{
        this.stepSize = stepSize;
        currentStep = 0;
        //}
    }

    public void udpate() {
        if (!animeComplete) {
            if (currentStep >= stepSize) {
                currentStep = 0;

                currentIndex += nextIndexVector;

                if (currentIndex > endIndex) {
                    if (animeMode == ANIME_REVERSE) {
                        currentIndex = endIndex - 1;
                        nextIndexVector = -nextIndexVector;
                    } else {
                        currentIndex = endIndex;

                        currentCount++;
                        if (currentCount == repeatCount)
                            animeComplete = true;
                        else
                            reset(0);
                    }

                } else if (currentIndex < 0) // only possible if ANIME_REVERSE
                {
                    currentCount++;
                    if (currentCount == repeatCount) {
                        currentIndex = 0;
                        animeComplete = true;
                    } else
                        reset(1);

                }

            }

            currentStep++;
        }
    }

    public TextureRegion getTextureRegion() {
        return arr[currentIndex];
    }


    public static class AnimPackage {
        TextureRegion[] arr;
        boolean animMode; // true means once, false means reverse
        int repeatCount;
        int stepSize = 1; // has to be >= 1

        public AnimPackage(TextureRegion[] arr, boolean animMode, int repeatCount, int stepSize) {
            set(arr, animMode, repeatCount, stepSize);
        }

        public void set(TextureRegion[] arr, boolean animMode, int repeatCount, int stepSize) {
            this.arr = arr;
            this.animMode = animMode;
            this.repeatCount = repeatCount;

            this.stepSize = stepSize;
        }
    }

}
