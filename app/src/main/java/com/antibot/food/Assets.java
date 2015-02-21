package com.antibot.food;

import com.antibot.poop.R;
import com.framework.utils.TextureHelper;
import com.game.framework.Game;
import com.game.framework.Sound;
import com.game.framework.gl.Texture;
import com.game.framework.gl.TextureRegion;

import java.io.IOException;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE1;

public class Assets {
    //public static String TAG = "Assets";

    public static Texture atlas, ui_atlas;

    public static TextureRegion loadingScreenRegion;


    public static TextureRegion gem_decor, bomb_exp3, explosion, bomb_exp2, shooter_norm, wallblock, big_baddy_norm1, protective_orb, coin_region, bomb_exp1, bomb_exp4, path_4, path_9, bullet_burst2, particle1, particle2, bomb_spark1, nitro_empty, bomb_spark2, nitro_full, bullet_state3, bigb_particle2, wb_particle2, dumbo_particle2, bullet_state2, bullet_state1, path_2, path_6, path_3, path_10, path_5, path_1, path_8, path_13, path_7, path_11, path_16, path_15, path_12, path_14, bullet_burst1, wb_particle1, bullet_burst3, musca_wing2, bb_wing3, bb_wing2, bb_wing1, bomb, musca_mouth1, musca_norm3, musca_norm1, musca_mouth2, musca_mouth3, musca_norm2, musca_chew2, musca_chew3, musca_chew1, gem, food1, food2, dumb_enemy_norm2, dumb_enemy_norm1, dumb_enemy_norm3, bb_indicator, musca_wing3, bigb_particle1, star, musca_wing1, dumbo_particle1;

    public static TextureRegion background2, slider_button_down, button_overlay, rect_black, rect_white, button_play, radio_button_off, indicator_touch, radio_button_on, indicator_phone_1, button_generic_ms, slider_button_up, background, button_generic_sm, button_generic_ss, button_settings, pause_button, indicator_arrow, indicator_phone_2, indicator_phone_3, button_generic_mm, button_generic_ms_clicked, button_generic_ss_clicked, label_equipped, slider_base;
    public static TextureRegion[] musca_chew_arr, musca_norm_arr, musca_mouth_arr, big_baddy_arr, dumb_enemy_arr, path_arr, shooter_arr, bb_wing_arr, musca_wing_arr, food_arr, particle_arr, bullet_state_arr, bullet_burst_arr, dumbo_particle_arr, bigb_particle_arr, bomb_spark_arr, bomb_exp_arr, wb_particle_arr, indicator_phone_arr;



    public static Font fnt_purisa;
    public static Font fnt_playtime;

    public static Sound coin;

    public static Texture[] textureArray = new Texture[2];


    public static void loadObjects(Game game) {

        gem_decor = new TextureRegion(atlas, 0, 0, 115, 115);
        bomb_exp3 = new TextureRegion(atlas, 117, 0, 123, 96);
        explosion = new TextureRegion(atlas, 242, 0, 100, 93);
        bomb_exp2 = new TextureRegion(atlas, 344, 0, 95, 84);
        shooter_norm = new TextureRegion(atlas, 441, 0, 80, 76);
        wallblock = new TextureRegion(atlas, 523, 0, 75, 75);
        big_baddy_norm1 = new TextureRegion(atlas, 600, 0, 120, 70);
        protective_orb = new TextureRegion(atlas, 722, 0, 69, 69);
        coin_region = new TextureRegion(atlas, 793, 0, 67, 67);
        bomb_exp1 = new TextureRegion(atlas, 862, 0, 80, 54);
        bomb_exp4 = new TextureRegion(atlas, 944, 0, 60, 48);
        path_4 = new TextureRegion(atlas, 1006, 0, 15, 15);
        path_9 = new TextureRegion(atlas, 1006, 17, 15, 15);
        bullet_burst2 = new TextureRegion(atlas, 1006, 34, 15, 14);
        particle1 = new TextureRegion(atlas, 944, 50, 5, 4);
        particle2 = new TextureRegion(atlas, 951, 50, 5, 4);
        bomb_spark1 = new TextureRegion(atlas, 958, 50, 3, 3);
        nitro_empty = new TextureRegion(atlas, 964, 51, 1, 1);
        bomb_spark2 = new TextureRegion(atlas, 968, 50, 4, 3);
        nitro_full = new TextureRegion(atlas, 975, 51, 1, 1);
        bullet_state3 = new TextureRegion(atlas, 862, 56, 19, 11);
        bigb_particle2 = new TextureRegion(atlas, 117, 98, 25, 17);
        wb_particle2 = new TextureRegion(atlas, 144, 98, 17, 16);
        dumbo_particle2 = new TextureRegion(atlas, 163, 98, 19, 16);
        bullet_state2 = new TextureRegion(atlas, 184, 98, 18, 16);
        bullet_state1 = new TextureRegion(atlas, 204, 98, 19, 16);
        path_2 = new TextureRegion(atlas, 225, 98, 15, 15);
        path_6 = new TextureRegion(atlas, 242, 98, 15, 15);
        path_3 = new TextureRegion(atlas, 259, 98, 15, 15);
        path_10 = new TextureRegion(atlas, 276, 98, 15, 15);
        path_5 = new TextureRegion(atlas, 293, 98, 15, 15);
        path_1 = new TextureRegion(atlas, 310, 98, 15, 15);
        path_8 = new TextureRegion(atlas, 327, 98, 15, 15);
        path_13 = new TextureRegion(atlas, 344, 98, 15, 15);
        path_7 = new TextureRegion(atlas, 361, 98, 15, 15);
        path_11 = new TextureRegion(atlas, 378, 98, 15, 15);
        path_16 = new TextureRegion(atlas, 395, 98, 15, 15);
        path_15 = new TextureRegion(atlas, 412, 98, 15, 15);
        path_12 = new TextureRegion(atlas, 429, 98, 15, 15);
        path_14 = new TextureRegion(atlas, 446, 98, 15, 15);
        bullet_burst1 = new TextureRegion(atlas, 463, 98, 16, 14);
        wb_particle1 = new TextureRegion(atlas, 481, 98, 12, 12);
        bullet_burst3 = new TextureRegion(atlas, 495, 98, 15, 12);
        musca_wing2 = new TextureRegion(atlas, 512, 98, 90, 12);
        bb_wing3 = new TextureRegion(atlas, 0, 117, 182, 51);
        bb_wing2 = new TextureRegion(atlas, 184, 117, 174, 51);
        bb_wing1 = new TextureRegion(atlas, 360, 117, 166, 51);
        bomb = new TextureRegion(atlas, 528, 117, 53, 46);
        musca_mouth1 = new TextureRegion(atlas, 583, 117, 50, 45);
        musca_norm3 = new TextureRegion(atlas, 635, 117, 50, 45);
        musca_norm1 = new TextureRegion(atlas, 687, 117, 50, 45);
        musca_mouth2 = new TextureRegion(atlas, 739, 117, 50, 45);
        musca_mouth3 = new TextureRegion(atlas, 791, 117, 50, 45);
        musca_norm2 = new TextureRegion(atlas, 843, 117, 50, 45);
        musca_chew2 = new TextureRegion(atlas, 895, 117, 50, 45);
        musca_chew3 = new TextureRegion(atlas, 947, 117, 50, 45);
        musca_chew1 = new TextureRegion(atlas, 0, 170, 50, 45);
        gem = new TextureRegion(atlas, 52, 170, 46, 44);
        food1 = new TextureRegion(atlas, 100, 170, 50, 44);
        food2 = new TextureRegion(atlas, 152, 170, 50, 43);
        dumb_enemy_norm2 = new TextureRegion(atlas, 204, 170, 59, 41);
        dumb_enemy_norm1 = new TextureRegion(atlas, 265, 170, 59, 40);
        dumb_enemy_norm3 = new TextureRegion(atlas, 326, 170, 60, 40);
        bb_indicator = new TextureRegion(atlas, 388, 170, 40, 30);
        musca_wing3 = new TextureRegion(atlas, 430, 170, 88, 29);
        bigb_particle1 = new TextureRegion(atlas, 520, 170, 38, 27);
        star = new TextureRegion(atlas, 560, 170, 27, 26);
        musca_wing1 = new TextureRegion(atlas, 589, 170, 89, 26);
        dumbo_particle1 = new TextureRegion(atlas, 680, 170, 26, 18);



        background2 = new TextureRegion(ui_atlas, 1, 0, 640, 360);
        slider_button_down = new TextureRegion(ui_atlas, 902, 0, 79, 79);
        button_overlay = new TextureRegion(ui_atlas, 983, 0, 24, 17);
        rect_black = new TextureRegion(ui_atlas, 1010, 1, 9, 9);
        rect_white = new TextureRegion(ui_atlas, 984, 20, 9, 9);
        button_play = new TextureRegion(ui_atlas, 902, 81, 70, 70);
        radio_button_off = new TextureRegion(ui_atlas, 974, 81, 40, 40);
        indicator_touch = new TextureRegion(ui_atlas, 902, 153, 69, 61, true);
        radio_button_on = new TextureRegion(ui_atlas, 973, 153, 40, 40);
        indicator_phone_1 = new TextureRegion(ui_atlas, 902, 216, 61, 35, true);
        button_generic_ms = new TextureRegion(ui_atlas, 644, 258, 280, 80);
        slider_button_up = new TextureRegion(ui_atlas, 926, 258, 58, 58);
        background = new TextureRegion(ui_atlas, 1, 362, 640, 360);
        button_generic_sm = new TextureRegion(ui_atlas, 644, 362, 160, 160);
        button_generic_ss = new TextureRegion(ui_atlas, 806, 362, 160, 80);
        button_settings = new TextureRegion(ui_atlas, 968, 362, 52, 52);
        pause_button = new TextureRegion(ui_atlas, 806, 444, 50, 50);
        indicator_arrow = new TextureRegion(ui_atlas, 858, 444, 78, 42);
        indicator_phone_2 = new TextureRegion(ui_atlas, 938, 444, 75, 30, true);
        indicator_phone_3 = new TextureRegion(ui_atlas, 806, 496, 80, 22, true);
        button_generic_mm = new TextureRegion(ui_atlas, 644, 524, 280, 160);
        button_generic_ms_clicked = new TextureRegion(ui_atlas, 732, 724, 280, 80);
        button_generic_ss_clicked = new TextureRegion(ui_atlas, 732, 806, 160, 80);
        label_equipped = new TextureRegion(ui_atlas, 0, 926, 187, 49);
        slider_base = new TextureRegion(ui_atlas, 189, 926, 400, 30);



        musca_chew_arr = new TextureRegion[] {musca_chew1, musca_chew2, musca_chew3} ;
        musca_norm_arr = new TextureRegion[] {musca_norm1, musca_norm2, musca_norm3} ;
        musca_mouth_arr = new TextureRegion[] {musca_mouth1, musca_mouth2, musca_mouth3} ;
        big_baddy_arr = new TextureRegion[] {big_baddy_norm1} ;
        dumb_enemy_arr = new TextureRegion[] {dumb_enemy_norm1, dumb_enemy_norm2, dumb_enemy_norm3} ;
        path_arr = new TextureRegion[] {path_1, path_2, path_3, path_4, path_5, path_6, path_7, path_8, path_9, path_10, path_11, path_12, path_13, path_14, path_15, path_16} ;
        shooter_arr = new TextureRegion[] {shooter_norm} ;
        bb_wing_arr = new TextureRegion[] {bb_wing1, bb_wing2, bb_wing3} ;
        musca_wing_arr = new TextureRegion[] {musca_wing1, musca_wing2, musca_wing3} ;
        food_arr = new TextureRegion[] {food1, food2} ;
        particle_arr = new TextureRegion[] {particle1, particle2} ;
        bullet_state_arr = new TextureRegion[] {bullet_state1, bullet_state2, bullet_state3} ;
        bullet_burst_arr = new TextureRegion[] {bullet_burst1, bullet_burst2, bullet_burst3} ;
        dumbo_particle_arr = new TextureRegion[] {dumbo_particle1, dumbo_particle2} ;
        bigb_particle_arr = new TextureRegion[] {bigb_particle1, bigb_particle2} ;
        bomb_spark_arr = new TextureRegion[] {bomb_spark1, bomb_spark2} ;
        bomb_exp_arr = new TextureRegion[] {bomb_exp1, bomb_exp2, bomb_exp3, bomb_exp4} ;
        wb_particle_arr = new TextureRegion[] {wb_particle1, wb_particle2} ;


        indicator_phone_arr = new TextureRegion[] {indicator_phone_1, indicator_phone_2, indicator_phone_3} ;


        try {
            fnt_purisa = new Font(ui_atlas, 644, 0, game.getFileIO().readAsset("fnt_purisa.fnt"));
            fnt_playtime = new Font(ui_atlas, 0, 724, game.getFileIO().readAsset("fnt_playtime.fnt"));
        } catch (IOException e) {
            e.printStackTrace();
        } //new TextureRegion(atlas,0,361,512,256);


        coin = game.getAudio().newSound("coin.ogg");

        //textureArray = new Texture[]{atlas, ui_atlas};
        fillTextureArray();

    }

    private static void fillTextureArray()
    {


        textureArray[0] = atlas;
        textureArray[1] = ui_atlas;
    }

   /* public static void loadTextures(Game game) {

        atlas = TextureHelper.loadTexture(game.getContext(), R.drawable.atlas, GL_TEXTURE0);

        ui_atlas = TextureHelper.loadTexture(game.getContext(), R.drawable.ui_atlas, GL_TEXTURE1);


    }*/


    public static Texture getTextureFromTextureRegion(TextureRegion region) {
       /* if (region.parentTextureActiveUnit == GL_TEXTURE0) {
            return atlas;
        }*/

        return textureArray[region.parentTextureActiveUnit-GL_TEXTURE0];
    }

    public static void loadTexturesAndShaderPrograms(Game game) {

        WorldRenderer.reloadShaderPrograms(game);


        atlas = TextureHelper.loadTexture(game.getContext(), R.drawable.atlas, GL_TEXTURE0);
        ui_atlas = TextureHelper.loadTexture(game.getContext(), R.drawable.ui_atlas, GL_TEXTURE1);

        fillTextureArray();  // spent an entire day to find this solution remember?


    }



}
