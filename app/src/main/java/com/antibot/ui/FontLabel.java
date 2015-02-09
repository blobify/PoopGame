package com.antibot.ui;

import com.antibot.poop.Font;
import com.antibot.poop.Static;
import com.antibot.poop.WorldRenderer;
import com.game.framework.gl.SpriteBatcher;

public class FontLabel extends UIElement {
    public static final int SIZE_PER_INDEX = 4;

    public float[] positionAndSize;

    public int[] fontIndex;

    public Font font;

    public float labelWidth;
    // these guys required for scaling
    String str;

    boolean center;


    public FontLabel(Font font, int maxLen) {
        this.font = font;
        positionAndSize = new float[maxLen * SIZE_PER_INDEX];
        fontIndex = new int[maxLen];
    }

    /**
     * Sets the data of position and sizes and also the string to draw
     * @param str       string to draw
     *
     * @param center    should the drawn text be centered?
     * @param fontScale the default scale of the font
     */
    public void set(String str, boolean center, float fontScale) {

        this.str = str;
        this.scale = fontScale;
        this.center = center;
        store((parent == null)?1 : parent.scale);
    }

    private void store(float parentScale)
    {
        Font.Glyph[] glyph = font.glyph;

        final float startX = labelWidth = 0; //posX * parentScale;
        float advancerY = (font.lineHeight * scale / 2) * parentScale;//(posY + font.lineHeight * scale / 2) * parentScale;


        currentScale = this.scale * parentScale;

        float textLen = 0;

        int endCursorIndex = str.length();
        if(str.length() > fontIndex.length) endCursorIndex = fontIndex.length;

        for (int cur = 0, subStrStartIndex = 0, subStrEndIndex = 0; cur < endCursorIndex; cur++)
        {
            int charr = str.charAt(cur);

            if (charr >= 32 && charr <= 126)
            {
                subStrEndIndex++;
                if (center)
                    textLen += glyph[charr - 32].xAdvance * currentScale;
            }

            if (charr == 10 || cur == endCursorIndex-1)  // if new line or end of string
            {
                float advancerX = startX - textLen / 2; //will remain startX if center is true

                for (int i = subStrStartIndex; i < subStrEndIndex; i++)
                {
                    fontIndex[i] = str.charAt(i) - 32;

                    Font.Glyph g = glyph[str.charAt(i) - 32];

                    int startIndexForPosition = i * SIZE_PER_INDEX;

                    // store position and size values according to scale
                    positionAndSize[startIndexForPosition++] = advancerX + g.xOffset * currentScale;
                    positionAndSize[startIndexForPosition++] = advancerY - g.yOffset * currentScale;
                    positionAndSize[startIndexForPosition++] = g.region.drawWidth * currentScale;
                    positionAndSize[startIndexForPosition] = g.region.drawHeight * currentScale;

                    advancerX += g.xAdvance * currentScale;
                }

                if (charr == 10)
                {
                    labelWidth = Math.max(labelWidth, advancerX - (startX - textLen/2));
                    subStrStartIndex = subStrEndIndex = cur + 1;
                    textLen = 0;
                    advancerY -= font.lineHeight * currentScale; // go down
                }

            }
        }

        if (str.length() < fontIndex.length)
        {
            // mark region for ending font rendering
            fontIndex[str.length()] = -1;
        }
    }

    @Override

    public void onParentScaleSet() {

       this.posX = parent.posX + relX * parent.currentScale;
       this.posY = parent.posY + relY * parent.currentScale;

        if(str != null)
        store(parent.currentScale);

    }

    @Override
    public boolean isClickable() {
        return false;
    }

    @Override
    public void onParentPosSet() {

        this.posX = parent.posX + relX;
        this.posY = parent.posY + relY;
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void draw() {
        drw(0,0);
    }

    private void drw(float delX, float delY)
    {
        if(visible)
        {
            SpriteBatcher batcher = WorldRenderer.batcher;

            for (int i = 0; i < fontIndex.length; i++) // loop through all
            // characters to draw
            {
                if (fontIndex[i] == -1) {
                    break;
                }

                Font.Glyph g = font.glyph[fontIndex[i]];
                int startIndexForPosition = i * SIZE_PER_INDEX;
                float x = positionAndSize[startIndexForPosition++];
                float y = positionAndSize[startIndexForPosition++];
                float drawWidth = positionAndSize[startIndexForPosition++];
                float drawHeight = positionAndSize[startIndexForPosition];

                batcher.drawCarefulSpriteWithPivot(x + posX + delX, y + posY + delY, drawWidth, drawHeight, 0, 1, g.region, WorldRenderer.texShaderProgram);
            }
        }
    }

    @Override
    public void draw(float delX, float delY) {
        drw(delX,delY);
    }


}
