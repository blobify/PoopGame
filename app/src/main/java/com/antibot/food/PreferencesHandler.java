package com.antibot.food;

import android.content.SharedPreferences;

import com.antibot.food.gameobj.Nitro;

public class PreferencesHandler {



    public static final String KEY_COIN_AMOUNT = "key_coin_amount";
    public static final String KEY_NITRO_CAPACITY = "key_nitro_capacity";
    public static final String KEY_GEM_AMOUNT = "key_gem_amount";

    public static final String KEY_MOVEMENT_SENSI_AMOUNT = "key_sensi_slider_step_amount";

    public static final String KEY_MOVEMENT_MODE = "key_movement_mode";



    SharedPreferences preferences;

    public PreferencesHandler(SharedPreferences preferences)
    {
        this.preferences = preferences;

        //preferences.edit().putInt(KEY_COIN_AMOUNT, 1000).commit(); //TODO deletable
    }

    public int retrieveCoinAmount()
    {
        return preferences.getInt(KEY_COIN_AMOUNT, 0);
    }

    public void setCoinAmount(int amount)
    {
        preferences.edit().putInt(KEY_COIN_AMOUNT, amount).apply();
    }

    public int retrieveGemAmount() { return preferences.getInt(KEY_GEM_AMOUNT,0);}

    public void setGemAmount(int amount)
    {
        preferences.edit().putInt(KEY_GEM_AMOUNT,amount).apply();
    }

    public int retrieveNitroCapacity()
    {
        return preferences.getInt(KEY_NITRO_CAPACITY, Nitro.MIN_CAPACITY);
    }

    public void setNitroCapacity(int capacity)
    {
        preferences.edit().putInt(KEY_NITRO_CAPACITY, capacity).apply();
    }


    public int retrieveKeyAmount(String key, int defult)
    {
        return preferences.getInt(key, defult);
    }

    public void setKeyAmount(String key, int amount)
    {
        preferences.edit().putInt(key,amount).apply();
    }

    public void setMovementSensiAmount(float amount)
    {
        preferences.edit().putFloat(KEY_MOVEMENT_SENSI_AMOUNT, amount).apply();
    }

    public float getMovementSensiAmount()
    {
        return preferences.getFloat(KEY_MOVEMENT_SENSI_AMOUNT, 1);
    }

    public boolean retrieveKeyBoolean(String key, boolean defult)
    {
        return preferences.getBoolean(key,defult);
    }

    public void setKeyBoolean(String key, boolean val)
    {
        preferences.edit().putBoolean(key,val);
    }

    /*public float retreieveKeyAmount(String key, float defult)
    {
        return preferences.getFloat(key,defult);
    }

    public void setKeyAmount(String key, float amount)
    {
        preferences.edit().putFloat(key,amount).apply();
    }*/

}
