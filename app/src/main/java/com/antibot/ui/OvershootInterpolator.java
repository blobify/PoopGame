package com.antibot.ui;

import com.framework.utils.Logger;

public class OvershootInterpolator {
    public float time;
    public float tension;
    public float startPos, endPos;
    public float timeScale;
    public int id;
    public float currentPos;

    public boolean started = false;
    InterpolationCompleteListener listener;

    public OvershootInterpolator(InterpolationCompleteListener listener) {
        this.listener = listener;
        this.tension = 2;
    }

    private static float getOvershootInterpolation(float time, final float tension) {
        time -= 1.0f;
        return time * time * ((tension + 1) * time + tension) + 1.0f;
    }

    public void set(float startPos, float endPos, float timeScale, int id, float tension) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.id = id;
        this.timeScale = timeScale;
        this.tension = tension;

        time = 0;
        currentPos = startPos;
        started = true;

    }

    public float getInterpolation(float deltaTime) {

            time += timeScale * deltaTime;

            if (time > 1.0f) {

                float toReturn = endPos;  // because endPos gets changed
                started = false;
                listener.onInterpolationComplete(id);
                return toReturn;
            }

            currentPos = (endPos - startPos) * getOvershootInterpolation(time, tension) + startPos;
            return currentPos;
    }

    public interface InterpolationCompleteListener {
        public void onInterpolationComplete(int id);
    }
}
