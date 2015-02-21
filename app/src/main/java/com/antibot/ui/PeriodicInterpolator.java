package com.antibot.ui;

public class PeriodicInterpolator
{

    private float interpolationAbsolute;

    private float tension;
    private float min;
    private float max;
    private float delta;

    private float acc, vel;

    public void set(float min,float max,float tension)
    {
        this.min = min; this.max = max; this.tension = tension;
        this.delta = Math.abs(max - min);

        interpolationAbsolute = 1;
        acc = -tension;
        vel = 0;
    }

    public void update(float deltaTime)
    {
        acc = -tension*interpolationAbsolute;
        vel += acc*deltaTime;

        interpolationAbsolute += vel*deltaTime;

        if(interpolationAbsolute > 1) {interpolationAbsolute = 1; vel = 0;}
        else if(interpolationAbsolute < -1) {interpolationAbsolute = -1; vel = 0;}
    }

    public float getInterpolation()
    {
        return min + 0.5f*(1+interpolationAbsolute)*(delta);
    }
}