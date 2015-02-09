package com.antibot.food;

public class ColorInterpolater {
    public static final int RED = 0, GREEN = 1, BLUE = 2, ALPHA = 3;
    public static final int NO_OF_FRAMES_PER_CHANGE = 10;
    public float colorArr[];

    public float color[];
    public float colorSteps[];

    int frameCount;
    int index;

//	final InterpolationCompleteListener interpolationCompleteListener;

    public ColorInterpolater() {
        //this.interpolationCompleteListener = interpolationCompleteListener;
        color = new float[4];
        color[3] = 1;
        colorSteps = new float[4];
        colorSteps[3] = 1;
    }

    public void setColorArr(float[] colorArr, int startIndex) {
        this.colorArr = colorArr;

        index = startIndex;

        loadColor();

        frameCount = 0;
        index = startIndex;

    }

    public void update() {
        for (int i = 0; i < color.length; i++) {
            color[i] += colorSteps[i];
        }

        frameCount++;

        if (frameCount >= NO_OF_FRAMES_PER_CHANGE) {
            frameCount = 0;
            index = (index + 3) % colorArr.length;

            loadColor();
        }
    }

    private void loadColor() {
        colorSteps[RED] = (colorArr[index + RED] - color[RED]) / NO_OF_FRAMES_PER_CHANGE;
        colorSteps[GREEN] = (colorArr[index + GREEN] - color[GREEN]) / NO_OF_FRAMES_PER_CHANGE;
        colorSteps[BLUE] = (colorArr[index + BLUE] - color[BLUE]) / NO_OF_FRAMES_PER_CHANGE;
    }

    public void reset() {
        for (int i = 0; i < color.length; i++) {
            color[i] = 0;
        }

        frameCount = 0;
    }
}
