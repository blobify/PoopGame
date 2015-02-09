package com.game.framework.impl;

import android.media.SoundPool;

import com.game.framework.Sound;

public class AndroidSound implements Sound {
    int soundId;
    SoundPool soundPool;

    public AndroidSound(SoundPool soundPool, int soundId) {
        this.soundId = soundId;
        this.soundPool = soundPool;
    }

    @Override
    public int play(float volume) {
        return soundPool.play(soundId, volume, volume, 0, 0, 1);
    }

    @Override
    public int play(float volume,int loop) {
        return soundPool.play(soundId,volume,volume,0,loop,1);
    }

    @Override
    public void pause(int streamId) {soundPool.pause(streamId);}

    @Override
    public void stop()
    {
        soundPool.stop(soundId);
    }

    @Override
    public void dispose() {
        soundPool.unload(soundId);
    }

}
