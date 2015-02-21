package com.antibot.ui;

import com.antibot.food.Assets;
import com.game.framework.gl.TextureRegion;

public class ImageFontLabelLayout extends GenericLayout {


    FontLabel label1, label2;
    ImageView imageView1, imageView2;

    public ImageFontLabelLayout(float width, float height, TextureRegion region)
    {
        super(width, height, region);

        label1 = new FontLabel(Assets.fnt_purisa,30);
        label2 = new FontLabel(Assets.fnt_purisa,30);

        imageView1 = new ImageView(0,0,null);
        imageView2 = new ImageView(0,0,null);


    }

    public FontLabel giveLabel1ForWork()
    {
        return (FontLabel)removeElement(label1);
    }
    public FontLabel giveLabel2ForWork()
    {
        return (FontLabel)removeElement(label2);
    }

    public ImageView giveImage1ForWork()
    {
        return (ImageView)removeElement(imageView1);
    }

    public ImageView giveImage2ForWork()
    {
        return (ImageView)removeElement(imageView2);
    }


    public void addLabel1BackToLayout(float relX, float relY)
    {
        addElement(label1,relX,relY);
    }
    public void addLabel2BackToLayout(float relX, float relY)
    {
        addElement(label2,relX,relY);
    }

    public void addImage1BackToLayout(float relX, float relY)
    {
        addElement(imageView1,relX,relY);
    }
    public void addImage2BackToLayout(float relX, float relY)
    {
        addElement(imageView2,relX,relY);
    }







}
