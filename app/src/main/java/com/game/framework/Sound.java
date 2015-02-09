package com.game.framework;

public interface Sound {
    public int play(float volume);

    public int play(float volume, int loop);

    public void pause(int streamId);

    public void dispose();

    public void stop();
}
